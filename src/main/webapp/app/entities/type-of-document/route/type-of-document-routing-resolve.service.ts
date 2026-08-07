import { HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, Router } from '@angular/router';

import { EMPTY, Observable, catchError, of } from 'rxjs';

import { TypeOfDocumentService } from '../service/type-of-document.service';
import { ITypeOfDocument } from '../type-of-document.model';

const typeOfDocumentResolve = (route: ActivatedRouteSnapshot): Observable<null | ITypeOfDocument> => {
  const { id } = route.params;
  if (id) {
    const router = inject(Router);
    const service = inject(TypeOfDocumentService);
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

export default typeOfDocumentResolve;
