import { Routes } from '@angular/router';

import { ASC } from 'app/config/navigation.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';

import TypeOfDocumentResolve from './route/type-of-document-routing-resolve.service';

const typeOfDocumentRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/type-of-document').then(m => m.TypeOfDocument),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/type-of-document-detail').then(m => m.TypeOfDocumentDetail),
    resolve: {
      typeOfDocument: TypeOfDocumentResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/type-of-document-update').then(m => m.TypeOfDocumentUpdate),
    resolve: {
      typeOfDocument: TypeOfDocumentResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/type-of-document-update').then(m => m.TypeOfDocumentUpdate),
    resolve: {
      typeOfDocument: TypeOfDocumentResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default typeOfDocumentRoute;
