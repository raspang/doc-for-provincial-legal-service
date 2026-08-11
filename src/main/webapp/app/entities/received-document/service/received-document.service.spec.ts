import { afterEach, beforeEach, describe, expect, it } from 'vitest';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { IReceivedDocument } from '../received-document.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../received-document.test-samples';

import { ReceivedDocumentService, RestReceivedDocument } from './received-document.service';

const requireRestSample: RestReceivedDocument = {
  ...sampleWithRequiredData,
  date: sampleWithRequiredData.date?.toJSON(),
  dateReleased: sampleWithRequiredData.dateReleased?.toJSON(),
};

describe('ReceivedDocument Service', () => {
  let service: ReceivedDocumentService;
  let httpMock: HttpTestingController;
  let expectedResult: IReceivedDocument | IReceivedDocument[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(ReceivedDocumentService);
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

    it('should create a ReceivedDocument', () => {
      const receivedDocument = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(receivedDocument).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ReceivedDocument', () => {
      const receivedDocument = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(receivedDocument).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ReceivedDocument', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ReceivedDocument', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a ReceivedDocument', () => {
      service.delete(123).subscribe();

      const requests = httpMock.match({ method: 'DELETE' });
      expect(requests).toHaveLength(1);
    });

    describe('addReceivedDocumentToCollectionIfMissing', () => {
      it('should add a ReceivedDocument to an empty array', () => {
        const receivedDocument: IReceivedDocument = sampleWithRequiredData;
        expectedResult = service.addReceivedDocumentToCollectionIfMissing([], receivedDocument);
        expect(expectedResult).toEqual([receivedDocument]);
      });

      it('should not add a ReceivedDocument to an array that contains it', () => {
        const receivedDocument: IReceivedDocument = sampleWithRequiredData;
        const receivedDocumentCollection: IReceivedDocument[] = [
          {
            ...receivedDocument,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addReceivedDocumentToCollectionIfMissing(receivedDocumentCollection, receivedDocument);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ReceivedDocument to an array that doesn't contain it", () => {
        const receivedDocument: IReceivedDocument = sampleWithRequiredData;
        const receivedDocumentCollection: IReceivedDocument[] = [sampleWithPartialData];
        expectedResult = service.addReceivedDocumentToCollectionIfMissing(receivedDocumentCollection, receivedDocument);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(receivedDocument);
      });

      it('should add only unique ReceivedDocument to an array', () => {
        const receivedDocumentArray: IReceivedDocument[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const receivedDocumentCollection: IReceivedDocument[] = [sampleWithRequiredData];
        expectedResult = service.addReceivedDocumentToCollectionIfMissing(receivedDocumentCollection, ...receivedDocumentArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const receivedDocument: IReceivedDocument = sampleWithRequiredData;
        const receivedDocument2: IReceivedDocument = sampleWithPartialData;
        expectedResult = service.addReceivedDocumentToCollectionIfMissing([], receivedDocument, receivedDocument2);
        expect(expectedResult).toEqual([receivedDocument, receivedDocument2]);
      });

      it('should accept null and undefined values', () => {
        const receivedDocument: IReceivedDocument = sampleWithRequiredData;
        expectedResult = service.addReceivedDocumentToCollectionIfMissing([], null, receivedDocument, undefined);
        expect(expectedResult).toEqual([receivedDocument]);
      });

      it('should return initial array if no ReceivedDocument is added', () => {
        const receivedDocumentCollection: IReceivedDocument[] = [sampleWithRequiredData];
        expectedResult = service.addReceivedDocumentToCollectionIfMissing(receivedDocumentCollection, undefined, null);
        expect(expectedResult).toEqual(receivedDocumentCollection);
      });
    });

    describe('compareReceivedDocument', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareReceivedDocument(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 17513 };
        const entity2 = null;

        const compareResult1 = service.compareReceivedDocument(entity1, entity2);
        const compareResult2 = service.compareReceivedDocument(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 17513 };
        const entity2 = { id: 23963 };

        const compareResult1 = service.compareReceivedDocument(entity1, entity2);
        const compareResult2 = service.compareReceivedDocument(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 17513 };
        const entity2 = { id: 17513 };

        const compareResult1 = service.compareReceivedDocument(entity1, entity2);
        const compareResult2 = service.compareReceivedDocument(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
