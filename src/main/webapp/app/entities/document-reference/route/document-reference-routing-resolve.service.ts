import { HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, Router } from '@angular/router';

import { EMPTY, Observable, catchError, of } from 'rxjs';

import { IDocumentReference } from '../document-reference.model';
import { DocumentReferenceService } from '../service/document-reference.service';

const documentReferenceResolve = (route: ActivatedRouteSnapshot): Observable<null | IDocumentReference> => {
  const { id } = route.params;
  if (id) {
    const router = inject(Router);
    const service = inject(DocumentReferenceService);
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

export default documentReferenceResolve;
