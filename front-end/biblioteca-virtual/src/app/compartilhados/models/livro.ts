import { Categoria } from './categoria';

export interface Livro {
	id: string,
	isbn: string,
	titulo: string,
	descricao: string,
	urlCapa: string,
	categorias: Array<Categoria>,
	idDonoLivro: string
}