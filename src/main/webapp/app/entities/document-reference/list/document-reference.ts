import { HttpHeaders } from '@angular/common/http';
import { ChangeDetectionStrategy, Component, OnInit, effect, inject, signal, untracked } from '@angular/core';
import { toSignal } from '@angular/core/rxjs-interop';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Data, ParamMap, Router, RouterLink } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap/modal';
import { NgbPagination } from '@ng-bootstrap/ng-bootstrap/pagination';
import { Subscription, combineLatest, filter, firstValueFrom, tap } from 'rxjs';

import { DEFAULT_SORT_DATA, ITEM_DELETED_EVENT, SORT } from 'app/config/navigation.constants';
import { ITEMS_PER_PAGE, PAGE_HEADER, TOTAL_COUNT_RESPONSE_HEADER } from 'app/config/pagination.constants';
import { Alert } from 'app/shared/alert/alert';
import { AlertError } from 'app/shared/alert/alert-error';
import { FormatMediumDatetimePipe } from 'app/shared/date';
import { Filter, FilterOptions, IFilterOption, IFilterOptions } from 'app/shared/filter';
import { ItemCount } from 'app/shared/pagination';
import { SortByDirective, SortDirective, SortService, type SortState, sortStateSignal } from 'app/shared/sort';
import { DocumentReferenceDeleteDialog } from '../delete/document-reference-delete-dialog';
import { IDocumentReference } from '../document-reference.model';
import { DocumentReferenceService } from '../service/document-reference.service';
import { ITypeOfDocument } from 'app/entities/type-of-document/type-of-document.model';
import { TypeOfDocumentService } from 'app/entities/type-of-document/service/type-of-document.service';

import { jsPDF } from 'jspdf';
import autoTable from 'jspdf-autotable';
import dayjs from 'dayjs/esm';

@Component({
  changeDetection: ChangeDetectionStrategy.OnPush,
  selector: 'jhi-document-reference',
  templateUrl: './document-reference.html',
  imports: [
    RouterLink,
    FormsModule,
    FontAwesomeModule,
    AlertError,
    Alert,
    SortDirective,
    SortByDirective,
    FormatMediumDatetimePipe,
    Filter,
    NgbPagination,
    ItemCount,
  ],
})
export class DocumentReference implements OnInit {
  subscription: Subscription | null = null;
  readonly documentReferences = signal<IDocumentReference[]>([]);
  readonly isExporting = signal(false);

  sortState = sortStateSignal({});
  filters: IFilterOptions = new FilterOptions();
  referenceNo?: string;
  documentTitle?: string;
  author?: string;
  remarks?: string;
  typeOfDocumentId: number | null = null;

  dateFrom?: string;
  dateTo?: string;

  typeOfDocuments = signal<ITypeOfDocument[]>([]);

  readonly itemsPerPage = signal(ITEMS_PER_PAGE);
  readonly totalItems = signal(0);
  readonly page = signal(1);

  readonly router = inject(Router);
  protected readonly documentReferenceService = inject(DocumentReferenceService);
  // eslint-disable-next-line @typescript-eslint/member-ordering
  readonly isLoading = this.documentReferenceService.documentReferencesResource.isLoading;
  protected readonly activatedRoute = inject(ActivatedRoute);
  protected readonly sortService = inject(SortService);
  protected readonly filterOptions = toSignal(this.filters.filterChanges);
  protected modalService = inject(NgbModal);
  protected readonly typeOfDocumentService = inject(TypeOfDocumentService);

  constructor() {
    effect(() => {
      const headers = this.documentReferenceService.documentReferencesResource.headers();
      if (headers) {
        this.fillComponentAttributesFromResponseHeader(headers);
      }
    });
    effect(() => {
      this.documentReferences.set(this.fillComponentAttributesFromResponseBody([...this.documentReferenceService.documentReferences()]));
    });

    effect(() => {
      const filterOptions = this.filterOptions();
      if (filterOptions) {
        untracked(() => {
          // Only watch for filter changes. Other signals should be ignored.
          this.handleNavigation(1, this.sortState(), filterOptions);
        });
      }
    });
  }

