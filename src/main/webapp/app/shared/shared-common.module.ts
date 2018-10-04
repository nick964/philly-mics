import { NgModule } from '@angular/core';

import { OpenmicsSharedLibsModule, JhiAlertComponent, JhiAlertErrorComponent } from './';

@NgModule({
    imports: [OpenmicsSharedLibsModule],
    declarations: [JhiAlertComponent, JhiAlertErrorComponent],
    exports: [OpenmicsSharedLibsModule, JhiAlertComponent, JhiAlertErrorComponent]
})
export class OpenmicsSharedCommonModule {}
