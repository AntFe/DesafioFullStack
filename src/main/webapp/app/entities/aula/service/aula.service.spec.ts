import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IAula } from '../aula.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../aula.test-samples';

import { AulaService } from './aula.service';

const requireRestSample: IAula = {
  ...sampleWithRequiredData,
};

describe('Aula Service', () => {
  let service: AulaService;
  let httpMock: HttpTestingController;
  let expectedResult: IAula | IAula[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(AulaService);
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

    it('should create a Aula', () => {
      const aula = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(aula).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Aula', () => {
      const aula = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(aula).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Aula', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Aula', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Aula', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addAulaToCollectionIfMissing', () => {
      it('should add a Aula to an empty array', () => {
        const aula: IAula = sampleWithRequiredData;
        expectedResult = service.addAulaToCollectionIfMissing([], aula);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(aula);
      });

      it('should not add a Aula to an array that contains it', () => {
        const aula: IAula = sampleWithRequiredData;
        const aulaCollection: IAula[] = [
          {
            ...aula,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addAulaToCollectionIfMissing(aulaCollection, aula);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Aula to an array that doesn't contain it", () => {
        const aula: IAula = sampleWithRequiredData;
        const aulaCollection: IAula[] = [sampleWithPartialData];
        expectedResult = service.addAulaToCollectionIfMissing(aulaCollection, aula);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(aula);
      });

      it('should add only unique Aula to an array', () => {
        const aulaArray: IAula[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const aulaCollection: IAula[] = [sampleWithRequiredData];
        expectedResult = service.addAulaToCollectionIfMissing(aulaCollection, ...aulaArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const aula: IAula = sampleWithRequiredData;
        const aula2: IAula = sampleWithPartialData;
        expectedResult = service.addAulaToCollectionIfMissing([], aula, aula2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(aula);
        expect(expectedResult).toContain(aula2);
      });

      it('should accept null and undefined values', () => {
        const aula: IAula = sampleWithRequiredData;
        expectedResult = service.addAulaToCollectionIfMissing([], null, aula, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(aula);
      });

      it('should return initial array if no Aula is added', () => {
        const aulaCollection: IAula[] = [sampleWithRequiredData];
        expectedResult = service.addAulaToCollectionIfMissing(aulaCollection, undefined, null);
        expect(expectedResult).toEqual(aulaCollection);
      });
    });

    describe('compareAula', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareAula(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 31179 };
        const entity2 = null;

        const compareResult1 = service.compareAula(entity1, entity2);
        const compareResult2 = service.compareAula(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 31179 };
        const entity2 = { id: 11398 };

        const compareResult1 = service.compareAula(entity1, entity2);
        const compareResult2 = service.compareAula(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 31179 };
        const entity2 = { id: 31179 };

        const compareResult1 = service.compareAula(entity1, entity2);
        const compareResult2 = service.compareAula(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
