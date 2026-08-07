import { afterEach, beforeEach, describe, expect, it } from 'vitest';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { IDocumentHistory } from '../document-history.model';
import { sampleWithFullData, sampleWithPartialData, sampleWithRequiredData } from '../document-history.test-samples';

import { DocumentHistoryService, RestDocumentHistory } from './document-history.service';

const requireRestSample: RestDocumentHistory = {
  ...sampleWithRequiredData,
  timestamp: sampleWithRequiredData.timestamp?.toJSON(),
};

describe('DocumentHistory Service', () => {
  let service: DocumentHistoryService;
  let httpMock: HttpTestingController;
  let expectedResult: IDocumentHistory | IDocumentHistory[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(DocumentHistoryService);
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

    it('should return a list of DocumentHistory', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    describe('addDocumentHistoryToCollectionIfMissing', () => {
      it('should add a DocumentHistory to an empty array', () => {
        const documentHistory: IDocumentHistory = sampleWithRequiredData;
        expectedResult = service.addDocumentHistoryToCollectionIfMissing([], documentHistory);
        expect(expectedResult).toEqual([documentHistory]);
      });

      it('should not add a DocumentHistory to an array that contains it', () => {
        const documentHistory: IDocumentHistory = sampleWithRequiredData;
        const documentHistoryCollection: IDocumentHistory[] = [
          {
            ...documentHistory,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addDocumentHistoryToCollectionIfMissing(documentHistoryCollection, documentHistory);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a DocumentHistory to an array that doesn't contain it", () => {
        const documentHistory: IDocumentHistory = sampleWithRequiredData;
        const documentHistoryCollection: IDocumentHistory[] = [sampleWithPartialData];
        expectedResult = service.addDocumentHistoryToCollectionIfMissing(documentHistoryCollection, documentHistory);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(documentHistory);
      });

      it('should add only unique DocumentHistory to an array', () => {
        const documentHistoryArray: IDocumentHistory[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const documentHistoryCollection: IDocumentHistory[] = [sampleWithRequiredData];
        expectedResult = service.addDocumentHistoryToCollectionIfMissing(documentHistoryCollection, ...documentHistoryArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const documentHistory: IDocumentHistory = sampleWithRequiredData;
        const documentHistory2: IDocumentHistory = sampleWithPartialData;
        expectedResult = service.addDocumentHistoryToCollectionIfMissing([], documentHistory, documentHistory2);
        expect(expectedResult).toEqual([documentHistory, documentHistory2]);
      });

      it('should accept null and undefined values', () => {
        const documentHistory: IDocumentHistory = sampleWithRequiredData;
        expectedResult = service.addDocumentHistoryToCollectionIfMissing([], null, documentHistory, undefined);
        expect(expectedResult).toEqual([documentHistory]);
      });

      it('should return initial array if no DocumentHistory is added', () => {
        const documentHistoryCollection: IDocumentHistory[] = [sampleWithRequiredData];
        expectedResult = service.addDocumentHistoryToCollectionIfMissing(documentHistoryCollection, undefined, null);
        expect(expectedResult).toEqual(documentHistoryCollection);
      });
    });

    describe('compareDocumentHistory', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareDocumentHistory(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 25873 };
        const entity2 = null;

        const compareResult1 = service.compareDocumentHistory(entity1, entity2);
        const compareResult2 = service.compareDocumentHistory(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 25873 };
        const entity2 = { id: 2900 };

        const compareResult1 = service.compareDocumentHistory(entity1, entity2);
        const compareResult2 = service.compareDocumentHistory(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 25873 };
        const entity2 = { id: 25873 };

        const compareResult1 = service.compareDocumentHistory(entity1, entity2);
        const compareResult2 = service.compareDocumentHistory(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
