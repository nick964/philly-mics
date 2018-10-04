import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IMic } from 'app/shared/model/mic.model';

@Component({
    selector: 'jhi-mic-detail',
    templateUrl: './mic-detail.component.html'
})
export class MicDetailComponent implements OnInit {
    mic: IMic;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ mic }) => {
            this.mic = mic;
        });
    }

    previousState() {
        window.history.back();
    }
}
