import { Injectable } from '@angular/core';
import { Http, Response, URLSearchParams, BaseRequestOptions } from '@angular/http';
import { Observable } from 'rxjs/Rx';

import { Organizacao } from './organizacao.model';
import { Contrato } from '../contrato/contrato.model';

@Injectable()
export class OrganizacaoService {

    private resourceUrl = 'api/organizacaos';
    private resourceSearchUrl = 'api/_search/organizacaos';


    public idOrganizacaoParaInvocarModal;
    public contrato: Contrato;

    constructor(private http: Http) { }

    create(organizacao: Organizacao): Observable<Organizacao> {
        let copy: Organizacao = Object.assign({}, organizacao);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            return res.json();
        });
    }

    update(organizacao: Organizacao): Observable<Organizacao> {
        let copy: Organizacao = Object.assign({}, organizacao);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            return res.json();
        });
    }

    find(id: number): Observable<Organizacao> {
        return this.http.get(`${this.resourceUrl}/${id}`).map((res: Response) => {
            return res.json();
        });
    }

    query(req?: any): Observable<Response> {
        let options = this.createRequestOption(req);
        return this.http.get(this.resourceUrl, options)
            ;
    }

    delete(id: number): Observable<Response> {
        return this.http.delete(`${this.resourceUrl}/${id}`);
    }

    search(req?: any): Observable<Response> {
        let options = this.createRequestOption(req);
        return this.http.get(this.resourceSearchUrl, options)
            ;
    }


    private createRequestOption(req?: any): BaseRequestOptions {
        let options: BaseRequestOptions = new BaseRequestOptions();
        if (req) {
            let params: URLSearchParams = new URLSearchParams();
            params.set('page', req.page);
            params.set('size', req.size);
            if (req.sort) {
                params.paramsMap.set('sort', req.sort);
            }
            params.set('query', req.query);

            options.search = params;
        }
        return options;
    }
}
