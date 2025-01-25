import { Routes } from '@angular/router';

const routes: Routes = [
  {
    path: 'authority',
    data: { pageTitle: 'plataformaAulasApp.adminAuthority.home.title' },
    loadChildren: () => import('./admin/authority/authority.routes'),
  },
  {
    path: 'local',
    data: { pageTitle: 'plataformaAulasApp.local.home.title' },
    loadChildren: () => import('./local/local.routes'),
  },
  {
    path: 'pais',
    data: { pageTitle: 'plataformaAulasApp.pais.home.title' },
    loadChildren: () => import('./pais/pais.routes'),
  },
  {
    path: 'endereco',
    data: { pageTitle: 'plataformaAulasApp.endereco.home.title' },
    loadChildren: () => import('./endereco/endereco.routes'),
  },
  {
    path: 'curso',
    data: { pageTitle: 'plataformaAulasApp.curso.home.title' },
    loadChildren: () => import('./curso/curso.routes'),
  },
  {
    path: 'aula',
    data: { pageTitle: 'plataformaAulasApp.aula.home.title' },
    loadChildren: () => import('./aula/aula.routes'),
  },
  {
    path: 'aluno',
    data: { pageTitle: 'plataformaAulasApp.aluno.home.title' },
    loadChildren: () => import('./aluno/aluno.routes'),
  },
  {
    path: 'professor',
    data: { pageTitle: 'plataformaAulasApp.professor.home.title' },
    loadChildren: () => import('./professor/professor.routes'),
  },
  {
    path: 'materia',
    data: { pageTitle: 'plataformaAulasApp.materia.home.title' },
    loadChildren: () => import('./materia/materia.routes'),
  },
  {
    path: 'servico',
    data: { pageTitle: 'plataformaAulasApp.servico.home.title' },
    loadChildren: () => import('./servico/servico.routes'),
  },
  /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
];

export default routes;
