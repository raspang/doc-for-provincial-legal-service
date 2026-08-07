import { afterEach, beforeEach, describe, expect, it } from 'vitest';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { IResponsiblePerson } from '../responsible-person.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../responsible-person.test-samples';

import { ResponsiblePersonService } from './responsible-person.service';

const requireRestSample: IResponsiblePerson = {
  ...sampleWithRequiredData,
};

describe('ResponsiblePerson Service', () => {
  let service: ResponsiblePersonService;
  let httpMock: HttpTestingController;
  let expectedResult: IResponsiblePerson | IResponsiblePerson[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(ResponsiblePersonService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a ResponsiblePerson', () => {
      const responsiblePerson = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(responsiblePerson).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ResponsiblePerson', () => {
      const responsiblePerson = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(responsiblePerson).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ResponsiblePerson', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ResponsiblePerson', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a ResponsiblePerson', () => {
      service.delete(123).subscribe();

      const requests = httpMock.match({ method: 'DELETE' });
      expect(requests).toHaveLength(1);
    });

    describe('addResponsiblePersonToCollectionIfMissing', () => {
      it('should add a ResponsiblePerson to an empty array', () => {
        const responsiblePerson: IResponsiblePerson = sampleWithRequiredData;
        expectedResult = service.addResponsiblePersonToCollectionIfMissing([], responsiblePerson);
        expect(expectedResult).toEqual([responsiblePerson]);
      });

      it('should not add a ResponsiblePerson to an array that contains it', () => {
        const responsiblePerson: IResponsiblePerson = sampleWithRequiredData;
        const responsiblePersonCollection: IResponsiblePerson[] = [
          {
            ...responsiblePerson,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addResponsiblePersonToCollectionIfMissing(responsiblePersonCollection, responsiblePerson);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ResponsiblePerson to an array that doesn't contain it", () => {
        const responsiblePerson: IResponsiblePerson = sampleWithRequiredData;
        const responsiblePersonCollection: IResponsiblePerson[] = [sampleWithPartialData];
        expectedResult = service.addResponsiblePersonToCollectionIfMissing(responsiblePersonCollection, responsiblePerson);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(responsiblePerson);
      });

      it('should add only unique ResponsiblePerson to an array', () => {
        const responsiblePersonArray: IResponsiblePerson[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const responsiblePersonCollection: IResponsiblePerson[] = [sampleWithRequiredData];
        expectedResult = service.addResponsiblePersonToCollectionIfMissing(responsiblePersonCollection, ...responsiblePersonArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const responsiblePerson: IResponsiblePerson = sampleWithRequiredData;
        const responsiblePerson2: IResponsiblePerson = sampleWithPartialData;
        expectedResult = service.addResponsiblePersonToCollectionIfMissing([], responsiblePerson, responsiblePerson2);
        expect(expectedResult).toEqual([responsiblePerson, responsiblePerson2]);
      });

      it('should accept null and undefined values', () => {
        const responsiblePerson: IResponsiblePerson = sampleWithRequiredData;
        expectedResult = service.addResponsiblePersonToCollectionIfMissing([], null, responsiblePerson, undefined);
        expect(expectedResult).toEqual([responsiblePerson]);
      });

      it('should return initial array if no ResponsiblePerson is added', () => {
        const responsiblePersonCollection: IResponsiblePerson[] = [sampleWithRequiredData];
        expectedResult = service.addResponsiblePersonToCollectionIfMissing(responsiblePersonCollection, undefined, null);
        expect(expectedResult).toEqual(responsiblePersonCollection);
      });
    });

    describe('compareResponsiblePerson', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareResponsiblePerson(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 29109 };
        const entity2 = null;

        const compareResult1 = service.compareResponsiblePerson(entity1, entity2);
        const compareResult2 = service.compareResponsiblePerson(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 29109 };
        const entity2 = { id: 13363 };

        const compareResult1 = service.compareResponsiblePerson(entity1, entity2);
        const compareResult2 = service.compareResponsiblePerson(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 29109 };
        const entity2 = { id: 29109 };

        const compareResult1 = service.compareResponsiblePerson(entity1, entity2);
        const compareResult2 = service.compareResponsiblePerson(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
