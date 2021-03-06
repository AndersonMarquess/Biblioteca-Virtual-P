import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Livro } from '../compartilhados/models/livro';
import { environment } from '../../environments/environment';
import { LivroComContato } from '../compartilhados/models/livro-com-contato';

const API_URL = environment.ApiUrl;

@Injectable()
export class LivrosService {

	constructor(private httpClient: HttpClient) { }

	buscarTodosOsLivros(): Observable<Array<Livro>> {
		const endereco = API_URL + "/v1/livro/all";
		return this.httpClient.get<Array<Livro>>(endereco);
	}

	buscarTodosOsLivrosComPaginacao(index: number, size: number): Observable<Array<LivroComContato>> {
		const endereco = `${API_URL}/v1/livro/allpg?page=${index}&size=${size}`;
		return this.httpClient.get<Array<LivroComContato>>(endereco);
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