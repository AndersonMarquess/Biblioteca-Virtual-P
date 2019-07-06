import { Categoria } from './categoria';
import { Contato } from './contato';
import { Livro } from './livro';

export class LivroComContato {

	private livro: Livro;
	private contatos: Array<Contato>;

	constructor(livro: Livro, contatos: Array<Contato>) {
		this.livro = livro;
		this.contatos = contatos;
	}

	getIsbn(): string {
		return this.livro.isbn;
	}

	getTitulo(): string {
		return this.livro.titulo;
	}

	getDescricao(): string {
		return this.livro.descricao;
	}

	getCategorias(): Array<Categoria> {
		return this.livro.categorias;
	}

	getContatos(): Array<Contato> {
		return this.contatos;
	}
}