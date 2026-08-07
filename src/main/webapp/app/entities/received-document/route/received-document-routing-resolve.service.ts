import { HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, Router } from '@angular/router';

import { EMPTY, Observable, catchError, of } from 'rxjs';

import { IReceivedDocument } from '../received-document.model';
import { ReceivedDocumentService } from '../service/received-document.service';

const receivedDocumentResolve = (route: ActivatedRouteSnapshot): Observable<null | IReceivedDocument> => {
  const { id } = route.params;
  if (id) {
    const router = inject(Router);
    const service = inject(ReceivedDocumentService);
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

export default receivedDocumentResolve;
