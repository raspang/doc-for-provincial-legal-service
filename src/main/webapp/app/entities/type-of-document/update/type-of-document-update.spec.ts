import { beforeEach, describe, expect, it, vitest } from 'vitest';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { Subject, from, of } from 'rxjs';

import { TypeOfDocumentService } from '../service/type-of-document.service';
import { ITypeOfDocument } from '../type-of-document.model';

import { TypeOfDocumentFormService } from './type-of-document-form.service';
import { TypeOfDocumentUpdate } from './type-of-document-update';

describe('TypeOfDocument Management Update Component', () => {
  let comp: TypeOfDocumentUpdate;
  let fixture: ComponentFixture<TypeOfDocumentUpdate>;
  let activatedRoute: ActivatedRoute;
  let typeOfDocumentFormService: TypeOfDocumentFormService;
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

    fixture = TestBed.createComponent(TypeOfDocumentUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    typeOfDocumentFormService = TestBed.inject(TypeOfDocumentFormService);
    typeOfDocumentService = TestBed.inject(TypeOfDocumentService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const typeOfDocument: ITypeOfDocument = { id: 9309 };

      activatedRoute.data = of({ typeOfDocument });
      comp.ngOnInit();

      expect(comp.typeOfDocument).toEqual(typeOfDocument);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<ITypeOfDocument>();
      const typeOfDocument = { id: 8066 };
      vitest.spyOn(typeOfDocumentFormService, 'getTypeOfDocument').mockReturnValue(typeOfDocument);
      vitest.spyOn(typeOfDocumentService, 'update').mockReturnValue(saveSubject);
      vitest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ typeOfDocument });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(typeOfDocument);
      saveSubject.complete();

      // THEN
      expect(typeOfDocumentFormService.getTypeOfDocument).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(typeOfDocumentService.update).toHaveBeenCalledWith(expect.objectContaining(typeOfDocument));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<ITypeOfDocument>();
      const typeOfDocument = { id: 8066 };
      vitest.spyOn(typeOfDocumentFormService, 'getTypeOfDocument').mockReturnValue({ id: null });
      vitest.spyOn(typeOfDocumentService, 'create').mockReturnValue(saveSubject);
      vitest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ typeOfDocument: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(typeOfDocument);
      saveSubject.complete();

      // THEN
      expect(typeOfDocumentFormService.getTypeOfDocument).toHaveBeenCalled();
      expect(typeOfDocumentService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<ITypeOfDocument>();
      const typeOfDocument = { id: 8066 };
      vitest.spyOn(typeOfDocumentService, 'update').mockReturnValue(saveSubject);
      vitest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ typeOfDocument });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(typeOfDocumentService.update).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
