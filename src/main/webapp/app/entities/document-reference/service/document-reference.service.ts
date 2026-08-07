import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Injectable, computed, inject, signal } from '@angular/core';

import dayjs from 'dayjs/esm';
import { Observable, map } from 'rxjs';

import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { isPresent } from 'app/core/util/operators';
import { IDocumentReference, NewDocumentReference } from '../document-reference.model';

export type PartialUpdateDocumentReference = Partial<IDocumentReference> & Pick<IDocumentReference, 'id'>;

type RestOf<T extends IDocumentReference | NewDocumentReference> = Omit<T, 'date' | 'dateReleased' | 'submittedToSirKing'> & {
  date?: string | null;
  dateReleased?: string | null;
  submittedToSirKing?: string | null;
};

export type RestDocumentReference = RestOf<IDocumentReference>;

export type NewRestDocumentReference = RestOf<NewDocumentReference>;

export type PartialUpdateRestDocumentReference = RestOf<PartialUpdateDocumentReference>;

@Injectable()
export class DocumentReferencesService {
  readonly documentReferencesParams = signal<
    Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined
  >(undefined);
  readonly documentReferencesResource = httpResource<RestDocumentReference[]>(() => {
    const params = this.documentReferencesParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of documentReference that have been fetched. It is updated when the documentReferencesResource emits a new value.
   * In case of error while fetching the documentReferences, the signal is set to an empty array.
   */
  readonly documentReferences = computed(() =>
    (this.documentReferencesResource.hasValue() ? this.documentReferencesResource.value() : []).map(item =>
      this.convertValueFromServer(item),
    ),
  );
  protected readonly applicationConfigService = inject(ApplicationConfigService);
  protected readonly resourceUrl = this.applicationConfigService.getEndpointFor('api/document-references');

  protected convertValueFromServer(restDocumentReference: RestDocumentReference): IDocumentReference {
    return {
      ...restDocumentReference,
      date: restDocumentReference.date ? dayjs(restDocumentReference.date) : undefined,
      dateReleased: restDocumentReference.dateReleased ? dayjs(restDocumentReference.dateReleased) : undefined,
      submittedToSirKing: restDocumentReference.submittedToSirKing ? dayjs(restDocumentReference.submittedToSirKing) : undefined,
    };
  }
}

@Injectable({ providedIn: 'root' })
export class DocumentReferenceService extends DocumentReferencesService {
  protected readonly http = inject(HttpClient);

  create(documentReference: NewDocumentReference): Observable<IDocumentReference> {
    const copy = this.convertValueFromClient(documentReference);
    return this.http.post<RestDocumentReference>(this.resourceUrl, copy).pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(documentReference: IDocumentReference): Observable<IDocumentReference> {
    const copy = this.convertValueFromClient(documentReference);
    return this.http
      .put<RestDocumentReference>(`${this.resourceUrl}/${encodeURIComponent(this.getDocumentReferenceIdentifier(documentReference))}`, copy)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(documentReference: PartialUpdateDocumentReference): Observable<IDocumentReference> {
    const copy = this.convertValueFromClient(documentReference);
    return this.http
      .patch<RestDocumentReference>(
        `${this.resourceUrl}/${encodeURIComponent(this.getDocumentReferenceIdentifier(documentReference))}`,
        copy,
      )
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<IDocumentReference> {
    return this.http
      .get<RestDocumentReference>(`${this.resourceUrl}/${encodeURIComponent(id)}`)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<HttpResponse<IDocumentReference[]>> {
    const options = createRequestOption(req);
    return this.http
      .get<RestDocumentReference[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => res.clone({ body: this.convertResponseArrayFromServer(res.body!) })));
  }

  delete(id: number): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getDocumentReferenceIdentifier(documentReference: Pick<IDocumentReference, 'id'>): number {
    return documentReference.id;
  }

  compareDocumentReference(o1: Pick<IDocumentReference, 'id'> | null, o2: Pick<IDocumentReference, 'id'> | null): boolean {
    return o1 && o2 ? this.getDocumentReferenceIdentifier(o1) === this.getDocumentReferenceIdentifier(o2) : o1 === o2;
  }

  addDocumentReferenceToCollectionIfMissing<Type extends Pick<IDocumentReference, 'id'>>(
    documentReferenceCollection: Type[],
    ...documentReferencesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const documentReferences: Type[] = documentReferencesToCheck.filter(isPresent);
    if (documentReferences.length > 0) {
      const documentReferenceCollectionIdentifiers = documentReferenceCollection.map(documentReferenceItem =>
        this.getDocumentReferenceIdentifier(documentReferenceItem),
      );
      const documentReferencesToAdd = documentReferences.filter(documentReferenceItem => {
        const documentReferenceIdentifier = this.getDocumentReferenceIdentifier(documentReferenceItem);
        if (documentReferenceCollectionIdentifiers.includes(documentReferenceIdentifier)) {
          return false;
        }
        documentReferenceCollectionIdentifiers.push(documentReferenceIdentifier);
        return true;
      });
      return [...documentReferencesToAdd, ...documentReferenceCollection];
    }
    return documentReferenceCollection;
  }

  protected convertValueFromClient<T extends IDocumentReference | NewDocumentReference | PartialUpdateDocumentReference>(
    documentReference: T,
  ): RestOf<T> {
    return {
      ...documentReference,
      date: documentReference.date?.toJSON() ?? null,
      dateReleased: documentReference.dateReleased?.toJSON() ?? null,
      submittedToSirKing: documentReference.submittedToSirKing?.toJSON() ?? null,
    };
  }

  protected convertResponseFromServer(res: RestDocumentReference): IDocumentReference {
    return this.convertValueFromServer(res);
  }

  protected convertResponseArrayFromServer(res: RestDocumentReference[]): IDocumentReference[] {
    return res.map(item => this.convertValueFromServer(item));
  }
}
