import { beforeEach, describe, expect, it, vitest } from 'vitest';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { Subject, from, of } from 'rxjs';

import { IRequestedAction } from '../requested-action.model';
import { RequestedActionService } from '../service/requested-action.service';

import { RequestedActionFormService } from './requested-action-form.service';
import { RequestedActionUpdate } from './requested-action-update';

describe('RequestedAction Management Update Component', () => {
  let comp: RequestedActionUpdate;
  let fixture: ComponentFixture<RequestedActionUpdate>;
  let activatedRoute: ActivatedRoute;
  let requestedActionFormService: RequestedActionFormService;
  let requestedActionService: RequestedActionService;

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

    fixture = TestBed.createComponent(RequestedActionUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    requestedActionFormService = TestBed.inject(RequestedActionFormService);
    requestedActionService = TestBed.inject(RequestedActionService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const requestedAction: IRequestedAction = { id: 2669 };

      activatedRoute.data = of({ requestedAction });
      comp.ngOnInit();

      expect(comp.requestedAction).toEqual(requestedAction);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<IRequestedAction>();
      const requestedAction = { id: 13303 };
      vitest.spyOn(requestedActionFormService, 'getRequestedAction').mockReturnValue(requestedAction);
      vitest.spyOn(requestedActionService, 'update').mockReturnValue(saveSubject);
      vitest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ requestedAction });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(requestedAction);
      saveSubject.complete();

      // THEN
      expect(requestedActionFormService.getRequestedAction).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(requestedActionService.update).toHaveBeenCalledWith(expect.objectContaining(requestedAction));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<IRequestedAction>();
      const requestedAction = { id: 13303 };
      vitest.spyOn(requestedActionFormService, 'getRequestedAction').mockReturnValue({ id: null });
      vitest.spyOn(requestedActionService, 'create').mockReturnValue(saveSubject);
      vitest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ requestedAction: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(requestedAction);
      saveSubject.complete();

      // THEN
      expect(requestedActionFormService.getRequestedAction).toHaveBeenCalled();
      expect(requestedActionService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<IRequestedAction>();
      const requestedAction = { id: 13303 };
      vitest.spyOn(requestedActionService, 'update').mockReturnValue(saveSubject);
      vitest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ requestedAction });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(requestedActionService.update).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
