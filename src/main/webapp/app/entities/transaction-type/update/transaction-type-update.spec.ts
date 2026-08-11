import { beforeEach, describe, expect, it, vitest } from 'vitest';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { Subject, from, of } from 'rxjs';

import { TransactionTypeService } from '../service/transaction-type.service';
import { ITransactionType } from '../transaction-type.model';

import { TransactionTypeFormService } from './transaction-type-form.service';
import { TransactionTypeUpdate } from './transaction-type-update';

describe('TransactionType Management Update Component', () => {
  let comp: TransactionTypeUpdate;
  let fixture: ComponentFixture<TransactionTypeUpdate>;
  let activatedRoute: ActivatedRoute;
  let transactionTypeFormService: TransactionTypeFormService;
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

    fixture = TestBed.createComponent(TransactionTypeUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    transactionTypeFormService = TestBed.inject(TransactionTypeFormService);
    transactionTypeService = TestBed.inject(TransactionTypeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const transactionType: ITransactionType = { id: 32159 };

      activatedRoute.data = of({ transactionType });
      comp.ngOnInit();

      expect(comp.transactionType).toEqual(transactionType);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<ITransactionType>();
      const transactionType = { id: 4045 };
      vitest.spyOn(transactionTypeFormService, 'getTransactionType').mockReturnValue(transactionType);
      vitest.spyOn(transactionTypeService, 'update').mockReturnValue(saveSubject);
      vitest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ transactionType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(transactionType);
      saveSubject.complete();

      // THEN
      expect(transactionTypeFormService.getTransactionType).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(transactionTypeService.update).toHaveBeenCalledWith(expect.objectContaining(transactionType));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<ITransactionType>();
      const transactionType = { id: 4045 };
      vitest.spyOn(transactionTypeFormService, 'getTransactionType').mockReturnValue({ id: null });
      vitest.spyOn(transactionTypeService, 'create').mockReturnValue(saveSubject);
      vitest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ transactionType: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(transactionType);
      saveSubject.complete();

      // THEN
      expect(transactionTypeFormService.getTransactionType).toHaveBeenCalled();
      expect(transactionTypeService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<ITransactionType>();
      const transactionType = { id: 4045 };
      vitest.spyOn(transactionTypeService, 'update').mockReturnValue(saveSubject);
      vitest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ transactionType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(transactionTypeService.update).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
