import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JhiAlertService } from 'ng-jhipster';

import { IHost } from 'app/shared/model/host.model';
import { HostService } from './host.service';
import { IMic } from 'app/shared/model/mic.model';
import { MicService } from 'app/entities/mic';

@Component({
    selector: 'jhi-host-update',
    templateUrl: './host-update.component.html'
})
export class HostUpdateComponent implements OnInit {
    private _host: IHost;
    isSaving: boolean;

    mics: IMic[];

    constructor(
        private jhiAlertService: JhiAlertService,
        private hostService: HostService,
        private micService: MicService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ host }) => {
            this.host = host;
        });
        this.micService.query().subscribe(
            (res: HttpResponse<IMic[]>) => {
                this.mics = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.host.id !== undefined) {
            this.subscribeToSaveResponse(this.hostService.update(this.host));
        } else {
            this.subscribeToSaveResponse(this.hostService.create(this.host));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IHost>>) {
        result.subscribe((res: HttpResponse<IHost>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    trackMicById(index: number, item: IMic) {
        return item.id;
    }

    getSelected(selectedVals: Array<any>, option: any) {
        if (selectedVals) {
            for (let i = 0; i < selectedVals.length; i++) {
                if (option.id === selectedVals[i].id) {
                    return selectedVals[i];
                }
            }
        }
        return option;
    }
    get host() {
        return this._host;
    }

    set host(host: IHost) {
        this._host = host;
    }
}
