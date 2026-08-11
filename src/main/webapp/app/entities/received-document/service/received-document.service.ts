import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Injectable, computed, inject, signal } from '@angular/core';

import dayjs from 'dayjs/esm';
import { Observable, map } from 'rxjs';

import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { isPresent } from 'app/core/util/operators';
import { IReceivedDocument, NewReceivedDocument } from '../received-document.model';

export type PartialUpdateReceivedDocument = Partial<IReceivedDocument> & Pick<IReceivedDocument, 'id'>;

type RestOf<T extends IReceivedDocument | NewReceivedDocument> = Omit<T, 'date' | 'dateReleased'> & {
  date?: string | null;
  dateReleased?: string | null;
};

export type RestReceivedDocument = RestOf<IReceivedDocument>;

export type NewRestReceivedDocument = RestOf<NewReceivedDocument>;

export type PartialUpdateRestReceivedDocument = RestOf<PartialUpdateReceivedDocument>;

@Injectable()
export class ReceivedDocumentsService {
  readonly receivedDocumentsParams = signal<Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined>(
    undefined,
  );
  readonly receivedDocumentsResource = httpResource<RestReceivedDocument[]>(() => {
    const params = this.receivedDocumentsParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of receivedDocument that have been fetched. It is updated when the receivedDocumentsResource emits a new value.
   * In case of error while fetching the receivedDocuments, the signal is set to an empty array.
   */
  readonly receivedDocuments = computed(() =>
    (this.receivedDocumentsResource.hasValue() ? this.receivedDocumentsResource.value() : []).map(item =>
      this.convertValueFromServer(item),
    ),
  );
  protected readonly applicationConfigService = inject(ApplicationConfigService);
  protected readonly resourceUrl = this.applicationConfigService.getEndpointFor('api/received-documents');

  protected convertValueFromServer(restReceivedDocument: RestReceivedDocument): IReceivedDocument {
    return {
      ...restReceivedDocument,
      date: restReceivedDocument.date ? dayjs(restReceivedDocument.date) : undefined,
      dateReleased: restReceivedDocument.dateReleased ? dayjs(restReceivedDocument.dateReleased) : undefined,
    };
  }
}

@Injectable({ providedIn: 'root' })
export class ReceivedDocumentService extends ReceivedDocumentsService {
  protected readonly http = inject(HttpClient);

  create(receivedDocument: NewReceivedDocument): Observable<IReceivedDocument> {
    const copy = this.convertValueFromClient(receivedDocument);
    return this.http.post<RestReceivedDocument>(this.resourceUrl, copy).pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(receivedDocument: IReceivedDocument): Observable<IReceivedDocument> {
    const copy = this.convertValueFromClient(receivedDocument);
    return this.http
      .put<RestReceivedDocument>(`${this.resourceUrl}/${encodeURIComponent(this.getReceivedDocumentIdentifier(receivedDocument))}`, copy)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(receivedDocument: PartialUpdateReceivedDocument): Observable<IReceivedDocument> {
    const copy = this.convertValueFromClient(receivedDocument);
    return this.http
      .patch<RestReceivedDocument>(`${this.resourceUrl}/${encodeURIComponent(this.getReceivedDocumentIdentifier(receivedDocument))}`, copy)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<IReceivedDocument> {
    return this.http
      .get<RestReceivedDocument>(`${this.resourceUrl}/${encodeURIComponent(id)}`)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<HttpResponse<IReceivedDocument[]>> {
    const options = createRequestOption(req);
    return this.http
      .get<RestReceivedDocument[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => res.clone({ body: this.convertResponseArrayFromServer(res.body!) })));
  }

  delete(id: number): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getReceivedDocumentIdentifier(receivedDocument: Pick<IReceivedDocument, 'id'>): number {
    return receivedDocument.id;
  }

  compareReceivedDocument(o1: Pick<IReceivedDocument, 'id'> | null, o2: Pick<IReceivedDocument, 'id'> | null): boolean {
    return o1 && o2 ? this.getReceivedDocumentIdentifier(o1) === this.getReceivedDocumentIdentifier(o2) : o1 === o2;
  }

  addReceivedDocumentToCollectionIfMissing<Type extends Pick<IReceivedDocument, 'id'>>(
    receivedDocumentCollection: Type[],
    ...receivedDocumentsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const receivedDocuments: Type[] = receivedDocumentsToCheck.filter(isPresent);
    if (receivedDocuments.length > 0) {
      const receivedDocumentCollectionIdentifiers = receivedDocumentCollection.map(receivedDocumentItem =>
        this.getReceivedDocumentIdentifier(receivedDocumentItem),
      );
      const receivedDocumentsToAdd = receivedDocuments.filter(receivedDocumentItem => {
        const receivedDocumentIdentifier = this.getReceivedDocumentIdentifier(receivedDocumentItem);
        if (receivedDocumentCollectionIdentifiers.includes(receivedDocumentIdentifier)) {
          return false;
        }
        receivedDocumentCollectionIdentifiers.push(receivedDocumentIdentifier);
        return true;
      });
      return [...receivedDocumentsToAdd, ...receivedDocumentCollection];
    }
    return receivedDocumentCollection;
  }

  protected convertValueFromClient<T extends IReceivedDocument | NewReceivedDocument | PartialUpdateReceivedDocument>(
    receivedDocument: T,
  ): RestOf<T> {
    return {
      ...receivedDocument,
      date: receivedDocument.date?.toJSON() ?? null,
      dateReleased: receivedDocument.dateReleased?.toJSON() ?? null,
    };
  }

  protected convertResponseFromServer(res: RestReceivedDocument): IReceivedDocument {
    return this.convertValueFromServer(res);
  }

  protected convertResponseArrayFromServer(res: RestReceivedDocument[]): IReceivedDocument[] {
    return res.map(item => this.convertValueFromServer(item));
  }
}
