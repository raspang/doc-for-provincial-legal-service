import { beforeEach, describe, expect, it, vitest } from 'vitest';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { Subject, from, of } from 'rxjs';

import { IResponsiblePerson } from '../responsible-person.model';
import { ResponsiblePersonService } from '../service/responsible-person.service';

import { ResponsiblePersonFormService } from './responsible-person-form.service';
import { ResponsiblePersonUpdate } from './responsible-person-update';

describe('ResponsiblePerson Management Update Component', () => {
  let comp: ResponsiblePersonUpdate;
  let fixture: ComponentFixture<ResponsiblePersonUpdate>;
  let activatedRoute: ActivatedRoute;
  let responsiblePersonFormService: ResponsiblePersonFormService;
  let responsiblePersonService: ResponsiblePersonService;

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

    fixture = TestBed.createComponent(ResponsiblePersonUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    responsiblePersonFormService = TestBed.inject(ResponsiblePersonFormService);
    responsiblePersonService = TestBed.inject(ResponsiblePersonService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const responsiblePerson: IResponsiblePerson = { id: 13363 };

      activatedRoute.data = of({ responsiblePerson });
      comp.ngOnInit();

      expect(comp.responsiblePerson).toEqual(responsiblePerson);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<IResponsiblePerson>();
      const responsiblePerson = { id: 29109 };
      vitest.spyOn(responsiblePersonFormService, 'getResponsiblePerson').mockReturnValue(responsiblePerson);
      vitest.spyOn(responsiblePersonService, 'update').mockReturnValue(saveSubject);
      vitest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ responsiblePerson });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(responsiblePerson);
      saveSubject.complete();

      // THEN
      expect(responsiblePersonFormService.getResponsiblePerson).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(responsiblePersonService.update).toHaveBeenCalledWith(expect.objectContaining(responsiblePerson));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<IResponsiblePerson>();
      const responsiblePerson = { id: 29109 };
      vitest.spyOn(responsiblePersonFormService, 'getResponsiblePerson').mockReturnValue({ id: null });
      vitest.spyOn(responsiblePersonService, 'create').mockReturnValue(saveSubject);
      vitest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ responsiblePerson: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(responsiblePerson);
      saveSubject.complete();

      // THEN
      expect(responsiblePersonFormService.getResponsiblePerson).toHaveBeenCalled();
      expect(responsiblePersonService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<IResponsiblePerson>();
      const responsiblePerson = { id: 29109 };
      vitest.spyOn(responsiblePersonService, 'update').mockReturnValue(saveSubject);
      vitest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ responsiblePerson });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(responsiblePersonService.update).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
