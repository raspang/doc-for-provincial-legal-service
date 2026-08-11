import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Injectable, computed, inject, signal } from '@angular/core';

import { Observable } from 'rxjs';

import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { isPresent } from 'app/core/util/operators';
import { ITransactionType, NewTransactionType } from '../transaction-type.model';

export type PartialUpdateTransactionType = Partial<ITransactionType> & Pick<ITransactionType, 'id'>;

@Injectable()
export class TransactionTypesService {
  readonly transactionTypesParams = signal<Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined>(
    undefined,
  );
  readonly transactionTypesResource = httpResource<ITransactionType[]>(() => {
    const params = this.transactionTypesParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of transactionType that have been fetched. It is updated when the transactionTypesResource emits a new value.
   * In case of error while fetching the transactionTypes, the signal is set to an empty array.
   */
  readonly transactionTypes = computed(() => (this.transactionTypesResource.hasValue() ? this.transactionTypesResource.value() : []));
  protected readonly applicationConfigService = inject(ApplicationConfigService);
  protected readonly resourceUrl = this.applicationConfigService.getEndpointFor('api/transaction-types');
}

@Injectable({ providedIn: 'root' })
export class TransactionTypeService extends TransactionTypesService {
  protected readonly http = inject(HttpClient);

  create(transactionType: NewTransactionType): Observable<ITransactionType> {
    return this.http.post<ITransactionType>(this.resourceUrl, transactionType);
  }

  update(transactionType: ITransactionType): Observable<ITransactionType> {
    return this.http.put<ITransactionType>(
      `${this.resourceUrl}/${encodeURIComponent(this.getTransactionTypeIdentifier(transactionType))}`,
      transactionType,
    );
  }

  partialUpdate(transactionType: PartialUpdateTransactionType): Observable<ITransactionType> {
    return this.http.patch<ITransactionType>(
      `${this.resourceUrl}/${encodeURIComponent(this.getTransactionTypeIdentifier(transactionType))}`,
      transactionType,
    );
  }

  find(id: number): Observable<ITransactionType> {
    return this.http.get<ITransactionType>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  query(req?: any): Observable<HttpResponse<ITransactionType[]>> {
    const options = createRequestOption(req);
    return this.http.get<ITransactionType[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getTransactionTypeIdentifier(transactionType: Pick<ITransactionType, 'id'>): number {
    return transactionType.id;
  }

  compareTransactionType(o1: Pick<ITransactionType, 'id'> | null, o2: Pick<ITransactionType, 'id'> | null): boolean {
    return o1 && o2 ? this.getTransactionTypeIdentifier(o1) === this.getTransactionTypeIdentifier(o2) : o1 === o2;
  }

  addTransactionTypeToCollectionIfMissing<Type extends Pick<ITransactionType, 'id'>>(
    transactionTypeCollection: Type[],
    ...transactionTypesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const transactionTypes: Type[] = transactionTypesToCheck.filter(isPresent);
    if (transactionTypes.length > 0) {
      const transactionTypeCollectionIdentifiers = transactionTypeCollection.map(transactionTypeItem =>
        this.getTransactionTypeIdentifier(transactionTypeItem),
      );
      const transactionTypesToAdd = transactionTypes.filter(transactionTypeItem => {
        const transactionTypeIdentifier = this.getTransactionTypeIdentifier(transactionTypeItem);
        if (transactionTypeCollectionIdentifiers.includes(transactionTypeIdentifier)) {
          return false;
        }
        transactionTypeCollectionIdentifiers.push(transactionTypeIdentifier);
        return true;
      });
      return [...transactionTypesToAdd, ...transactionTypeCollection];
    }
    return transactionTypeCollection;
  }
}
