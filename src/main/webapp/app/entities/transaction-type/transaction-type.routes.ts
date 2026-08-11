import { Routes } from '@angular/router';

import { ASC } from 'app/config/navigation.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';

import TransactionTypeResolve from './route/transaction-type-routing-resolve.service';

const transactionTypeRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/transaction-type').then(m => m.TransactionType),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/transaction-type-detail').then(m => m.TransactionTypeDetail),
    resolve: {
      transactionType: TransactionTypeResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/transaction-type-update').then(m => m.TransactionTypeUpdate),
    resolve: {
      transactionType: TransactionTypeResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/transaction-type-update').then(m => m.TransactionTypeUpdate),
    resolve: {
      transactionType: TransactionTypeResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default transactionTypeRoute;