  trackId = (item: IDocumentReference): number => this.documentReferenceService.getDocumentReferenceIdentifier(item);

  ngOnInit(): void {
    this.loadTypeOfDocuments();
    this.subscription = combineLatest([this.activatedRoute.queryParamMap, this.activatedRoute.data])
      .pipe(
        tap(([params, data]) => this.fillComponentAttributeFromRoute(params, data)),
        tap(() => this.load()),
      )
      .subscribe();
  }

  async exportAllFilteredPdf(): Promise<void> {
    if (this.isExporting()) {
      return;
    }

    this.isExporting.set(true);

    try {
      const allRows = await this.fetchAllFilteredDocumentReferences();

      if (allRows.length === 0) {
        return;
      }

      this.generateDocumentReferencePdf(allRows);
    } finally {
      this.isExporting.set(false);
    }
  }

  protected async fetchAllFilteredDocumentReferences(): Promise<IDocumentReference[]> {
    const pageSize = 1000;
    let page = 0;
    let allRows: IDocumentReference[] = [];

    while (true) {
      const queryObject = this.buildExportQuery(page, pageSize);

      const response = await firstValueFrom(this.documentReferenceService.query(queryObject));

      const rows = response.body ?? [];

      if (rows.length === 0) {
        break;
      }

      allRows = [...allRows, ...rows];

      const totalCountHeader = response.headers.get(TOTAL_COUNT_RESPONSE_HEADER);

      if (totalCountHeader) {
        const total = Number(totalCountHeader);

        if (!Number.isNaN(total) && allRows.length >= total) {
          break;
        }
      } else if (rows.length < pageSize) {
        break;
      }

      page++;

      // Safety guard to avoid infinite loop
      if (page > 1000) {
        break;
      }
    }

    return allRows;
  }

  protected buildExportQuery(page: number, size: number): any {
    const queryObject: any = {
      page,
      size,
      eagerload: true,
      sort: this.sortService.buildSortParam(this.sortState()),
    };

    for (const filterOption of this.filters.filterOptions) {
      /*
      Use the same filter format as your table query.

      Your current queryBackend() uses:

        queryObject[filterOption.name] = filterOption.values;

      So this export also uses filterOption.name.

      If you later change queryBackend() to use filterOption.nameAsQueryParam(),
      then change this line too.
    */
      queryObject[filterOption.name] = filterOption.values;
    }

    return queryObject;
  }

