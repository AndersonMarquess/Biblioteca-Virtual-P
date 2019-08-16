import { Categoria } from './categoria';
import { Contato } from './contato';

export interface LivroComContato {
	id: string,
	isbn: string,
	titulo: string,
	descricao: string,
	urlCapa: string,
	categorias: Array<Categoria>,
	idDonoLivro: string,
	contatos: Array<Contato>
}