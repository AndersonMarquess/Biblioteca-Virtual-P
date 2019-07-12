import { Contato } from './contato';

export interface Usuario {
	id: string,
	nome: string,
	senha: string,
	email: string,
	contatos: Array<Contato>
}