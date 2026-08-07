import { afterEach, beforeEach, describe, expect, it } from 'vitest';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { ITypeOfDocument } from '../type-of-document.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../type-of-document.test-samples';

import { TypeOfDocumentService } from './type-of-document.service';

const requireRestSample: ITypeOfDocument = {
  ...sampleWithRequiredData,
};

describe('TypeOfDocument Service', () => {
  let service: TypeOfDocumentService;
  let httpMock: HttpTestingController;
  let expectedResult: ITypeOfDocument | ITypeOfDocument[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(TypeOfDocumentService);
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

    it('should create a TypeOfDocument', () => {
      const typeOfDocument = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(typeOfDocument).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a TypeOfDocument', () => {
      const typeOfDocument = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(typeOfDocument).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a TypeOfDocument', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of TypeOfDocument', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a TypeOfDocument', () => {
      service.delete(123).subscribe();

      const requests = httpMock.match({ method: 'DELETE' });
      expect(requests).toHaveLength(1);
    });

    describe('addTypeOfDocumentToCollectionIfMissing', () => {
      it('should add a TypeOfDocument to an empty array', () => {
        const typeOfDocument: ITypeOfDocument = sampleWithRequiredData;
        expectedResult = service.addTypeOfDocumentToCollectionIfMissing([], typeOfDocument);
        expect(expectedResult).toEqual([typeOfDocument]);
      });

      it('should not add a TypeOfDocument to an array that contains it', () => {
        const typeOfDocument: ITypeOfDocument = sampleWithRequiredData;
        const typeOfDocumentCollection: ITypeOfDocument[] = [
          {
            ...typeOfDocument,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addTypeOfDocumentToCollectionIfMissing(typeOfDocumentCollection, typeOfDocument);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a TypeOfDocument to an array that doesn't contain it", () => {
        const typeOfDocument: ITypeOfDocument = sampleWithRequiredData;
        const typeOfDocumentCollection: ITypeOfDocument[] = [sampleWithPartialData];
        expectedResult = service.addTypeOfDocumentToCollectionIfMissing(typeOfDocumentCollection, typeOfDocument);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(typeOfDocument);
      });

      it('should add only unique TypeOfDocument to an array', () => {
        const typeOfDocumentArray: ITypeOfDocument[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const typeOfDocumentCollection: ITypeOfDocument[] = [sampleWithRequiredData];
        expectedResult = service.addTypeOfDocumentToCollectionIfMissing(typeOfDocumentCollection, ...typeOfDocumentArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const typeOfDocument: ITypeOfDocument = sampleWithRequiredData;
        const typeOfDocument2: ITypeOfDocument = sampleWithPartialData;
        expectedResult = service.addTypeOfDocumentToCollectionIfMissing([], typeOfDocument, typeOfDocument2);
        expect(expectedResult).toEqual([typeOfDocument, typeOfDocument2]);
      });

      it('should accept null and undefined values', () => {
        const typeOfDocument: ITypeOfDocument = sampleWithRequiredData;
        expectedResult = service.addTypeOfDocumentToCollectionIfMissing([], null, typeOfDocument, undefined);
        expect(expectedResult).toEqual([typeOfDocument]);
      });

      it('should return initial array if no TypeOfDocument is added', () => {
        const typeOfDocumentCollection: ITypeOfDocument[] = [sampleWithRequiredData];
        expectedResult = service.addTypeOfDocumentToCollectionIfMissing(typeOfDocumentCollection, undefined, null);
        expect(expectedResult).toEqual(typeOfDocumentCollection);
      });
    });

    describe('compareTypeOfDocument', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareTypeOfDocument(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 8066 };
        const entity2 = null;

        const compareResult1 = service.compareTypeOfDocument(entity1, entity2);
        const compareResult2 = service.compareTypeOfDocument(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 8066 };
        const entity2 = { id: 9309 };

        const compareResult1 = service.compareTypeOfDocument(entity1, entity2);
        const compareResult2 = service.compareTypeOfDocument(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 8066 };
        const entity2 = { id: 8066 };

        const compareResult1 = service.compareTypeOfDocument(entity1, entity2);
        const compareResult2 = service.compareTypeOfDocument(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
