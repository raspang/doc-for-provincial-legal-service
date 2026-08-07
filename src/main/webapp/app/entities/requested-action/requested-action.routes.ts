import { Routes } from '@angular/router';

import { ASC } from 'app/config/navigation.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';

import RequestedActionResolve from './route/requested-action-routing-resolve.service';

const requestedActionRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/requested-action').then(m => m.RequestedAction),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/requested-action-detail').then(m => m.RequestedActionDetail),
    resolve: {
      requestedAction: RequestedActionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/requested-action-update').then(m => m.RequestedActionUpdate),
    resolve: {
      requestedAction: RequestedActionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/requested-action-update').then(m => m.RequestedActionUpdate),
    resolve: {
      requestedAction: RequestedActionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default requestedActionRoute;
