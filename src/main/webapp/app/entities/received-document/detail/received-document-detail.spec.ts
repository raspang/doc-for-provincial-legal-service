import { beforeEach, describe, expect, it, vitest } from 'vitest';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';

import { FaIconLibrary } from '@fortawesome/angular-fontawesome';
import { faArrowLeft, faPencilAlt } from '@fortawesome/free-solid-svg-icons';
import { of } from 'rxjs';

import { ReceivedDocumentDetail } from './received-document-detail';

describe('ReceivedDocument Management Detail Component', () => {
  let comp: ReceivedDocumentDetail;
  let fixture: ComponentFixture<ReceivedDocumentDetail>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./received-document-detail').then(m => m.ReceivedDocumentDetail),
              resolve: { receivedDocument: () => of({ id: 17513 }) },
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
    fixture = TestBed.createComponent(ReceivedDocumentDetail);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load receivedDocument on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', ReceivedDocumentDetail);

      // THEN
      expect(instance.receivedDocument()).toEqual(expect.objectContaining({ id: 17513 }));
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
