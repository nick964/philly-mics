/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { OpenmicsTestModule } from '../../../test.module';
import { HostDetailComponent } from 'app/entities/host/host-detail.component';
import { Host } from 'app/shared/model/host.model';

describe('Component Tests', () => {
    describe('Host Management Detail Component', () => {
        let comp: HostDetailComponent;
        let fixture: ComponentFixture<HostDetailComponent>;
        const route = ({ data: of({ host: new Host(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [OpenmicsTestModule],
                declarations: [HostDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(HostDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(HostDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.host).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
