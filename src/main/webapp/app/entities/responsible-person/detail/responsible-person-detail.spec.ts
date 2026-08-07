import { beforeEach, describe, expect, it, vitest } from 'vitest';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';

import { FaIconLibrary } from '@fortawesome/angular-fontawesome';
import { faArrowLeft, faPencilAlt } from '@fortawesome/free-solid-svg-icons';
import { of } from 'rxjs';

import { ResponsiblePersonDetail } from './responsible-person-detail';

describe('ResponsiblePerson Management Detail Component', () => {
  let comp: ResponsiblePersonDetail;
  let fixture: ComponentFixture<ResponsiblePersonDetail>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./responsible-person-detail').then(m => m.ResponsiblePersonDetail),
              resolve: { responsiblePerson: () => of({ id: 29109 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    });
    const library = TestBed.inject(FaIconLibrary);
    library.addIcons(faArrowLeft);
    library.addIcons(faPencilAlt);
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ResponsiblePersonDetail);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load responsiblePerson on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', ResponsiblePersonDetail);

      // THEN
      expect(instance.responsiblePerson()).toEqual(expect.objectContaining({ id: 29109 }));
    });
  });

  describe('PreviousState', () => {
    it('should navigate to previous state', () => {
      vitest.spyOn(globalThis.history, 'back');
      comp.previousState();
      expect(globalThis.history.back).toHaveBeenCalled();
    });
  });
});
