import { Routes } from '@angular/router';

import { ASC } from 'app/config/navigation.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';

import DocumentStatusResolve from './route/document-status-routing-resolve.service';

const documentStatusRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/document-status').then(m => m.DocumentStatus),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/document-status-detail').then(m => m.DocumentStatusDetail),
    resolve: {
      documentStatus: DocumentStatusResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/document-status-update').then(m => m.DocumentStatusUpdate),
    resolve: {
      documentStatus: DocumentStatusResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/document-status-update').then(m => m.DocumentStatusUpdate),
    resolve: {
      documentStatus: DocumentStatusResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default documentStatusRoute;
