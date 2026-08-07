import { beforeEach, describe, expect, it, vitest } from 'vitest';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { Subject, from, of } from 'rxjs';

import { IOffice } from '../office.model';
import { OfficeService } from '../service/office.service';

import { OfficeFormService } from './office-form.service';
import { OfficeUpdate } from './office-update';

describe('Office Management Update Component', () => {
  let comp: OfficeUpdate;
  let fixture: ComponentFixture<OfficeUpdate>;
  let activatedRoute: ActivatedRoute;
  let officeFormService: OfficeFormService;
  let officeService: OfficeService;

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

    fixture = TestBed.createComponent(OfficeUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    officeFormService = TestBed.inject(OfficeFormService);
    officeService = TestBed.inject(OfficeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const office: IOffice = { id: 7490 };

      activatedRoute.data = of({ office });
      comp.ngOnInit();

      expect(comp.office).toEqual(office);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<IOffice>();
      const office = { id: 20465 };
      vitest.spyOn(officeFormService, 'getOffice').mockReturnValue(office);
      vitest.spyOn(officeService, 'update').mockReturnValue(saveSubject);
      vitest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ office });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(office);
      saveSubject.complete();

      // THEN
      expect(officeFormService.getOffice).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(officeService.update).toHaveBeenCalledWith(expect.objectContaining(office));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<IOffice>();
      const office = { id: 20465 };
      vitest.spyOn(officeFormService, 'getOffice').mockReturnValue({ id: null });
      vitest.spyOn(officeService, 'create').mockReturnValue(saveSubject);
      vitest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ office: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(office);
      saveSubject.complete();

      // THEN
      expect(officeFormService.getOffice).toHaveBeenCalled();
      expect(officeService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<IOffice>();
      const office = { id: 20465 };
      vitest.spyOn(officeService, 'update').mockReturnValue(saveSubject);
      vitest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ office });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(officeService.update).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
