import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Injectable, computed, inject, signal } from '@angular/core';

import { Observable } from 'rxjs';

import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { isPresent } from 'app/core/util/operators';
import { ITypeOfDocument, NewTypeOfDocument } from '../type-of-document.model';

export type PartialUpdateTypeOfDocument = Partial<ITypeOfDocument> & Pick<ITypeOfDocument, 'id'>;

@Injectable()
export class TypeOfDocumentsService {
  readonly typeOfDocumentsParams = signal<Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined>(
    undefined,
  );
  readonly typeOfDocumentsResource = httpResource<ITypeOfDocument[]>(() => {
    const params = this.typeOfDocumentsParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of typeOfDocument that have been fetched. It is updated when the typeOfDocumentsResource emits a new value.
   * In case of error while fetching the typeOfDocuments, the signal is set to an empty array.
   */
  readonly typeOfDocuments = computed(() => (this.typeOfDocumentsResource.hasValue() ? this.typeOfDocumentsResource.value() : []));
  protected readonly applicationConfigService = inject(ApplicationConfigService);
  protected readonly resourceUrl = this.applicationConfigService.getEndpointFor('api/type-of-documents');
}

@Injectable({ providedIn: 'root' })
export class TypeOfDocumentService extends TypeOfDocumentsService {
  protected readonly http = inject(HttpClient);

  create(typeOfDocument: NewTypeOfDocument): Observable<ITypeOfDocument> {
    return this.http.post<ITypeOfDocument>(this.resourceUrl, typeOfDocument);
  }

  update(typeOfDocument: ITypeOfDocument): Observable<ITypeOfDocument> {
    return this.http.put<ITypeOfDocument>(
      `${this.resourceUrl}/${encodeURIComponent(this.getTypeOfDocumentIdentifier(typeOfDocument))}`,
      typeOfDocument,
    );
  }

  partialUpdate(typeOfDocument: PartialUpdateTypeOfDocument): Observable<ITypeOfDocument> {
    return this.http.patch<ITypeOfDocument>(
      `${this.resourceUrl}/${encodeURIComponent(this.getTypeOfDocumentIdentifier(typeOfDocument))}`,
      typeOfDocument,
    );
  }

  find(id: number): Observable<ITypeOfDocument> {
    return this.http.get<ITypeOfDocument>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  query(req?: any): Observable<HttpResponse<ITypeOfDocument[]>> {
    const options = createRequestOption(req);
    return this.http.get<ITypeOfDocument[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getTypeOfDocumentIdentifier(typeOfDocument: Pick<ITypeOfDocument, 'id'>): number {
    return typeOfDocument.id;
  }

  compareTypeOfDocument(o1: Pick<ITypeOfDocument, 'id'> | null, o2: Pick<ITypeOfDocument, 'id'> | null): boolean {
    return o1 && o2 ? this.getTypeOfDocumentIdentifier(o1) === this.getTypeOfDocumentIdentifier(o2) : o1 === o2;
  }

  addTypeOfDocumentToCollectionIfMissing<Type extends Pick<ITypeOfDocument, 'id'>>(
    typeOfDocumentCollection: Type[],
    ...typeOfDocumentsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const typeOfDocuments: Type[] = typeOfDocumentsToCheck.filter(isPresent);
    if (typeOfDocuments.length > 0) {
      const typeOfDocumentCollectionIdentifiers = typeOfDocumentCollection.map(typeOfDocumentItem =>
        this.getTypeOfDocumentIdentifier(typeOfDocumentItem),
      );
      const typeOfDocumentsToAdd = typeOfDocuments.filter(typeOfDocumentItem => {
        const typeOfDocumentIdentifier = this.getTypeOfDocumentIdentifier(typeOfDocumentItem);
        if (typeOfDocumentCollectionIdentifiers.includes(typeOfDocumentIdentifier)) {
          return false;
        }
        typeOfDocumentCollectionIdentifiers.push(typeOfDocumentIdentifier);
        return true;
      });
      return [...typeOfDocumentsToAdd, ...typeOfDocumentCollection];
    }
    return typeOfDocumentCollection;
  }
}
