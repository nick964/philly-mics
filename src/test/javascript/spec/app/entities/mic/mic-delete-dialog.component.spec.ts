/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { OpenmicsTestModule } from '../../../test.module';
import { MicDeleteDialogComponent } from 'app/entities/mic/mic-delete-dialog.component';
import { MicService } from 'app/entities/mic/mic.service';

describe('Component Tests', () => {
    describe('Mic Management Delete Component', () => {
        let comp: MicDeleteDialogComponent;
        let fixture: ComponentFixture<MicDeleteDialogComponent>;
        let service: MicService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [OpenmicsTestModule],
                declarations: [MicDeleteDialogComponent]
            })
                .overrideTemplate(MicDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(MicDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(MicService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('confirmDelete', () => {
            it('Should call delete service on confirmDelete', inject(
                [],
                fakeAsync(() => {
                    // GIVEN
                    spyOn(service, 'delete').and.returnValue(of({}));

                    // WHEN
                    comp.confirmDelete(123);
                    tick();

                    // THEN
                    expect(service.delete).toHaveBeenCalledWith(123);
                    expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
                })
            ));
        });
    });
});
