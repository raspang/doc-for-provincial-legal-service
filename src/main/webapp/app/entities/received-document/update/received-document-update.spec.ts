import { beforeEach, describe, expect, it, vitest } from 'vitest';
import { HttpResponse } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { Subject, from, of } from 'rxjs';

import { IDocumentStatus } from 'app/entities/document-status/document-status.model';
import { DocumentStatusService } from 'app/entities/document-status/service/document-status.service';
import { IOffice } from 'app/entities/office/office.model';
import { OfficeService } from 'app/entities/office/service/office.service';
import { IRequestedAction } from 'app/entities/requested-action/requested-action.model';
import { RequestedActionService } from 'app/entities/requested-action/service/requested-action.service';
import { IResponsiblePerson } from 'app/entities/responsible-person/responsible-person.model';
import { ResponsiblePersonService } from 'app/entities/responsible-person/service/responsible-person.service';
import { TransactionTypeService } from 'app/entities/transaction-type/service/transaction-type.service';
import { ITransactionType } from 'app/entities/transaction-type/transaction-type.model';
import { TypeOfDocumentService } from 'app/entities/type-of-document/service/type-of-document.service';
import { ITypeOfDocument } from 'app/entities/type-of-document/type-of-document.model';
import { IReceivedDocument } from '../received-document.model';
import { ReceivedDocumentService } from '../service/received-document.service';

import { ReceivedDocumentFormService } from './received-document-form.service';
import { ReceivedDocumentUpdate } from './received-document-update';

