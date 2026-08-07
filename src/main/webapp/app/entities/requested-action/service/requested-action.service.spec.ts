import { afterEach, beforeEach, describe, expect, it } from 'vitest';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { IRequestedAction } from '../requested-action.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../requested-action.test-samples';

import { RequestedActionService } from './requested-action.service';

const requireRestSample: IRequestedAction = {
  ...sampleWithRequiredData,
};

describe('RequestedAction Service', () => {
  let service: RequestedActionService;
  let httpMock: HttpTestingController;
  let expectedResult: IRequestedAction | IRequestedAction[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(RequestedActionService);
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

    it('should create a RequestedAction', () => {
      const requestedAction = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(requestedAction).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a RequestedAction', () => {
      const requestedAction = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(requestedAction).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a RequestedAction', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of RequestedAction', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a RequestedAction', () => {
      service.delete(123).subscribe();

      const requests = httpMock.match({ method: 'DELETE' });
      expect(requests).toHaveLength(1);
    });

    describe('addRequestedActionToCollectionIfMissing', () => {
      it('should add a RequestedAction to an empty array', () => {
        const requestedAction: IRequestedAction = sampleWithRequiredData;
        expectedResult = service.addRequestedActionToCollectionIfMissing([], requestedAction);
        expect(expectedResult).toEqual([requestedAction]);
      });

      it('should not add a RequestedAction to an array that contains it', () => {
        const requestedAction: IRequestedAction = sampleWithRequiredData;
        const requestedActionCollection: IRequestedAction[] = [
          {
            ...requestedAction,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addRequestedActionToCollectionIfMissing(requestedActionCollection, requestedAction);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a RequestedAction to an array that doesn't contain it", () => {
        const requestedAction: IRequestedAction = sampleWithRequiredData;
        const requestedActionCollection: IRequestedAction[] = [sampleWithPartialData];
        expectedResult = service.addRequestedActionToCollectionIfMissing(requestedActionCollection, requestedAction);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(requestedAction);
      });

      it('should add only unique RequestedAction to an array', () => {
        const requestedActionArray: IRequestedAction[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const requestedActionCollection: IRequestedAction[] = [sampleWithRequiredData];
        expectedResult = service.addRequestedActionToCollectionIfMissing(requestedActionCollection, ...requestedActionArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const requestedAction: IRequestedAction = sampleWithRequiredData;
        const requestedAction2: IRequestedAction = sampleWithPartialData;
        expectedResult = service.addRequestedActionToCollectionIfMissing([], requestedAction, requestedAction2);
        expect(expectedResult).toEqual([requestedAction, requestedAction2]);
      });

      it('should accept null and undefined values', () => {
        const requestedAction: IRequestedAction = sampleWithRequiredData;
        expectedResult = service.addRequestedActionToCollectionIfMissing([], null, requestedAction, undefined);
        expect(expectedResult).toEqual([requestedAction]);
      });

      it('should return initial array if no RequestedAction is added', () => {
        const requestedActionCollection: IRequestedAction[] = [sampleWithRequiredData];
        expectedResult = service.addRequestedActionToCollectionIfMissing(requestedActionCollection, undefined, null);
        expect(expectedResult).toEqual(requestedActionCollection);
      });
    });

    describe('compareRequestedAction', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareRequestedAction(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 13303 };
        const entity2 = null;

        const compareResult1 = service.compareRequestedAction(entity1, entity2);
        const compareResult2 = service.compareRequestedAction(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 13303 };
        const entity2 = { id: 2669 };

        const compareResult1 = service.compareRequestedAction(entity1, entity2);
        const compareResult2 = service.compareRequestedAction(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 13303 };
        const entity2 = { id: 13303 };

        const compareResult1 = service.compareRequestedAction(entity1, entity2);
        const compareResult2 = service.compareRequestedAction(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
