/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Observable, of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { OpenmicsTestModule } from '../../../test.module';
import { MicComponent } from 'app/entities/mic/mic.component';
import { MicService } from 'app/entities/mic/mic.service';
import { Mic } from 'app/shared/model/mic.model';

describe('Component Tests', () => {
    describe('Mic Management Component', () => {
        let comp: MicComponent;
        let fixture: ComponentFixture<MicComponent>;
        let service: MicService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [OpenmicsTestModule],
                declarations: [MicComponent],
                providers: []
            })
                .overrideTemplate(MicComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(MicComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(MicService);
        });

        it('Should call load all on init', () => {
            // GIVEN
            const headers = new HttpHeaders().append('link', 'link;link');
            spyOn(service, 'query').and.returnValue(
                of(
                    new HttpResponse({
                        body: [new Mic(123)],
                        headers
                    })
                )
            );

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.query).toHaveBeenCalled();
            expect(comp.mics[0]).toEqual(jasmine.objectContaining({ id: 123 }));
        });
    });
});
