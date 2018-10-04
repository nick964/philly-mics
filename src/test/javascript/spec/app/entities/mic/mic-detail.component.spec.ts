/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { OpenmicsTestModule } from '../../../test.module';
import { MicDetailComponent } from 'app/entities/mic/mic-detail.component';
import { Mic } from 'app/shared/model/mic.model';

describe('Component Tests', () => {
    describe('Mic Management Detail Component', () => {
        let comp: MicDetailComponent;
        let fixture: ComponentFixture<MicDetailComponent>;
        const route = ({ data: of({ mic: new Mic(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [OpenmicsTestModule],
                declarations: [MicDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(MicDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(MicDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.mic).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
