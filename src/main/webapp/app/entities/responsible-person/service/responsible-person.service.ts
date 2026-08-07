import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Injectable, computed, inject, signal } from '@angular/core';

import { Observable } from 'rxjs';

import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { isPresent } from 'app/core/util/operators';
import { IResponsiblePerson, NewResponsiblePerson } from '../responsible-person.model';

export type PartialUpdateResponsiblePerson = Partial<IResponsiblePerson> & Pick<IResponsiblePerson, 'id'>;

@Injectable()
export class ResponsiblePeopleService {
  readonly responsiblePeopleParams = signal<Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined>(
    undefined,
  );
  readonly responsiblePeopleResource = httpResource<IResponsiblePerson[]>(() => {
    const params = this.responsiblePeopleParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of responsiblePerson that have been fetched. It is updated when the responsiblePeopleResource emits a new value.
   * In case of error while fetching the responsiblePeople, the signal is set to an empty array.
   */
  readonly responsiblePeople = computed(() => (this.responsiblePeopleResource.hasValue() ? this.responsiblePeopleResource.value() : []));
  protected readonly applicationConfigService = inject(ApplicationConfigService);
  protected readonly resourceUrl = this.applicationConfigService.getEndpointFor('api/responsible-people');
}

@Injectable({ providedIn: 'root' })
export class ResponsiblePersonService extends ResponsiblePeopleService {
  protected readonly http = inject(HttpClient);

  create(responsiblePerson: NewResponsiblePerson): Observable<IResponsiblePerson> {
    return this.http.post<IResponsiblePerson>(this.resourceUrl, responsiblePerson);
  }

  update(responsiblePerson: IResponsiblePerson): Observable<IResponsiblePerson> {
    return this.http.put<IResponsiblePerson>(
      `${this.resourceUrl}/${encodeURIComponent(this.getResponsiblePersonIdentifier(responsiblePerson))}`,
      responsiblePerson,
    );
  }

  partialUpdate(responsiblePerson: PartialUpdateResponsiblePerson): Observable<IResponsiblePerson> {
    return this.http.patch<IResponsiblePerson>(
      `${this.resourceUrl}/${encodeURIComponent(this.getResponsiblePersonIdentifier(responsiblePerson))}`,
      responsiblePerson,
    );
  }

  find(id: number): Observable<IResponsiblePerson> {
    return this.http.get<IResponsiblePerson>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  query(req?: any): Observable<HttpResponse<IResponsiblePerson[]>> {
    const options = createRequestOption(req);
    return this.http.get<IResponsiblePerson[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getResponsiblePersonIdentifier(responsiblePerson: Pick<IResponsiblePerson, 'id'>): number {
    return responsiblePerson.id;
  }

  compareResponsiblePerson(o1: Pick<IResponsiblePerson, 'id'> | null, o2: Pick<IResponsiblePerson, 'id'> | null): boolean {
    return o1 && o2 ? this.getResponsiblePersonIdentifier(o1) === this.getResponsiblePersonIdentifier(o2) : o1 === o2;
  }

  addResponsiblePersonToCollectionIfMissing<Type extends Pick<IResponsiblePerson, 'id'>>(
    responsiblePersonCollection: Type[],
    ...responsiblePeopleToCheck: (Type | null | undefined)[]
  ): Type[] {
    const responsiblePeople: Type[] = responsiblePeopleToCheck.filter(isPresent);
    if (responsiblePeople.length > 0) {
      const responsiblePersonCollectionIdentifiers = responsiblePersonCollection.map(responsiblePersonItem =>
        this.getResponsiblePersonIdentifier(responsiblePersonItem),
      );
      const responsiblePeopleToAdd = responsiblePeople.filter(responsiblePersonItem => {
        const responsiblePersonIdentifier = this.getResponsiblePersonIdentifier(responsiblePersonItem);
        if (responsiblePersonCollectionIdentifiers.includes(responsiblePersonIdentifier)) {
          return false;
        }
        responsiblePersonCollectionIdentifiers.push(responsiblePersonIdentifier);
        return true;
      });
      return [...responsiblePeopleToAdd, ...responsiblePersonCollection];
    }
    return responsiblePersonCollection;
  }
}
