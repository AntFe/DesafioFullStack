import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IMateria } from '../materia.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../materia.test-samples';

import { MateriaService } from './materia.service';

const requireRestSample: IMateria = {
  ...sampleWithRequiredData,
};

describe('Materia Service', () => {
  let service: MateriaService;
  let httpMock: HttpTestingController;
  let expectedResult: IMateria | IMateria[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(MateriaService);
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

    it('should create a Materia', () => {
      const materia = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(materia).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Materia', () => {
      const materia = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(materia).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Materia', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Materia', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Materia', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addMateriaToCollectionIfMissing', () => {
      it('should add a Materia to an empty array', () => {
        const materia: IMateria = sampleWithRequiredData;
        expectedResult = service.addMateriaToCollectionIfMissing([], materia);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(materia);
      });

      it('should not add a Materia to an array that contains it', () => {
        const materia: IMateria = sampleWithRequiredData;
        const materiaCollection: IMateria[] = [
          {
            ...materia,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addMateriaToCollectionIfMissing(materiaCollection, materia);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Materia to an array that doesn't contain it", () => {
        const materia: IMateria = sampleWithRequiredData;
        const materiaCollection: IMateria[] = [sampleWithPartialData];
        expectedResult = service.addMateriaToCollectionIfMissing(materiaCollection, materia);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(materia);
      });

      it('should add only unique Materia to an array', () => {
        const materiaArray: IMateria[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const materiaCollection: IMateria[] = [sampleWithRequiredData];
        expectedResult = service.addMateriaToCollectionIfMissing(materiaCollection, ...materiaArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const materia: IMateria = sampleWithRequiredData;
        const materia2: IMateria = sampleWithPartialData;
        expectedResult = service.addMateriaToCollectionIfMissing([], materia, materia2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(materia);
        expect(expectedResult).toContain(materia2);
      });

      it('should accept null and undefined values', () => {
        const materia: IMateria = sampleWithRequiredData;
        expectedResult = service.addMateriaToCollectionIfMissing([], null, materia, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(materia);
      });

      it('should return initial array if no Materia is added', () => {
        const materiaCollection: IMateria[] = [sampleWithRequiredData];
        expectedResult = service.addMateriaToCollectionIfMissing(materiaCollection, undefined, null);
        expect(expectedResult).toEqual(materiaCollection);
      });
    });

    describe('compareMateria', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareMateria(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 23511 };
        const entity2 = null;

        const compareResult1 = service.compareMateria(entity1, entity2);
        const compareResult2 = service.compareMateria(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 23511 };
        const entity2 = { id: 7681 };

        const compareResult1 = service.compareMateria(entity1, entity2);
        const compareResult2 = service.compareMateria(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 23511 };
        const entity2 = { id: 23511 };

        const compareResult1 = service.compareMateria(entity1, entity2);
        const compareResult2 = service.compareMateria(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
