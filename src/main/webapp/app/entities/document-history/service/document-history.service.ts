import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Injectable, computed, inject, signal } from '@angular/core';

import dayjs from 'dayjs/esm';
import { Observable, map } from 'rxjs';

import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { isPresent } from 'app/core/util/operators';
import { IDocumentHistory } from '../document-history.model';

type RestOf<T extends IDocumentHistory> = Omit<T, 'timestamp'> & {
  timestamp?: string | null;
};

export type RestDocumentHistory = RestOf<IDocumentHistory>;

@Injectable()
export class DocumentHistoriesService {
  readonly documentHistoriesParams = signal<Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined>(
    undefined,
  );
  readonly documentHistoriesResource = httpResource<RestDocumentHistory[]>(() => {
    const params = this.documentHistoriesParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of documentHistory that have been fetched. It is updated when the documentHistoriesResource emits a new value.
   * In case of error while fetching the documentHistories, the signal is set to an empty array.
   */
  readonly documentHistories = computed(() =>
    (this.documentHistoriesResource.hasValue() ? this.documentHistoriesResource.value() : []).map(item =>
      this.convertValueFromServer(item),
    ),
  );
  protected readonly applicationConfigService = inject(ApplicationConfigService);
  protected readonly resourceUrl = this.applicationConfigService.getEndpointFor('api/document-histories');

  protected convertValueFromServer(restDocumentHistory: RestDocumentHistory): IDocumentHistory {
    return {
      ...restDocumentHistory,
      timestamp: restDocumentHistory.timestamp ? dayjs(restDocumentHistory.timestamp) : undefined,
    };
  }
}

@Injectable({ providedIn: 'root' })
export class DocumentHistoryService extends DocumentHistoriesService {
  protected readonly http = inject(HttpClient);

  find(id: number): Observable<IDocumentHistory> {
    return this.http
      .get<RestDocumentHistory>(`${this.resourceUrl}/${encodeURIComponent(id)}`)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<HttpResponse<IDocumentHistory[]>> {
    const options = createRequestOption(req);
    return this.http
      .get<RestDocumentHistory[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => res.clone({ body: this.convertResponseArrayFromServer(res.body!) })));
  }

  getDocumentHistoryIdentifier(documentHistory: Pick<IDocumentHistory, 'id'>): number {
    return documentHistory.id;
  }

  compareDocumentHistory(o1: Pick<IDocumentHistory, 'id'> | null, o2: Pick<IDocumentHistory, 'id'> | null): boolean {
    return o1 && o2 ? this.getDocumentHistoryIdentifier(o1) === this.getDocumentHistoryIdentifier(o2) : o1 === o2;
  }

  addDocumentHistoryToCollectionIfMissing<Type extends Pick<IDocumentHistory, 'id'>>(
    documentHistoryCollection: Type[],
    ...documentHistoriesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const documentHistories: Type[] = documentHistoriesToCheck.filter(isPresent);
    if (documentHistories.length > 0) {
      const documentHistoryCollectionIdentifiers = documentHistoryCollection.map(documentHistoryItem =>
        this.getDocumentHistoryIdentifier(documentHistoryItem),
      );
      const documentHistoriesToAdd = documentHistories.filter(documentHistoryItem => {
        const documentHistoryIdentifier = this.getDocumentHistoryIdentifier(documentHistoryItem);
        if (documentHistoryCollectionIdentifiers.includes(documentHistoryIdentifier)) {
          return false;
        }
        documentHistoryCollectionIdentifiers.push(documentHistoryIdentifier);
        return true;
      });
      return [...documentHistoriesToAdd, ...documentHistoryCollection];
    }
    return documentHistoryCollection;
  }

  protected convertValueFromClient<T extends IDocumentHistory>(documentHistory: T): RestOf<T> {
    return {
      ...documentHistory,
      timestamp: documentHistory.timestamp?.toJSON() ?? null,
    };
  }

  protected convertResponseFromServer(res: RestDocumentHistory): IDocumentHistory {
    return this.convertValueFromServer(res);
  }

  protected convertResponseArrayFromServer(res: RestDocumentHistory[]): IDocumentHistory[] {
    return res.map(item => this.convertValueFromServer(item));
  }
}
