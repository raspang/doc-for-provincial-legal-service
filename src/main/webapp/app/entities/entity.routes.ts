import { Routes } from '@angular/router';

const routes: Routes = [
  {
    path: 'authority',
    data: { pageTitle: 'Authorities' },
    loadChildren: () => import('./admin/authority/authority.routes'),
  },
  {
    path: 'user-management',
    data: { pageTitle: 'UserManagements' },
    loadChildren: () => import('./admin/user-management/user-management.routes'),
  },
  {
    path: 'received-document',
    data: { pageTitle: 'ReceivedDocuments' },
    loadChildren: () => import('./received-document/received-document.routes'),
  },
  {
    path: 'document-reference',
    data: { pageTitle: 'DocumentReferences' },
    loadChildren: () => import('./document-reference/document-reference.routes'),
  },
  {
    path: 'responsible-person',
    data: { pageTitle: 'ResponsiblePeople' },
    loadChildren: () => import('./responsible-person/responsible-person.routes'),
  },
  {
    path: 'requested-action',
    data: { pageTitle: 'RequestedActions' },
    loadChildren: () => import('./requested-action/requested-action.routes'),
  },
  {
    path: 'type-of-document',
    data: { pageTitle: 'TypeOfDocuments' },
    loadChildren: () => import('./type-of-document/type-of-document.routes'),
  },
  {
    path: 'office',
    data: { pageTitle: 'Offices' },
    loadChildren: () => import('./office/office.routes'),
  },
  {
    path: 'document-history',
    data: { pageTitle: 'DocumentHistories' },
    loadChildren: () => import('./document-history/document-history.routes'),
  },
  {
    path: 'document-status',
    data: { pageTitle: 'DocumentStatuses' },
    loadChildren: () => import('./document-status/document-status.routes'),
  },
  /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
];

export default routes;
