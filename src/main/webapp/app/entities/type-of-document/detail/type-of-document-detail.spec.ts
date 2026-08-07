import { beforeEach, describe, expect, it, vitest } from 'vitest';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';

import { FaIconLibrary } from '@fortawesome/angular-fontawesome';
import { faArrowLeft, faPencilAlt } from '@fortawesome/free-solid-svg-icons';
import { of } from 'rxjs';

import { TypeOfDocumentDetail } from './type-of-document-detail';

describe('TypeOfDocument Management Detail Component', () => {
  let comp: TypeOfDocumentDetail;
  let fixture: ComponentFixture<TypeOfDocumentDetail>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./type-of-document-detail').then(m => m.TypeOfDocumentDetail),
              resolve: { typeOfDocument: () => of({ id: 8066 }) },
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
    fixture = TestBed.createComponent(TypeOfDocumentDetail);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load typeOfDocument on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', TypeOfDocumentDetail);

      // THEN
      expect(instance.typeOfDocument()).toEqual(expect.objectContaining({ id: 8066 }));
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
