import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import AulaResolve from './route/aula-routing-resolve.service';

const aulaRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/aula.component').then(m => m.AulaComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/aula-detail.component').then(m => m.AulaDetailComponent),
    resolve: {
      aula: AulaResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/aula-update.component').then(m => m.AulaUpdateComponent),
    resolve: {
      aula: AulaResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/aula-update.component').then(m => m.AulaUpdateComponent),
    resolve: {
      aula: AulaResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default aulaRoute;
