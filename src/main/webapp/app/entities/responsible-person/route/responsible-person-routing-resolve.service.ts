import { HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, Router } from '@angular/router';

import { EMPTY, Observable, catchError, of } from 'rxjs';

import { IResponsiblePerson } from '../responsible-person.model';
import { ResponsiblePersonService } from '../service/responsible-person.service';

const responsiblePersonResolve = (route: ActivatedRouteSnapshot): Observable<null | IResponsiblePerson> => {
  const { id } = route.params;
  if (id) {
    const router = inject(Router);
    const service = inject(ResponsiblePersonService);
    return service.find(id).pipe(
      catchError((error: HttpErrorResponse) => {
        if (error.status === 404) {
          router.navigate(['404']);
        } else {
          router.navigate(['error']);
        }
        return EMPTY;
      }),
    );
  }

  return of(null);
};

export default responsiblePersonResolve;
