import { Moment } from 'moment';
import { IUser } from 'app/core/user/user.model';
import { IHost } from 'app/shared/model//host.model';

export const enum MicType {
    COMEDY = 'COMEDY',
    MUSIC = 'MUSIC',
    POETRY = 'POETRY',
    ANYTHING = 'ANYTHING'
}

export interface IMic {
    id?: number;
    name?: string;
    startDate?: Moment;
    endDate?: Moment;
    micTime?: number;
    duration?: number;
    isRecurring?: boolean;
    recurrencePattern?: string;
    streetAddress?: string;
    postalCode?: string;
    city?: string;
    notes?: string;
    micType?: MicType;
    user?: IUser;
    hosts?: IHost[];
}

export class Mic implements IMic {
    constructor(
        public id?: number,
        public name?: string,
        public startDate?: Moment,
        public endDate?: Moment,
        public micTime?: number,
        public duration?: number,
        public isRecurring?: boolean,
        public recurrencePattern?: string,
        public streetAddress?: string,
        public postalCode?: string,
        public city?: string,
        public notes?: string,
        public micType?: MicType,
        public user?: IUser,
        public hosts?: IHost[]
    ) {
        this.isRecurring = this.isRecurring || false;
    }
}
