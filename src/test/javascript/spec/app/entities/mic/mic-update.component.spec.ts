/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { OpenmicsTestModule } from '../../../test.module';
import { MicUpdateComponent } from 'app/entities/mic/mic-update.component';
import { MicService } from 'app/entities/mic/mic.service';
import { Mic } from 'app/shared/model/mic.model';

describe('Component Tests', () => {
    describe('Mic Management Update Component', () => {
        let comp: MicUpdateComponent;
        let fixture: ComponentFixture<MicUpdateComponent>;
        let service: MicService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [OpenmicsTestModule],
                declarations: [MicUpdateComponent]
            })
                .overrideTemplate(MicUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(MicUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(MicService);
        });

        describe('save', () => {
            it(
                'Should call update service on save for existing entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new Mic(123);
                    spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.mic = entity;
                    // WHEN
                    comp.save();
                    tick(); // simulate async

                    // THEN
                    expect(service.update).toHaveBeenCalledWith(entity);
                    expect(comp.isSaving).toEqual(false);
                })
            );

            it(
                'Should call create service on save for new entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new Mic();
                    spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.mic = entity;
                    // WHEN
                    comp.save();
                    tick(); // simulate async

                    // THEN
                    expect(service.create).toHaveBeenCalledWith(entity);
                    expect(comp.isSaving).toEqual(false);
                })
            );
        });
    });
});