  protected generateDocumentReferencePdf(rows: IDocumentReference[]): void {
    const doc = new jsPDF({
      orientation: 'landscape',
      unit: 'pt',
      format: 'a4',
    });

    const pageWidth = doc.internal.pageSize.getWidth();

    const bismillahLogo = 'content/images/bismillah.png';
    const leftLogo = 'content/images/cropped-CAPITOLLOGO-01.png';
    const rightLogo = 'content/images/BARMM-OFFICIAL-LOGO.png';

    const loadImage = (url: string): Promise<string> =>
      fetch(url)
        .then(response => response.blob())
        .then(
          blob =>
            new Promise<string>(resolve => {
              const reader = new FileReader();
              reader.onload = () => resolve(reader.result as string);
              reader.readAsDataURL(blob);
            }),
        );

    Promise.all([loadImage(bismillahLogo), loadImage(leftLogo), loadImage(rightLogo)])
      .then(([bismillahImg, leftImg, rightImg]) => {
        // Add header images
        // Add header images (Adjusted width/height to match the text block)
        doc.addImage(leftImg, 'PNG', 20, 15, 45, 60);
        doc.addImage(rightImg, 'PNG', pageWidth - 65, 15, 45, 60);
        //doc.addImage(bismillahImg, 'PNG', pageWidth / 2 - 20, 5, 40, 18);

        // Set header text (Increased Y spacing to prevent overlapping)
        doc.setFont('helvetica', 'bold');
        doc.setFontSize(9);
        doc.setTextColor(0, 0, 0);
        doc.text('Republic of the Philippines', pageWidth / 2, 30, { align: 'center' });

        doc.setFontSize(11);
        doc.text('BANGSAMORO AUTONOMOUS REGION IN MUSLIM MINDANAO', pageWidth / 2, 44, { align: 'center' });

        doc.setFontSize(9);
        doc.text('Province of Lanao del Sur', pageWidth / 2, 57, { align: 'center' });

        doc.setFontSize(12);
        doc.text('PROVINCIAL LEGAL SERVICES', pageWidth / 2, 72, { align: 'center' });

        doc.setFontSize(9);
        doc.text('New Capitol Complex, Buadi Sacayo, Marawi City', pageWidth / 2, 85, { align: 'center' });

        // Line separator (Moved down to 98)
        doc.setLineWidth(0.5);
        doc.line(15, 98, pageWidth - 15, 98);

        // Report title (Moved down to 115)
        doc.setFontSize(14);
        doc.text('Document Reference Report', pageWidth / 2, 115, { align: 'center' }); // Change to 'Document Reference Report' for the other file

        // Table setup
        const columns = [
          'Date',
          'Reference No',
          'Document Title',
          'Type Of Document',
          'Author',
          'Date Released',
          'Submitted To Sir King',
          'Remarks',
        ];

        const tableRows = rows.map(documentReference => [
          this.formatDateTimeForPdf(documentReference.date),
          documentReference.referenceNo ?? '',
          documentReference.documentTitle ?? '',
          documentReference.typeOfDocument?.name ?? '',
          documentReference.author ?? '',
          this.formatDateTimeForPdf(documentReference.dateReleased),
          this.formatDateTimeForPdf(documentReference.submittedToSirKing),
          documentReference.remarks ?? '',
        ]);

        autoTable(doc, {
          head: [columns],
          body: tableRows,
          startY: 125,
          styles: {
            fontSize: 9,
            cellPadding: 3,
            halign: 'left',
            overflow: 'linebreak',
          },
          headStyles: {
            fillColor: [200, 200, 200],
            textColor: 0,
            fontStyle: 'bold',
          },
          alternateRowStyles: { fillColor: [240, 240, 240] },
          margin: { top: 10, left: 20, right: 20 },
          theme: 'grid',
        });

        // Add Footer
        const pageCount = doc.getNumberOfPages();
        for (let i = 1; i <= pageCount; i++) {
          doc.setPage(i);
          doc.setFontSize(10);
          doc.setTextColor(100);
          doc.text(`Page ${i} of ${pageCount}`, pageWidth / 2, doc.internal.pageSize.getHeight() - 15, { align: 'center' });
        }

        doc.save(`document-references-${dayjs().format('YYYY-MM-DD-HH-mm')}.pdf`);
      })
      .catch((error: unknown) => {
        console.error('Error loading images for PDF:', error);
      });
  }

  protected formatDateTimeForPdf(value?: dayjs.Dayjs | null): string {
    if (!value) {
      return '';
    }

    return `${value.format('MMM[.] D, YYYY h:mm')}${value.format('a')}`;
  }

