import { afterEach, beforeEach, describe, expect, it } from 'vitest';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { IDocumentStatus } from '../document-status.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../document-status.test-samples';

import { DocumentStatusService } from './document-status.service';

const requireRestSample: IDocumentStatus = {
  ...sampleWithRequiredData,
};

describe('DocumentStatus Service', () => {
  let service: DocumentStatusService;
  let httpMock: HttpTestingController;
  let expectedResult: IDocumentStatus | IDocumentStatus[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(DocumentStatusService);
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

    it('should create a DocumentStatus', () => {
      const documentStatus = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(documentStatus).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a DocumentStatus', () => {
      const documentStatus = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(documentStatus).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a DocumentStatus', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of DocumentStatus', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a DocumentStatus', () => {
      service.delete(123).subscribe();

      const requests = httpMock.match({ method: 'DELETE' });
      expect(requests).toHaveLength(1);
    });

    describe('addDocumentStatusToCollectionIfMissing', () => {
      it('should add a DocumentStatus to an empty array', () => {
        const documentStatus: IDocumentStatus = sampleWithRequiredData;
        expectedResult = service.addDocumentStatusToCollectionIfMissing([], documentStatus);
        expect(expectedResult).toEqual([documentStatus]);
      });

      it('should not add a DocumentStatus to an array that contains it', () => {
        const documentStatus: IDocumentStatus = sampleWithRequiredData;
        const documentStatusCollection: IDocumentStatus[] = [
          {
            ...documentStatus,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addDocumentStatusToCollectionIfMissing(documentStatusCollection, documentStatus);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a DocumentStatus to an array that doesn't contain it", () => {
        const documentStatus: IDocumentStatus = sampleWithRequiredData;
        const documentStatusCollection: IDocumentStatus[] = [sampleWithPartialData];
        expectedResult = service.addDocumentStatusToCollectionIfMissing(documentStatusCollection, documentStatus);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(documentStatus);
      });

      it('should add only unique DocumentStatus to an array', () => {
        const documentStatusArray: IDocumentStatus[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const documentStatusCollection: IDocumentStatus[] = [sampleWithRequiredData];
        expectedResult = service.addDocumentStatusToCollectionIfMissing(documentStatusCollection, ...documentStatusArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const documentStatus: IDocumentStatus = sampleWithRequiredData;
        const documentStatus2: IDocumentStatus = sampleWithPartialData;
        expectedResult = service.addDocumentStatusToCollectionIfMissing([], documentStatus, documentStatus2);
        expect(expectedResult).toEqual([documentStatus, documentStatus2]);
      });

      it('should accept null and undefined values', () => {
        const documentStatus: IDocumentStatus = sampleWithRequiredData;
        expectedResult = service.addDocumentStatusToCollectionIfMissing([], null, documentStatus, undefined);
        expect(expectedResult).toEqual([documentStatus]);
      });

      it('should return initial array if no DocumentStatus is added', () => {
        const documentStatusCollection: IDocumentStatus[] = [sampleWithRequiredData];
        expectedResult = service.addDocumentStatusToCollectionIfMissing(documentStatusCollection, undefined, null);
        expect(expectedResult).toEqual(documentStatusCollection);
      });
    });

    describe('compareDocumentStatus', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareDocumentStatus(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 22980 };
        const entity2 = null;

        const compareResult1 = service.compareDocumentStatus(entity1, entity2);
        const compareResult2 = service.compareDocumentStatus(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 22980 };
        const entity2 = { id: 12288 };

        const compareResult1 = service.compareDocumentStatus(entity1, entity2);
        const compareResult2 = service.compareDocumentStatus(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 22980 };
        const entity2 = { id: 22980 };

        const compareResult1 = service.compareDocumentStatus(entity1, entity2);
        const compareResult2 = service.compareDocumentStatus(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