describe('ReceivedDocument Management Update Component', () => {
  let comp: ReceivedDocumentUpdate;
  let fixture: ComponentFixture<ReceivedDocumentUpdate>;
  let activatedRoute: ActivatedRoute;
  let receivedDocumentFormService: ReceivedDocumentFormService;
  let receivedDocumentService: ReceivedDocumentService;
  let requestedActionService: RequestedActionService;
  let typeOfDocumentService: TypeOfDocumentService;
  let officeService: OfficeService;
  let responsiblePersonService: ResponsiblePersonService;
  let documentStatusService: DocumentStatusService;
  let transactionTypeService: TransactionTypeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        provideHttpClientTesting(),
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    });

    fixture = TestBed.createComponent(ReceivedDocumentUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    receivedDocumentFormService = TestBed.inject(ReceivedDocumentFormService);
    receivedDocumentService = TestBed.inject(ReceivedDocumentService);
    requestedActionService = TestBed.inject(RequestedActionService);
    typeOfDocumentService = TestBed.inject(TypeOfDocumentService);
    officeService = TestBed.inject(OfficeService);
    responsiblePersonService = TestBed.inject(ResponsiblePersonService);
    documentStatusService = TestBed.inject(DocumentStatusService);
    transactionTypeService = TestBed.inject(TransactionTypeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call RequestedAction query and add missing value', () => {
      const receivedDocument: IReceivedDocument = { id: 23963 };
      const requestedAction: IRequestedAction = { id: 13303 };
      receivedDocument.requestedAction = requestedAction;

      const requestedActionCollection: IRequestedAction[] = [{ id: 13303 }];
      vitest.spyOn(requestedActionService, 'query').mockReturnValue(of(new HttpResponse({ body: requestedActionCollection })));
      const additionalRequestedActions = [requestedAction];
      const expectedCollection: IRequestedAction[] = [...additionalRequestedActions, ...requestedActionCollection];
      vitest.spyOn(requestedActionService, 'addRequestedActionToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ receivedDocument });
      comp.ngOnInit();

      expect(requestedActionService.query).toHaveBeenCalled();
      expect(requestedActionService.addRequestedActionToCollectionIfMissing).toHaveBeenCalledWith(
        requestedActionCollection,
        ...additionalRequestedActions.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.requestedActionsSharedCollection()).toEqual(expectedCollection);
    });

    it('should call TypeOfDocument query and add missing value', () => {
      const receivedDocument: IReceivedDocument = { id: 23963 };
      const typeOfDocument: ITypeOfDocument = { id: 8066 };
      receivedDocument.typeOfDocument = typeOfDocument;

      const typeOfDocumentCollection: ITypeOfDocument[] = [{ id: 8066 }];
      vitest.spyOn(typeOfDocumentService, 'query').mockReturnValue(of(new HttpResponse({ body: typeOfDocumentCollection })));
      const additionalTypeOfDocuments = [typeOfDocument];
      const expectedCollection: ITypeOfDocument[] = [...additionalTypeOfDocuments, ...typeOfDocumentCollection];
      vitest.spyOn(typeOfDocumentService, 'addTypeOfDocumentToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ receivedDocument });
      comp.ngOnInit();

      expect(typeOfDocumentService.query).toHaveBeenCalled();
      expect(typeOfDocumentService.addTypeOfDocumentToCollectionIfMissing).toHaveBeenCalledWith(
        typeOfDocumentCollection,
        ...additionalTypeOfDocuments.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.typeOfDocumentsSharedCollection()).toEqual(expectedCollection);
    });

    it('should call Office query and add missing value', () => {
      const receivedDocument: IReceivedDocument = { id: 23963 };
      const office: IOffice = { id: 20465 };
      receivedDocument.office = office;

      const officeCollection: IOffice[] = [{ id: 20465 }];
      vitest.spyOn(officeService, 'query').mockReturnValue(of(new HttpResponse({ body: officeCollection })));
      const additionalOffices = [office];
      const expectedCollection: IOffice[] = [...additionalOffices, ...officeCollection];
      vitest.spyOn(officeService, 'addOfficeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ receivedDocument });
      comp.ngOnInit();

      expect(officeService.query).toHaveBeenCalled();
      expect(officeService.addOfficeToCollectionIfMissing).toHaveBeenCalledWith(
        officeCollection,
        ...additionalOffices.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.officesSharedCollection()).toEqual(expectedCollection);
    });

    it('should call ResponsiblePerson query and add missing value', () => {
      const receivedDocument: IReceivedDocument = { id: 23963 };
      const responsiblePerson: IResponsiblePerson = { id: 29109 };
      receivedDocument.responsiblePerson = responsiblePerson;

      const responsiblePersonCollection: IResponsiblePerson[] = [{ id: 29109 }];
      vitest.spyOn(responsiblePersonService, 'query').mockReturnValue(of(new HttpResponse({ body: responsiblePersonCollection })));
      const additionalResponsiblePeople = [responsiblePerson];
      const expectedCollection: IResponsiblePerson[] = [...additionalResponsiblePeople, ...responsiblePersonCollection];
      vitest.spyOn(responsiblePersonService, 'addResponsiblePersonToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ receivedDocument });
      comp.ngOnInit();

      expect(responsiblePersonService.query).toHaveBeenCalled();
      expect(responsiblePersonService.addResponsiblePersonToCollectionIfMissing).toHaveBeenCalledWith(
        responsiblePersonCollection,
        ...additionalResponsiblePeople.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.responsiblePeopleSharedCollection()).toEqual(expectedCollection);
    });

    it('should call DocumentStatus query and add missing value', () => {
      const receivedDocument: IReceivedDocument = { id: 23963 };
      const documentStatus: IDocumentStatus = { id: 22980 };
      receivedDocument.documentStatus = documentStatus;

      const documentStatusCollection: IDocumentStatus[] = [{ id: 22980 }];
      vitest.spyOn(documentStatusService, 'query').mockReturnValue(of(new HttpResponse({ body: documentStatusCollection })));
      const additionalDocumentStatuses = [documentStatus];
      const expectedCollection: IDocumentStatus[] = [...additionalDocumentStatuses, ...documentStatusCollection];
      vitest.spyOn(documentStatusService, 'addDocumentStatusToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ receivedDocument });
      comp.ngOnInit();

      expect(documentStatusService.query).toHaveBeenCalled();
      expect(documentStatusService.addDocumentStatusToCollectionIfMissing).toHaveBeenCalledWith(
        documentStatusCollection,
        ...additionalDocumentStatuses.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.documentStatusesSharedCollection()).toEqual(expectedCollection);
    });

    it('should call TransactionType query and add missing value', () => {
      const receivedDocument: IReceivedDocument = { id: 23963 };
      const transactionType: ITransactionType = { id: 4045 };
      receivedDocument.transactionType = transactionType;

      const transactionTypeCollection: ITransactionType[] = [{ id: 4045 }];
      vitest.spyOn(transactionTypeService, 'query').mockReturnValue(of(new HttpResponse({ body: transactionTypeCollection })));
      const additionalTransactionTypes = [transactionType];
      const expectedCollection: ITransactionType[] = [...additionalTransactionTypes, ...transactionTypeCollection];
      vitest.spyOn(transactionTypeService, 'addTransactionTypeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ receivedDocument });
      comp.ngOnInit();

      expect(transactionTypeService.query).toHaveBeenCalled();
      expect(transactionTypeService.addTransactionTypeToCollectionIfMissing).toHaveBeenCalledWith(
        transactionTypeCollection,
        ...additionalTransactionTypes.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.transactionTypesSharedCollection()).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const receivedDocument: IReceivedDocument = { id: 23963 };
      const requestedAction: IRequestedAction = { id: 13303 };
      receivedDocument.requestedAction = requestedAction;
      const typeOfDocument: ITypeOfDocument = { id: 8066 };
      receivedDocument.typeOfDocument = typeOfDocument;
      const office: IOffice = { id: 20465 };
      receivedDocument.office = office;
      const responsiblePerson: IResponsiblePerson = { id: 29109 };
      receivedDocument.responsiblePerson = responsiblePerson;
      const documentStatus: IDocumentStatus = { id: 22980 };
      receivedDocument.documentStatus = documentStatus;
      const transactionType: ITransactionType = { id: 4045 };
      receivedDocument.transactionType = transactionType;

      activatedRoute.data = of({ receivedDocument });
      comp.ngOnInit();

      expect(comp.requestedActionsSharedCollection()).toContainEqual(requestedAction);
      expect(comp.typeOfDocumentsSharedCollection()).toContainEqual(typeOfDocument);
      expect(comp.officesSharedCollection()).toContainEqual(office);
      expect(comp.responsiblePeopleSharedCollection()).toContainEqual(responsiblePerson);
      expect(comp.documentStatusesSharedCollection()).toContainEqual(documentStatus);
      expect(comp.transactionTypesSharedCollection()).toContainEqual(transactionType);
      expect(comp.receivedDocument).toEqual(receivedDocument);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<IReceivedDocument>();
      const receivedDocument = { id: 17513 };
      vitest.spyOn(receivedDocumentFormService, 'getReceivedDocument').mockReturnValue(receivedDocument);
      vitest.spyOn(receivedDocumentService, 'update').mockReturnValue(saveSubject);
      vitest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ receivedDocument });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(receivedDocument);
      saveSubject.complete();

      // THEN
      expect(receivedDocumentFormService.getReceivedDocument).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(receivedDocumentService.update).toHaveBeenCalledWith(expect.objectContaining(receivedDocument));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<IReceivedDocument>();
      const receivedDocument = { id: 17513 };
      vitest.spyOn(receivedDocumentFormService, 'getReceivedDocument').mockReturnValue({ id: null });
      vitest.spyOn(receivedDocumentService, 'create').mockReturnValue(saveSubject);
      vitest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ receivedDocument: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(receivedDocument);
      saveSubject.complete();

      // THEN
      expect(receivedDocumentFormService.getReceivedDocument).toHaveBeenCalled();
      expect(receivedDocumentService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<IReceivedDocument>();
      const receivedDocument = { id: 17513 };
      vitest.spyOn(receivedDocumentService, 'update').mockReturnValue(saveSubject);
      vitest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ receivedDocument });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(receivedDocumentService.update).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareRequestedAction', () => {
      it('should forward to requestedActionService', () => {
        const entity = { id: 13303 };
        const entity2 = { id: 2669 };
        vitest.spyOn(requestedActionService, 'compareRequestedAction');
        comp.compareRequestedAction(entity, entity2);
        expect(requestedActionService.compareRequestedAction).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareTypeOfDocument', () => {
      it('should forward to typeOfDocumentService', () => {
        const entity = { id: 8066 };
        const entity2 = { id: 9309 };
        vitest.spyOn(typeOfDocumentService, 'compareTypeOfDocument');
        comp.compareTypeOfDocument(entity, entity2);
        expect(typeOfDocumentService.compareTypeOfDocument).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareOffice', () => {
      it('should forward to officeService', () => {
        const entity = { id: 20465 };
        const entity2 = { id: 7490 };
        vitest.spyOn(officeService, 'compareOffice');
        comp.compareOffice(entity, entity2);
        expect(officeService.compareOffice).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareResponsiblePerson', () => {
      it('should forward to responsiblePersonService', () => {
        const entity = { id: 29109 };
        const entity2 = { id: 13363 };
        vitest.spyOn(responsiblePersonService, 'compareResponsiblePerson');
        comp.compareResponsiblePerson(entity, entity2);
        expect(responsiblePersonService.compareResponsiblePerson).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareDocumentStatus', () => {
      it('should forward to documentStatusService', () => {
        const entity = { id: 22980 };
        const entity2 = { id: 12288 };
        vitest.spyOn(documentStatusService, 'compareDocumentStatus');
        comp.compareDocumentStatus(entity, entity2);
        expect(documentStatusService.compareDocumentStatus).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareTransactionType', () => {
      it('should forward to transactionTypeService', () => {
        const entity = { id: 4045 };
        const entity2 = { id: 32159 };
        vitest.spyOn(transactionTypeService, 'compareTransactionType');
        comp.compareTransactionType(entity, entity2);
        expect(transactionTypeService.compareTransactionType).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
