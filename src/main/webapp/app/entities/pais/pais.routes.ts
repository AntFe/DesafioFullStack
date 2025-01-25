import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import PaisResolve from './route/pais-routing-resolve.service';

const paisRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/pais.component').then(m => m.PaisComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/pais-detail.component').then(m => m.PaisDetailComponent),
    resolve: {
      pais: PaisResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/pais-update.component').then(m => m.PaisUpdateComponent),
    resolve: {
      pais: PaisResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/pais-update.component').then(m => m.PaisUpdateComponent),
    resolve: {
      pais: PaisResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default paisRoute;
