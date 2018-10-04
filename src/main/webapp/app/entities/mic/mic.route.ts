import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { Mic } from 'app/shared/model/mic.model';
import { MicService } from './mic.service';
import { MicComponent } from './mic.component';
import { MicDetailComponent } from './mic-detail.component';
import { MicUpdateComponent } from './mic-update.component';
import { MicDeletePopupComponent } from './mic-delete-dialog.component';
import { IMic } from 'app/shared/model/mic.model';

@Injectable({ providedIn: 'root' })
export class MicResolve implements Resolve<IMic> {
    constructor(private service: MicService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((mic: HttpResponse<Mic>) => mic.body));
        }
        return of(new Mic());
    }
}

export const micRoute: Routes = [
    {
        path: 'mic',
        component: MicComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Mics'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'mic/:id/view',
        component: MicDetailComponent,
        resolve: {
            mic: MicResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Mics'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'mic/new',
        component: MicUpdateComponent,
        resolve: {
            mic: MicResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Mics'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'mic/:id/edit',
        component: MicUpdateComponent,
        resolve: {
            mic: MicResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Mics'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const micPopupRoute: Routes = [
    {
        path: 'mic/:id/delete',
        component: MicDeletePopupComponent,
        resolve: {
            mic: MicResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Mics'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
