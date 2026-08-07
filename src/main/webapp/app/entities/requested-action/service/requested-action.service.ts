import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Injectable, computed, inject, signal } from '@angular/core';

import { Observable } from 'rxjs';

import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { isPresent } from 'app/core/util/operators';
import { IRequestedAction, NewRequestedAction } from '../requested-action.model';

export type PartialUpdateRequestedAction = Partial<IRequestedAction> & Pick<IRequestedAction, 'id'>;

@Injectable()
export class RequestedActionsService {
  readonly requestedActionsParams = signal<Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined>(
    undefined,
  );
  readonly requestedActionsResource = httpResource<IRequestedAction[]>(() => {
    const params = this.requestedActionsParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of requestedAction that have been fetched. It is updated when the requestedActionsResource emits a new value.
   * In case of error while fetching the requestedActions, the signal is set to an empty array.
   */
  readonly requestedActions = computed(() => (this.requestedActionsResource.hasValue() ? this.requestedActionsResource.value() : []));
  protected readonly applicationConfigService = inject(ApplicationConfigService);
  protected readonly resourceUrl = this.applicationConfigService.getEndpointFor('api/requested-actions');
}

@Injectable({ providedIn: 'root' })
export class RequestedActionService extends RequestedActionsService {
  protected readonly http = inject(HttpClient);

  create(requestedAction: NewRequestedAction): Observable<IRequestedAction> {
    return this.http.post<IRequestedAction>(this.resourceUrl, requestedAction);
  }

  update(requestedAction: IRequestedAction): Observable<IRequestedAction> {
    return this.http.put<IRequestedAction>(
      `${this.resourceUrl}/${encodeURIComponent(this.getRequestedActionIdentifier(requestedAction))}`,
      requestedAction,
    );
  }

  partialUpdate(requestedAction: PartialUpdateRequestedAction): Observable<IRequestedAction> {
    return this.http.patch<IRequestedAction>(
      `${this.resourceUrl}/${encodeURIComponent(this.getRequestedActionIdentifier(requestedAction))}`,
      requestedAction,
    );
  }

  find(id: number): Observable<IRequestedAction> {
    return this.http.get<IRequestedAction>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  query(req?: any): Observable<HttpResponse<IRequestedAction[]>> {
    const options = createRequestOption(req);
    return this.http.get<IRequestedAction[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getRequestedActionIdentifier(requestedAction: Pick<IRequestedAction, 'id'>): number {
    return requestedAction.id;
  }

  compareRequestedAction(o1: Pick<IRequestedAction, 'id'> | null, o2: Pick<IRequestedAction, 'id'> | null): boolean {
    return o1 && o2 ? this.getRequestedActionIdentifier(o1) === this.getRequestedActionIdentifier(o2) : o1 === o2;
  }

  addRequestedActionToCollectionIfMissing<Type extends Pick<IRequestedAction, 'id'>>(
    requestedActionCollection: Type[],
    ...requestedActionsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const requestedActions: Type[] = requestedActionsToCheck.filter(isPresent);
    if (requestedActions.length > 0) {
      const requestedActionCollectionIdentifiers = requestedActionCollection.map(requestedActionItem =>
        this.getRequestedActionIdentifier(requestedActionItem),
      );
      const requestedActionsToAdd = requestedActions.filter(requestedActionItem => {
        const requestedActionIdentifier = this.getRequestedActionIdentifier(requestedActionItem);
        if (requestedActionCollectionIdentifiers.includes(requestedActionIdentifier)) {
          return false;
        }
        requestedActionCollectionIdentifiers.push(requestedActionIdentifier);
        return true;
      });
      return [...requestedActionsToAdd, ...requestedActionCollection];
    }
    return requestedActionCollection;
  }
}
