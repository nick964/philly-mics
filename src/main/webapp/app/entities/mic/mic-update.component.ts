import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { JhiAlertService } from 'ng-jhipster';

import { IMic } from 'app/shared/model/mic.model';
import { MicService } from './mic.service';
import { IUser, UserService } from 'app/core';
import { IHost } from 'app/shared/model/host.model';
import { HostService } from 'app/entities/host';

@Component({
    selector: 'jhi-mic-update',
    templateUrl: './mic-update.component.html'
})
export class MicUpdateComponent implements OnInit {
    private _mic: IMic;
    isSaving: boolean;

    users: IUser[];

    hosts: IHost[];
    startDate: string;
    endDate: string;

    constructor(
        private jhiAlertService: JhiAlertService,
        private micService: MicService,
        private userService: UserService,
        private hostService: HostService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ mic }) => {
            this.mic = mic;
        });
        this.userService.query().subscribe(
            (res: HttpResponse<IUser[]>) => {
                this.users = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
        this.hostService.query().subscribe(
            (res: HttpResponse<IHost[]>) => {
                this.hosts = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        this.mic.startDate = moment(this.startDate, DATE_TIME_FORMAT);
        this.mic.endDate = moment(this.endDate, DATE_TIME_FORMAT);
        if (this.mic.id !== undefined) {
            this.subscribeToSaveResponse(this.micService.update(this.mic));
        } else {
            this.subscribeToSaveResponse(this.micService.create(this.mic));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IMic>>) {
        result.subscribe((res: HttpResponse<IMic>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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

    trackUserById(index: number, item: IUser) {
        return item.id;
    }

    trackHostById(index: number, item: IHost) {
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
    get mic() {
        return this._mic;
    }

    set mic(mic: IMic) {
        this._mic = mic;
        this.startDate = moment(mic.startDate).format(DATE_TIME_FORMAT);
        this.endDate = moment(mic.endDate).format(DATE_TIME_FORMAT);
    }
}
