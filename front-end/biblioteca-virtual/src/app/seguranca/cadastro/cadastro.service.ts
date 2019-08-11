import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';

const API_URL = environment.ApiUrl;

@Injectable()
export class CadastroService {

	constructor(private httpClient: HttpClient) { }

	cadastrar(obj: any): Observable<any> {
		const endereco = API_URL + "/v1/usuario";
		return this.httpClient.post(endereco, obj);
	}
}