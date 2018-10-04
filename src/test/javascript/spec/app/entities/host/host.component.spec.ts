/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Observable, of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { OpenmicsTestModule } from '../../../test.module';
import { HostComponent } from 'app/entities/host/host.component';
import { HostService } from 'app/entities/host/host.service';
import { Host } from 'app/shared/model/host.model';

describe('Component Tests', () => {
    describe('Host Management Component', () => {
        let comp: HostComponent;
        let fixture: ComponentFixture<HostComponent>;
        let service: HostService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [OpenmicsTestModule],
                declarations: [HostComponent],
                providers: []
            })
                .overrideTemplate(HostComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(HostComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(HostService);
        });

        it('Should call load all on init', () => {
            // GIVEN
            const headers = new HttpHeaders().append('link', 'link;link');
            spyOn(service, 'query').and.returnValue(
                of(
                    new HttpResponse({
                        body: [new Host(123)],
                        headers
                    })
                )
            );

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.query).toHaveBeenCalled();
            expect(comp.hosts[0]).toEqual(jasmine.objectContaining({ id: 123 }));
        });
    });
});
