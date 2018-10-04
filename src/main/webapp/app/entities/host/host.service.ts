import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IHost } from 'app/shared/model/host.model';

type EntityResponseType = HttpResponse<IHost>;
type EntityArrayResponseType = HttpResponse<IHost[]>;

@Injectable({ providedIn: 'root' })
export class HostService {
    private resourceUrl = SERVER_API_URL + 'api/hosts';

    constructor(private http: HttpClient) {}

    create(host: IHost): Observable<EntityResponseType> {
        return this.http.post<IHost>(this.resourceUrl, host, { observe: 'response' });
    }

    update(host: IHost): Observable<EntityResponseType> {
        return this.http.put<IHost>(this.resourceUrl, host, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<IHost>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IHost[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }
}
