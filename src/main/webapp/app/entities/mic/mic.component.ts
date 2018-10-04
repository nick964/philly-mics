import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { IMic } from 'app/shared/model/mic.model';
import { Principal } from 'app/core';
import { MicService } from './mic.service';

@Component({
    selector: 'jhi-mic',
    templateUrl: './mic.component.html'
})
export class MicComponent implements OnInit, OnDestroy {
    mics: IMic[];
    currentAccount: any;
    eventSubscriber: Subscription;

    constructor(
        private micService: MicService,
        private jhiAlertService: JhiAlertService,
        private eventManager: JhiEventManager,
        private principal: Principal
    ) {}

    loadAll() {
        this.micService.query().subscribe(
            (res: HttpResponse<IMic[]>) => {
                this.mics = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    ngOnInit() {
        this.loadAll();
        this.principal.identity().then(account => {
            this.currentAccount = account;
        });
        this.registerChangeInMics();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: IMic) {
        return item.id;
    }

    registerChangeInMics() {
        this.eventSubscriber = this.eventManager.subscribe('micListModification', response => this.loadAll());
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
}
