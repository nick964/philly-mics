import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IMic } from 'app/shared/model/mic.model';

type EntityResponseType = HttpResponse<IMic>;
type EntityArrayResponseType = HttpResponse<IMic[]>;

@Injectable({ providedIn: 'root' })
export class MicService {
    private resourceUrl = SERVER_API_URL + 'api/mics';

    constructor(private http: HttpClient) {}

    create(mic: IMic): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(mic);
        return this.http
            .post<IMic>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(mic: IMic): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(mic);
        return this.http
            .put<IMic>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http
            .get<IMic>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IMic[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    private convertDateFromClient(mic: IMic): IMic {
        const copy: IMic = Object.assign({}, mic, {
            startDate: mic.startDate != null && mic.startDate.isValid() ? mic.startDate.toJSON() : null,
            endDate: mic.endDate != null && mic.endDate.isValid() ? mic.endDate.toJSON() : null
        });
        return copy;
    }

    private convertDateFromServer(res: EntityResponseType): EntityResponseType {
        res.body.startDate = res.body.startDate != null ? moment(res.body.startDate) : null;
        res.body.endDate = res.body.endDate != null ? moment(res.body.endDate) : null;
        return res;
    }

    private convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        res.body.forEach((mic: IMic) => {
            mic.startDate = mic.startDate != null ? moment(mic.startDate) : null;
            mic.endDate = mic.endDate != null ? moment(mic.endDate) : null;
        });
        return res;
    }
}
