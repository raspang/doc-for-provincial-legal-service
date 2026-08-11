import { HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, Router } from '@angular/router';

import { EMPTY, Observable, catchError, of } from 'rxjs';

import { TransactionTypeService } from '../service/transaction-type.service';
import { ITransactionType } from '../transaction-type.model';

const transactionTypeResolve = (route: ActivatedRouteSnapshot): Observable<null | ITransactionType> => {
  const { id } = route.params;
  if (id) {
    const router = inject(Router);
    const service = inject(TransactionTypeService);
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

export default transactionTypeResolve;
