import { Routes } from '@angular/router';

import { ASC } from 'app/config/navigation.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';

import ResponsiblePersonResolve from './route/responsible-person-routing-resolve.service';

const responsiblePersonRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/responsible-person').then(m => m.ResponsiblePerson),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/responsible-person-detail').then(m => m.ResponsiblePersonDetail),
    resolve: {
      responsiblePerson: ResponsiblePersonResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/responsible-person-update').then(m => m.ResponsiblePersonUpdate),
    resolve: {
      responsiblePerson: ResponsiblePersonResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/responsible-person-update').then(m => m.ResponsiblePersonUpdate),
    resolve: {
      responsiblePerson: ResponsiblePersonResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default responsiblePersonRoute;
