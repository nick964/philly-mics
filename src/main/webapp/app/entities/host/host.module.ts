import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { OpenmicsSharedModule } from 'app/shared';
import {
    HostComponent,
    HostDetailComponent,
    HostUpdateComponent,
    HostDeletePopupComponent,
    HostDeleteDialogComponent,
    hostRoute,
    hostPopupRoute
} from './';

const ENTITY_STATES = [...hostRoute, ...hostPopupRoute];

@NgModule({
    imports: [OpenmicsSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [HostComponent, HostDetailComponent, HostUpdateComponent, HostDeleteDialogComponent, HostDeletePopupComponent],
    entryComponents: [HostComponent, HostUpdateComponent, HostDeleteDialogComponent, HostDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class OpenmicsHostModule {}
