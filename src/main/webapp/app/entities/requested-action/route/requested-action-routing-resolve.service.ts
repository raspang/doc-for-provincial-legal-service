import { HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, Router } from '@angular/router';

import { EMPTY, Observable, catchError, of } from 'rxjs';

import { IRequestedAction } from '../requested-action.model';
import { RequestedActionService } from '../service/requested-action.service';

const requestedActionResolve = (route: ActivatedRouteSnapshot): Observable<null | IRequestedAction> => {
  const { id } = route.params;
  if (id) {
    const router = inject(Router);
    const service = inject(RequestedActionService);
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

export default requestedActionResolve;
