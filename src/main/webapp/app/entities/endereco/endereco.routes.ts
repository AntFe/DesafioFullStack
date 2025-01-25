import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import EnderecoResolve from './route/endereco-routing-resolve.service';

const enderecoRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/endereco.component').then(m => m.EnderecoComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/endereco-detail.component').then(m => m.EnderecoDetailComponent),
    resolve: {
      endereco: EnderecoResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/endereco-update.component').then(m => m.EnderecoUpdateComponent),
    resolve: {
      endereco: EnderecoResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/endereco-update.component').then(m => m.EnderecoUpdateComponent),
    resolve: {
      endereco: EnderecoResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default enderecoRoute;
