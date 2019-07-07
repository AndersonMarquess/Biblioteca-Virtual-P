import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

const API_URL = "http://localhost:8080";

@Injectable()
export class UsuariosService {

	constructor(private httpClient: HttpClient) { }

	buscarContatoDoUsuario(idUsuario: string): Observable<any> {
		const endereco = API_URL + "/v1/usuario/contatos/" + idUsuario;
		return this.httpClient.get(endereco);
	}
}