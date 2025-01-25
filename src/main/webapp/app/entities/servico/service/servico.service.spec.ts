import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IServico } from '../servico.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../servico.test-samples';

import { RestServico, ServicoService } from './servico.service';

const requireRestSample: RestServico = {
  ...sampleWithRequiredData,
  startDate: sampleWithRequiredData.startDate?.toJSON(),
  endDate: sampleWithRequiredData.endDate?.toJSON(),
};

describe('Servico Service', () => {
  let service: ServicoService;
  let httpMock: HttpTestingController;
  let expectedResult: IServico | IServico[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(ServicoService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a Servico', () => {
      const servico = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(servico).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Servico', () => {
      const servico = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(servico).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Servico', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Servico', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Servico', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addServicoToCollectionIfMissing', () => {
      it('should add a Servico to an empty array', () => {
        const servico: IServico = sampleWithRequiredData;
        expectedResult = service.addServicoToCollectionIfMissing([], servico);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(servico);
      });

      it('should not add a Servico to an array that contains it', () => {
        const servico: IServico = sampleWithRequiredData;
        const servicoCollection: IServico[] = [
          {
            ...servico,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addServicoToCollectionIfMissing(servicoCollection, servico);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Servico to an array that doesn't contain it", () => {
        const servico: IServico = sampleWithRequiredData;
        const servicoCollection: IServico[] = [sampleWithPartialData];
        expectedResult = service.addServicoToCollectionIfMissing(servicoCollection, servico);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(servico);
      });

      it('should add only unique Servico to an array', () => {
        const servicoArray: IServico[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const servicoCollection: IServico[] = [sampleWithRequiredData];
        expectedResult = service.addServicoToCollectionIfMissing(servicoCollection, ...servicoArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const servico: IServico = sampleWithRequiredData;
        const servico2: IServico = sampleWithPartialData;
        expectedResult = service.addServicoToCollectionIfMissing([], servico, servico2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(servico);
        expect(expectedResult).toContain(servico2);
      });

      it('should accept null and undefined values', () => {
        const servico: IServico = sampleWithRequiredData;
        expectedResult = service.addServicoToCollectionIfMissing([], null, servico, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(servico);
      });

      it('should return initial array if no Servico is added', () => {
        const servicoCollection: IServico[] = [sampleWithRequiredData];
        expectedResult = service.addServicoToCollectionIfMissing(servicoCollection, undefined, null);
        expect(expectedResult).toEqual(servicoCollection);
      });
    });

    describe('compareServico', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareServico(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 7799 };
        const entity2 = null;

        const compareResult1 = service.compareServico(entity1, entity2);
        const compareResult2 = service.compareServico(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 7799 };
        const entity2 = { id: 14176 };

        const compareResult1 = service.compareServico(entity1, entity2);
        const compareResult2 = service.compareServico(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 7799 };
        const entity2 = { id: 7799 };

        const compareResult1 = service.compareServico(entity1, entity2);
        const compareResult2 = service.compareServico(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
