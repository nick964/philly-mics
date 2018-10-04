import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { OpenmicsSharedModule } from 'app/shared';
import { OpenmicsAdminModule } from 'app/admin/admin.module';
import {
    MicComponent,
    MicDetailComponent,
    MicUpdateComponent,
    MicDeletePopupComponent,
    MicDeleteDialogComponent,
    micRoute,
    micPopupRoute
} from './';

const ENTITY_STATES = [...micRoute, ...micPopupRoute];

@NgModule({
    imports: [OpenmicsSharedModule, OpenmicsAdminModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [MicComponent, MicDetailComponent, MicUpdateComponent, MicDeleteDialogComponent, MicDeletePopupComponent],
    entryComponents: [MicComponent, MicUpdateComponent, MicDeleteDialogComponent, MicDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class OpenmicsMicModule {}
