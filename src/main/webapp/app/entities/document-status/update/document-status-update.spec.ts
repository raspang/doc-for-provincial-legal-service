import { beforeEach, describe, expect, it, vitest } from 'vitest';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { Subject, from, of } from 'rxjs';

import { IDocumentStatus } from '../document-status.model';
import { DocumentStatusService } from '../service/document-status.service';

import { DocumentStatusFormService } from './document-status-form.service';
import { DocumentStatusUpdate } from './document-status-update';

describe('DocumentStatus Management Update Component', () => {
  let comp: DocumentStatusUpdate;
  let fixture: ComponentFixture<DocumentStatusUpdate>;
  let activatedRoute: ActivatedRoute;
  let documentStatusFormService: DocumentStatusFormService;
  let documentStatusService: DocumentStatusService;

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

    fixture = TestBed.createComponent(DocumentStatusUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    documentStatusFormService = TestBed.inject(DocumentStatusFormService);
    documentStatusService = TestBed.inject(DocumentStatusService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const documentStatus: IDocumentStatus = { id: 12288 };

      activatedRoute.data = of({ documentStatus });
      comp.ngOnInit();

      expect(comp.documentStatus).toEqual(documentStatus);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<IDocumentStatus>();
      const documentStatus = { id: 22980 };
      vitest.spyOn(documentStatusFormService, 'getDocumentStatus').mockReturnValue(documentStatus);
      vitest.spyOn(documentStatusService, 'update').mockReturnValue(saveSubject);
      vitest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ documentStatus });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(documentStatus);
      saveSubject.complete();

      // THEN
      expect(documentStatusFormService.getDocumentStatus).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(documentStatusService.update).toHaveBeenCalledWith(expect.objectContaining(documentStatus));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<IDocumentStatus>();
      const documentStatus = { id: 22980 };
      vitest.spyOn(documentStatusFormService, 'getDocumentStatus').mockReturnValue({ id: null });
      vitest.spyOn(documentStatusService, 'create').mockReturnValue(saveSubject);
      vitest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ documentStatus: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(documentStatus);
      saveSubject.complete();

      // THEN
      expect(documentStatusFormService.getDocumentStatus).toHaveBeenCalled();
      expect(documentStatusService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<IDocumentStatus>();
      const documentStatus = { id: 22980 };
      vitest.spyOn(documentStatusService, 'update').mockReturnValue(saveSubject);
      vitest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ documentStatus });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(documentStatusService.update).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
