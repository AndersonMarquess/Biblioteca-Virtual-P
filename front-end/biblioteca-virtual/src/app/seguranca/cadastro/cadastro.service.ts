import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

const API_URL = "http://localhost:8080"

@Injectable()
export class CadastroService {

	constructor(private httpClient: HttpClient) { }

	cadastrar(obj: any): Observable<any> {
		const endereco = API_URL + "/v1/usuario";
		return this.httpClient.post(endereco, obj);
	}
}