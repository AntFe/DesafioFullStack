import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import ServicoResolve from './route/servico-routing-resolve.service';

const servicoRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/servico.component').then(m => m.ServicoComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/servico-detail.component').then(m => m.ServicoDetailComponent),
    resolve: {
      servico: ServicoResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/servico-update.component').then(m => m.ServicoUpdateComponent),
    resolve: {
      servico: ServicoResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/servico-update.component').then(m => m.ServicoUpdateComponent),
    resolve: {
      servico: ServicoResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default servicoRoute;
