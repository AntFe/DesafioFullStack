import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import ProfessorResolve from './route/professor-routing-resolve.service';

const professorRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/professor.component').then(m => m.ProfessorComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/professor-detail.component').then(m => m.ProfessorDetailComponent),
    resolve: {
      professor: ProfessorResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/professor-update.component').then(m => m.ProfessorUpdateComponent),
    resolve: {
      professor: ProfessorResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/professor-update.component').then(m => m.ProfessorUpdateComponent),
    resolve: {
      professor: ProfessorResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default professorRoute;
