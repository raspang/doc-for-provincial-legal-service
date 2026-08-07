import { Routes } from '@angular/router';

import { ASC } from 'app/config/navigation.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';

import ReceivedDocumentResolve from './route/received-document-routing-resolve.service';

const receivedDocumentRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/received-document').then(m => m.ReceivedDocument),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/received-document-detail').then(m => m.ReceivedDocumentDetail),
    resolve: {
      receivedDocument: ReceivedDocumentResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/received-document-update').then(m => m.ReceivedDocumentUpdate),
    resolve: {
      receivedDocument: ReceivedDocumentResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/received-document-update').then(m => m.ReceivedDocumentUpdate),
    resolve: {
      receivedDocument: ReceivedDocumentResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default receivedDocumentRoute;
