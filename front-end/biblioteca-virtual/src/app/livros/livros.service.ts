import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

const API_URL = "http://localhost:8080";

@Injectable()
export class LivrosService {

	constructor(private httpClient: HttpClient) { }

	buscarTodosOsLivros(): Observable<any> {
		const endereco = API_URL + "/v1/livro/all";
		return this.httpClient.get(endereco);
	}

	removerLivroComId(idLivro: string): Observable<any> {
		const endereco = API_URL + "/v1/livro/" + idLivro;
		return this.httpClient.delete(endereco);
	}
}