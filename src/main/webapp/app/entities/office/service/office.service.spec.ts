import { afterEach, beforeEach, describe, expect, it } from 'vitest';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { IOffice } from '../office.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../office.test-samples';

import { OfficeService } from './office.service';

const requireRestSample: IOffice = {
  ...sampleWithRequiredData,
};

describe('Office Service', () => {
  let service: OfficeService;
  let httpMock: HttpTestingController;
  let expectedResult: IOffice | IOffice[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(OfficeService);
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

    it('should create a Office', () => {
      const office = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(office).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Office', () => {
      const office = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(office).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Office', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Office', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Office', () => {
      service.delete(123).subscribe();

      const requests = httpMock.match({ method: 'DELETE' });
      expect(requests).toHaveLength(1);
    });

    describe('addOfficeToCollectionIfMissing', () => {
      it('should add a Office to an empty array', () => {
        const office: IOffice = sampleWithRequiredData;
        expectedResult = service.addOfficeToCollectionIfMissing([], office);
        expect(expectedResult).toEqual([office]);
      });

      it('should not add a Office to an array that contains it', () => {
        const office: IOffice = sampleWithRequiredData;
        const officeCollection: IOffice[] = [
          {
            ...office,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addOfficeToCollectionIfMissing(officeCollection, office);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Office to an array that doesn't contain it", () => {
        const office: IOffice = sampleWithRequiredData;
        const officeCollection: IOffice[] = [sampleWithPartialData];
        expectedResult = service.addOfficeToCollectionIfMissing(officeCollection, office);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(office);
      });

      it('should add only unique Office to an array', () => {
        const officeArray: IOffice[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const officeCollection: IOffice[] = [sampleWithRequiredData];
        expectedResult = service.addOfficeToCollectionIfMissing(officeCollection, ...officeArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const office: IOffice = sampleWithRequiredData;
        const office2: IOffice = sampleWithPartialData;
        expectedResult = service.addOfficeToCollectionIfMissing([], office, office2);
        expect(expectedResult).toEqual([office, office2]);
      });

      it('should accept null and undefined values', () => {
        const office: IOffice = sampleWithRequiredData;
        expectedResult = service.addOfficeToCollectionIfMissing([], null, office, undefined);
        expect(expectedResult).toEqual([office]);
      });

      it('should return initial array if no Office is added', () => {
        const officeCollection: IOffice[] = [sampleWithRequiredData];
        expectedResult = service.addOfficeToCollectionIfMissing(officeCollection, undefined, null);
        expect(expectedResult).toEqual(officeCollection);
      });
    });

    describe('compareOffice', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareOffice(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 20465 };
        const entity2 = null;

        const compareResult1 = service.compareOffice(entity1, entity2);
        const compareResult2 = service.compareOffice(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 20465 };
        const entity2 = { id: 7490 };

        const compareResult1 = service.compareOffice(entity1, entity2);
        const compareResult2 = service.compareOffice(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 20465 };
        const entity2 = { id: 20465 };

        const compareResult1 = service.compareOffice(entity1, entity2);
        const compareResult2 = service.compareOffice(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
