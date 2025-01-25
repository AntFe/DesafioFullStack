import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import MateriaResolve from './route/materia-routing-resolve.service';

const materiaRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/materia.component').then(m => m.MateriaComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/materia-detail.component').then(m => m.MateriaDetailComponent),
    resolve: {
      materia: MateriaResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/materia-update.component').then(m => m.MateriaUpdateComponent),
    resolve: {
      materia: MateriaResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/materia-update.component').then(m => m.MateriaUpdateComponent),
    resolve: {
      materia: MateriaResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default materiaRoute;
