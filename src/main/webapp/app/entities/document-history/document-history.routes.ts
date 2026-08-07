import { Routes } from '@angular/router';

import { ASC } from 'app/config/navigation.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';

import DocumentHistoryResolve from './route/document-history-routing-resolve.service';

const documentHistoryRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/document-history').then(m => m.DocumentHistory),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/document-history-detail').then(m => m.DocumentHistoryDetail),
    resolve: {
      documentHistory: DocumentHistoryResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default documentHistoryRoute;
