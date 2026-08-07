import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Injectable, computed, inject, signal } from '@angular/core';

import { Observable } from 'rxjs';

import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { isPresent } from 'app/core/util/operators';
import { IOffice, NewOffice } from '../office.model';

export type PartialUpdateOffice = Partial<IOffice> & Pick<IOffice, 'id'>;

@Injectable()
export class OfficesService {
  readonly officesParams = signal<Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined>(
    undefined,
  );
  readonly officesResource = httpResource<IOffice[]>(() => {
    const params = this.officesParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of office that have been fetched. It is updated when the officesResource emits a new value.
   * In case of error while fetching the offices, the signal is set to an empty array.
   */
  readonly offices = computed(() => (this.officesResource.hasValue() ? this.officesResource.value() : []));
  protected readonly applicationConfigService = inject(ApplicationConfigService);
  protected readonly resourceUrl = this.applicationConfigService.getEndpointFor('api/offices');
}

@Injectable({ providedIn: 'root' })
export class OfficeService extends OfficesService {
  protected readonly http = inject(HttpClient);

  create(office: NewOffice): Observable<IOffice> {
    return this.http.post<IOffice>(this.resourceUrl, office);
  }

  update(office: IOffice): Observable<IOffice> {
    return this.http.put<IOffice>(`${this.resourceUrl}/${encodeURIComponent(this.getOfficeIdentifier(office))}`, office);
  }

  partialUpdate(office: PartialUpdateOffice): Observable<IOffice> {
    return this.http.patch<IOffice>(`${this.resourceUrl}/${encodeURIComponent(this.getOfficeIdentifier(office))}`, office);
  }

  find(id: number): Observable<IOffice> {
    return this.http.get<IOffice>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  query(req?: any): Observable<HttpResponse<IOffice[]>> {
    const options = createRequestOption(req);
    return this.http.get<IOffice[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getOfficeIdentifier(office: Pick<IOffice, 'id'>): number {
    return office.id;
  }

  compareOffice(o1: Pick<IOffice, 'id'> | null, o2: Pick<IOffice, 'id'> | null): boolean {
    return o1 && o2 ? this.getOfficeIdentifier(o1) === this.getOfficeIdentifier(o2) : o1 === o2;
  }

  addOfficeToCollectionIfMissing<Type extends Pick<IOffice, 'id'>>(
    officeCollection: Type[],
    ...officesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const offices: Type[] = officesToCheck.filter(isPresent);
    if (offices.length > 0) {
      const officeCollectionIdentifiers = officeCollection.map(officeItem => this.getOfficeIdentifier(officeItem));
      const officesToAdd = offices.filter(officeItem => {
        const officeIdentifier = this.getOfficeIdentifier(officeItem);
        if (officeCollectionIdentifiers.includes(officeIdentifier)) {
          return false;
        }
        officeCollectionIdentifiers.push(officeIdentifier);
        return true;
      });
      return [...officesToAdd, ...officeCollection];
    }
    return officeCollection;
  }
}
