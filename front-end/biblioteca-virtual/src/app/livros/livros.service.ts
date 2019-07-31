import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Livro } from '../compartilhados/models/livro';

const API_URL = "http://localhost:8080";

@Injectable()
export class LivrosService {

	constructor(private httpClient: HttpClient) { }

	buscarTodosOsLivros(): Observable<Array<Livro>> {
		const endereco = API_URL + "/v1/livro/all";
		return this.httpClient.get<Array<Livro>>(endereco);
	}

	buscarLivrosDoUsuario(idDonoLivro: string): Observable<Array<Livro>> {
		const endereco = API_URL + "/v1/livro/all/" + idDonoLivro;
		return this.httpClient.get<Array<Livro>>(endereco);
	}

	removerLivroComId(idLivro: string): Observable<any> {
		const endereco = API_URL + "/v1/livro/" + idLivro;
		return this.httpClient.delete(endereco);
	}

	cadastrar(livro: Livro): Observable<Livro> {
		const endereco = API_URL + "/v1/livro";
		return this.httpClient.post<Livro>(endereco, livro);
	}

	atualizar(livro: Livro): Observable<Livro> {
		const endereco = API_URL + "/v1/livro";
		return this.httpClient.put<Livro>(endereco, livro);
	}
}