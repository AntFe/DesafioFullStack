import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IPais } from '../pais.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../pais.test-samples';

import { PaisService } from './pais.service';

const requireRestSample: IPais = {
  ...sampleWithRequiredData,
};

describe('Pais Service', () => {
  let service: PaisService;
  let httpMock: HttpTestingController;
  let expectedResult: IPais | IPais[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(PaisService);
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

    it('should create a Pais', () => {
      const pais = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(pais).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Pais', () => {
      const pais = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(pais).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Pais', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Pais', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Pais', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addPaisToCollectionIfMissing', () => {
      it('should add a Pais to an empty array', () => {
        const pais: IPais = sampleWithRequiredData;
        expectedResult = service.addPaisToCollectionIfMissing([], pais);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(pais);
      });

      it('should not add a Pais to an array that contains it', () => {
        const pais: IPais = sampleWithRequiredData;
        const paisCollection: IPais[] = [
          {
            ...pais,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addPaisToCollectionIfMissing(paisCollection, pais);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Pais to an array that doesn't contain it", () => {
        const pais: IPais = sampleWithRequiredData;
        const paisCollection: IPais[] = [sampleWithPartialData];
        expectedResult = service.addPaisToCollectionIfMissing(paisCollection, pais);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(pais);
      });

      it('should add only unique Pais to an array', () => {
        const paisArray: IPais[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const paisCollection: IPais[] = [sampleWithRequiredData];
        expectedResult = service.addPaisToCollectionIfMissing(paisCollection, ...paisArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const pais: IPais = sampleWithRequiredData;
        const pais2: IPais = sampleWithPartialData;
        expectedResult = service.addPaisToCollectionIfMissing([], pais, pais2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(pais);
        expect(expectedResult).toContain(pais2);
      });

      it('should accept null and undefined values', () => {
        const pais: IPais = sampleWithRequiredData;
        expectedResult = service.addPaisToCollectionIfMissing([], null, pais, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(pais);
      });

      it('should return initial array if no Pais is added', () => {
        const paisCollection: IPais[] = [sampleWithRequiredData];
        expectedResult = service.addPaisToCollectionIfMissing(paisCollection, undefined, null);
        expect(expectedResult).toEqual(paisCollection);
      });
    });

    describe('comparePais', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.comparePais(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 17868 };
        const entity2 = null;

        const compareResult1 = service.comparePais(entity1, entity2);
        const compareResult2 = service.comparePais(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 17868 };
        const entity2 = { id: 16774 };

        const compareResult1 = service.comparePais(entity1, entity2);
        const compareResult2 = service.comparePais(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 17868 };
        const entity2 = { id: 17868 };

        const compareResult1 = service.comparePais(entity1, entity2);
        const compareResult2 = service.comparePais(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