  applyFilter(): void {
    this.filters.clear();

    if (this.referenceNo?.trim()) {
      this.filters.addFilter('referenceNo.contains', this.referenceNo.trim());
    }
    if (this.documentTitle?.trim()) {
      this.filters.addFilter('documentTitle.contains', this.documentTitle.trim());
    }
    if (this.author?.trim()) {
      this.filters.addFilter('author.contains', this.author.trim());
    }
    if (this.remarks?.trim()) {
      this.filters.addFilter('remarks.contains', this.remarks.trim());
    }
    if (this.typeOfDocumentId !== null && this.typeOfDocumentId !== undefined) {
      this.filters.addFilter('typeOfDocumentId.equals', String(this.typeOfDocumentId));
    }

    const dateFromInstant = this.toInstant(this.dateFrom);
    if (dateFromInstant) {
      this.filters.addFilter('date.greaterThanOrEqual', dateFromInstant);
    }

    const dateToInstant = this.toInstant(this.dateTo, true);
    if (dateToInstant) {
      this.filters.addFilter('date.lessThanOrEqual', dateToInstant);
    }
  }

  clearFilter(): void {
    this.referenceNo = undefined;
    this.documentTitle = undefined;
    this.author = undefined;
    this.remarks = undefined;
    this.typeOfDocumentId = null;
    this.dateFrom = undefined;
    this.dateTo = undefined;
    this.filters.clear();
  }

  delete(documentReference: IDocumentReference): void {
    const modalRef = this.modalService.open(DocumentReferenceDeleteDialog, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.documentReference = documentReference;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed
      .pipe(
        filter(reason => reason === ITEM_DELETED_EVENT),
        tap(() => this.load()),
      )
      .subscribe();
  }

  load(): void {
    this.queryBackend();
  }

  navigateToWithComponentValues(event: SortState): void {
    this.handleNavigation(this.page(), event, this.filters.filterOptions);
  }

  navigateToPage(page: number): void {
    this.handleNavigation(page, this.sortState(), this.filters.filterOptions);
  }

  protected toInstant(value: string | undefined, endOfRange = false): string | undefined {
    if (!value) {
      return undefined;
    }

    if (endOfRange) {
      return dayjs(value).endOf('minute').toISOString();
    }

    return dayjs(value).startOf('minute').toISOString();
  }

  protected loadTypeOfDocuments(): void {
    this.typeOfDocumentService.query({ page: 0, size: 1000, sort: ['name,asc'] }).subscribe({
      next: res => {
        this.typeOfDocuments.set(res.body ?? []);
      },
      error: () => {
        this.typeOfDocuments.set([]);
      },
    });
  }

  protected fillComponentAttributeFromRoute(params: ParamMap, data: Data): void {
    const page = params.get(PAGE_HEADER);
    this.page.set(+(page ?? 1));
    this.sortState.set(this.sortService.parseSortParam(params.get(SORT) ?? data[DEFAULT_SORT_DATA]));
    this.filters.initializeFromParams(params);
  }

  protected fillComponentAttributesFromResponseBody(data: IDocumentReference[]): IDocumentReference[] {
    return data;
  }

  protected fillComponentAttributesFromResponseHeader(headers: HttpHeaders): void {
    this.totalItems.set(Number(headers.get(TOTAL_COUNT_RESPONSE_HEADER)));
  }

  protected queryBackend(): void {
    const pageToLoad: number = this.page();
    const queryObject: any = {
      page: pageToLoad - 1,
      size: this.itemsPerPage(),
      eagerload: true,
      sort: this.sortService.buildSortParam(this.sortState()),
    };
    for (const filterOption of this.filters.filterOptions) {
      queryObject[filterOption.name] = filterOption.values;
    }
    this.documentReferenceService.documentReferencesParams.set(queryObject);
  }

  protected handleNavigation(page: number, sortState: SortState, filterOptions?: IFilterOption[]): void {
    const queryParamsObj: any = {
      page,
      size: this.itemsPerPage(),
      sort: this.sortService.buildSortParam(sortState),
    };

    if (filterOptions) {
      for (const filterOption of filterOptions) {
        queryParamsObj[filterOption.nameAsQueryParam()] = filterOption.values;
      }
    }

    this.router.navigate(['./'], {
      relativeTo: this.activatedRoute,
      queryParams: queryParamsObj,
    });
  }
}
