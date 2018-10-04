import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { Host } from 'app/shared/model/host.model';
import { HostService } from './host.service';
import { HostComponent } from './host.component';
import { HostDetailComponent } from './host-detail.component';
import { HostUpdateComponent } from './host-update.component';
import { HostDeletePopupComponent } from './host-delete-dialog.component';
import { IHost } from 'app/shared/model/host.model';

@Injectable({ providedIn: 'root' })
export class HostResolve implements Resolve<IHost> {
    constructor(private service: HostService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((host: HttpResponse<Host>) => host.body));
        }
        return of(new Host());
    }
}

export const hostRoute: Routes = [
    {
        path: 'host',
        component: HostComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Hosts'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'host/:id/view',
        component: HostDetailComponent,
        resolve: {
            host: HostResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Hosts'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'host/new',
        component: HostUpdateComponent,
        resolve: {
            host: HostResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Hosts'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'host/:id/edit',
        component: HostUpdateComponent,
        resolve: {
            host: HostResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Hosts'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const hostPopupRoute: Routes = [
    {
        path: 'host/:id/delete',
        component: HostDeletePopupComponent,
        resolve: {
            host: HostResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Hosts'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
