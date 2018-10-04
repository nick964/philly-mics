import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IHost } from 'app/shared/model/host.model';

@Component({
    selector: 'jhi-host-detail',
    templateUrl: './host-detail.component.html'
})
export class HostDetailComponent implements OnInit {
    host: IHost;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ host }) => {
            this.host = host;
        });
    }

    previousState() {
        window.history.back();
    }
}
