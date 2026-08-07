import { beforeEach, describe, expect, it, vitest } from 'vitest';
import { HttpResponse } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { Subject, from, of } from 'rxjs';

import { TypeOfDocumentService } from 'app/entities/type-of-document/service/type-of-document.service';
import { ITypeOfDocument } from 'app/entities/type-of-document/type-of-document.model';
import { IDocumentReference } from '../document-reference.model';
import { DocumentReferenceService } from '../service/document-reference.service';

import { DocumentReferenceFormService } from './document-reference-form.service';
import { DocumentReferenceUpdate } from './document-reference-update';

describe('DocumentReference Management Update Component', () => {
  let comp: DocumentReferenceUpdate;
  let fixture: ComponentFixture<DocumentReferenceUpdate>;
  let activatedRoute: ActivatedRoute;
  let documentReferenceFormService: DocumentReferenceFormService;
  let documentReferenceService: DocumentReferenceService;
  let typeOfDocumentService: TypeOfDocumentService;

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

    fixture = TestBed.createComponent(DocumentReferenceUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    documentReferenceFormService = TestBed.inject(DocumentReferenceFormService);
    documentReferenceService = TestBed.inject(DocumentReferenceService);
    typeOfDocumentService = TestBed.inject(TypeOfDocumentService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call TypeOfDocument query and add missing value', () => {
      const documentReference: IDocumentReference = { id: 6527 };
      const typeOfDocument: ITypeOfDocument = { id: 8066 };
      documentReference.typeOfDocument = typeOfDocument;

      const typeOfDocumentCollection: ITypeOfDocument[] = [{ id: 8066 }];
      vitest.spyOn(typeOfDocumentService, 'query').mockReturnValue(of(new HttpResponse({ body: typeOfDocumentCollection })));
      const additionalTypeOfDocuments = [typeOfDocument];
      const expectedCollection: ITypeOfDocument[] = [...additionalTypeOfDocuments, ...typeOfDocumentCollection];
      vitest.spyOn(typeOfDocumentService, 'addTypeOfDocumentToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ documentReference });
      comp.ngOnInit();

      expect(typeOfDocumentService.query).toHaveBeenCalled();
      expect(typeOfDocumentService.addTypeOfDocumentToCollectionIfMissing).toHaveBeenCalledWith(
        typeOfDocumentCollection,
        ...additionalTypeOfDocuments.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.typeOfDocumentsSharedCollection()).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const documentReference: IDocumentReference = { id: 6527 };
      const typeOfDocument: ITypeOfDocument = { id: 8066 };
      documentReference.typeOfDocument = typeOfDocument;

      activatedRoute.data = of({ documentReference });
      comp.ngOnInit();

      expect(comp.typeOfDocumentsSharedCollection()).toContainEqual(typeOfDocument);
      expect(comp.documentReference).toEqual(documentReference);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<IDocumentReference>();
      const documentReference = { id: 29885 };
      vitest.spyOn(documentReferenceFormService, 'getDocumentReference').mockReturnValue(documentReference);
      vitest.spyOn(documentReferenceService, 'update').mockReturnValue(saveSubject);
      vitest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ documentReference });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(documentReference);
      saveSubject.complete();

      // THEN
      expect(documentReferenceFormService.getDocumentReference).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(documentReferenceService.update).toHaveBeenCalledWith(expect.objectContaining(documentReference));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<IDocumentReference>();
      const documentReference = { id: 29885 };
      vitest.spyOn(documentReferenceFormService, 'getDocumentReference').mockReturnValue({ id: null });
      vitest.spyOn(documentReferenceService, 'create').mockReturnValue(saveSubject);
      vitest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ documentReference: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(documentReference);
      saveSubject.complete();

      // THEN
      expect(documentReferenceFormService.getDocumentReference).toHaveBeenCalled();
      expect(documentReferenceService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<IDocumentReference>();
      const documentReference = { id: 29885 };
      vitest.spyOn(documentReferenceService, 'update').mockReturnValue(saveSubject);
      vitest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ documentReference });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(documentReferenceService.update).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareTypeOfDocument', () => {
      it('should forward to typeOfDocumentService', () => {
        const entity = { id: 8066 };
        const entity2 = { id: 9309 };
        vitest.spyOn(typeOfDocumentService, 'compareTypeOfDocument');
        comp.compareTypeOfDocument(entity, entity2);
        expect(typeOfDocumentService.compareTypeOfDocument).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
