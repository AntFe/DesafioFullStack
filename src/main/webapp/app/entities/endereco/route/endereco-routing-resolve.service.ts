import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IEndereco } from '../endereco.model';
import { EnderecoService } from '../service/endereco.service';

const enderecoResolve = (route: ActivatedRouteSnapshot): Observable<null | IEndereco> => {
  const id = route.params.id;
  if (id) {
    return inject(EnderecoService)
      .find(id)
      .pipe(
        mergeMap((endereco: HttpResponse<IEndereco>) => {
          if (endereco.body) {
            return of(endereco.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default enderecoResolve;
