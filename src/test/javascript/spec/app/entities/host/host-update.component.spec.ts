/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { OpenmicsTestModule } from '../../../test.module';
import { HostUpdateComponent } from 'app/entities/host/host-update.component';
import { HostService } from 'app/entities/host/host.service';
import { Host } from 'app/shared/model/host.model';

describe('Component Tests', () => {
    describe('Host Management Update Component', () => {
        let comp: HostUpdateComponent;
        let fixture: ComponentFixture<HostUpdateComponent>;
        let service: HostService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [OpenmicsTestModule],
                declarations: [HostUpdateComponent]
            })
                .overrideTemplate(HostUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(HostUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(HostService);
        });

        describe('save', () => {
            it(
                'Should call update service on save for existing entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new Host(123);
                    spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.host = entity;
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
                    const entity = new Host();
                    spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.host = entity;
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
