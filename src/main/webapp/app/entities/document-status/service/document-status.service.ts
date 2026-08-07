import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Injectable, computed, inject, signal } from '@angular/core';

import { Observable } from 'rxjs';

import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { isPresent } from 'app/core/util/operators';
import { IDocumentStatus, NewDocumentStatus } from '../document-status.model';

export type PartialUpdateDocumentStatus = Partial<IDocumentStatus> & Pick<IDocumentStatus, 'id'>;

@Injectable()
export class DocumentStatusesService {
  readonly documentStatusesParams = signal<Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined>(
    undefined,
  );
  readonly documentStatusesResource = httpResource<IDocumentStatus[]>(() => {
    const params = this.documentStatusesParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of documentStatus that have been fetched. It is updated when the documentStatusesResource emits a new value.
   * In case of error while fetching the documentStatuses, the signal is set to an empty array.
   */
  readonly documentStatuses = computed(() => (this.documentStatusesResource.hasValue() ? this.documentStatusesResource.value() : []));
  protected readonly applicationConfigService = inject(ApplicationConfigService);
  protected readonly resourceUrl = this.applicationConfigService.getEndpointFor('api/document-statuses');
}

@Injectable({ providedIn: 'root' })
export class DocumentStatusService extends DocumentStatusesService {
  protected readonly http = inject(HttpClient);

  create(documentStatus: NewDocumentStatus): Observable<IDocumentStatus> {
    return this.http.post<IDocumentStatus>(this.resourceUrl, documentStatus);
  }

  update(documentStatus: IDocumentStatus): Observable<IDocumentStatus> {
    return this.http.put<IDocumentStatus>(
      `${this.resourceUrl}/${encodeURIComponent(this.getDocumentStatusIdentifier(documentStatus))}`,
      documentStatus,
    );
  }

  partialUpdate(documentStatus: PartialUpdateDocumentStatus): Observable<IDocumentStatus> {
    return this.http.patch<IDocumentStatus>(
      `${this.resourceUrl}/${encodeURIComponent(this.getDocumentStatusIdentifier(documentStatus))}`,
      documentStatus,
    );
  }

  find(id: number): Observable<IDocumentStatus> {
    return this.http.get<IDocumentStatus>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  query(req?: any): Observable<HttpResponse<IDocumentStatus[]>> {
    const options = createRequestOption(req);
    return this.http.get<IDocumentStatus[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getDocumentStatusIdentifier(documentStatus: Pick<IDocumentStatus, 'id'>): number {
    return documentStatus.id;
  }

  compareDocumentStatus(o1: Pick<IDocumentStatus, 'id'> | null, o2: Pick<IDocumentStatus, 'id'> | null): boolean {
    return o1 && o2 ? this.getDocumentStatusIdentifier(o1) === this.getDocumentStatusIdentifier(o2) : o1 === o2;
  }

  addDocumentStatusToCollectionIfMissing<Type extends Pick<IDocumentStatus, 'id'>>(
    documentStatusCollection: Type[],
    ...documentStatusesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const documentStatuses: Type[] = documentStatusesToCheck.filter(isPresent);
    if (documentStatuses.length > 0) {
      const documentStatusCollectionIdentifiers = documentStatusCollection.map(documentStatusItem =>
        this.getDocumentStatusIdentifier(documentStatusItem),
      );
      const documentStatusesToAdd = documentStatuses.filter(documentStatusItem => {
        const documentStatusIdentifier = this.getDocumentStatusIdentifier(documentStatusItem);
        if (documentStatusCollectionIdentifiers.includes(documentStatusIdentifier)) {
          return false;
        }
        documentStatusCollectionIdentifiers.push(documentStatusIdentifier);
        return true;
      });
      return [...documentStatusesToAdd, ...documentStatusCollection];
    }
    return documentStatusCollection;
  }
}
