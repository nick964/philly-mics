import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { IHost } from 'app/shared/model/host.model';
import { Principal } from 'app/core';
import { HostService } from './host.service';

@Component({
    selector: 'jhi-host',
    templateUrl: './host.component.html'
})
export class HostComponent implements OnInit, OnDestroy {
    hosts: IHost[];
    currentAccount: any;
    eventSubscriber: Subscription;

    constructor(
        private hostService: HostService,
        private jhiAlertService: JhiAlertService,
        private eventManager: JhiEventManager,
        private principal: Principal
    ) {}

    loadAll() {
        this.hostService.query().subscribe(
            (res: HttpResponse<IHost[]>) => {
                this.hosts = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    ngOnInit() {
        this.loadAll();
        this.principal.identity().then(account => {
            this.currentAccount = account;
        });
        this.registerChangeInHosts();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: IHost) {
        return item.id;
    }

    registerChangeInHosts() {
        this.eventSubscriber = this.eventManager.subscribe('hostListModification', response => this.loadAll());
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
}
