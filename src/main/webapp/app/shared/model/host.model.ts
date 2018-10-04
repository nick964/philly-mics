import { IMic } from 'app/shared/model//mic.model';

export interface IHost {
    id?: number;
    hostId?: number;
    name?: string;
    email?: string;
    phoneNumber?: string;
    mics?: IMic[];
}

export class Host implements IHost {
    constructor(
        public id?: number,
        public hostId?: number,
        public name?: string,
        public email?: string,
        public phoneNumber?: string,
        public mics?: IMic[]
    ) {}
}
