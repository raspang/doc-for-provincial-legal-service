import { HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, Router } from '@angular/router';

import { EMPTY, Observable, catchError, of } from 'rxjs';

import { IDocumentHistory } from '../document-history.model';
import { DocumentHistoryService } from '../service/document-history.service';

const documentHistoryResolve = (route: ActivatedRouteSnapshot): Observable<null | IDocumentHistory> => {
  const { id } = route.params;
  if (id) {
    const router = inject(Router);
    const service = inject(DocumentHistoryService);
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

export default documentHistoryResolve;
