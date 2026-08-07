import { Routes } from '@angular/router';

import { ASC } from 'app/config/navigation.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';

import DocumentReferenceResolve from './route/document-reference-routing-resolve.service';

const documentReferenceRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/document-reference').then(m => m.DocumentReference),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/document-reference-detail').then(m => m.DocumentReferenceDetail),
    resolve: {
      documentReference: DocumentReferenceResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/document-reference-update').then(m => m.DocumentReferenceUpdate),
    resolve: {
      documentReference: DocumentReferenceResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/document-reference-update').then(m => m.DocumentReferenceUpdate),
    resolve: {
      documentReference: DocumentReferenceResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default documentReferenceRoute;
