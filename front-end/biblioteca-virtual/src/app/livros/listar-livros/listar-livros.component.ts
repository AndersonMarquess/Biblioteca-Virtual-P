import { Component, OnInit } from '@angular/core';
import { Contato } from 'src/app/compartilhados/models/contato';
import { Livro } from 'src/app/compartilhados/models/livro';
import { LivroComContato } from 'src/app/compartilhados/models/livro-com-contato';
import { LivrosService } from '../livros.service';
import { UsuariosService } from 'src/app/usuarios/usuarios.service';

@Component({
	selector: 'bvp-listar-livros',
	templateUrl: './listar-livros.component.html'
})
export class ListarLivrosComponent implements OnInit {

	todosOsLivros: Array<LivroComContato> = [];

	constructor(private livrosService: LivrosService, private usuarioService: UsuariosService) { }

	ngOnInit(): void {
		this.livrosService
			.buscarTodosOsLivros()
			.subscribe(
				suc => {
					suc.forEach(livro => this.transformarEmLivroComContato(livro));
				},
				err => console.log(err)
			);
	}

	private transformarEmLivroComContato(livro: Livro): void {
		this.usuarioService
			.buscarContatoDoUsuario(livro.idDonoLivro)
			.subscribe((suc: Contato[]) => {
				const contatos = suc;
				this.todosOsLivros.push(new LivroComContato(livro, contatos));
			});
	}
}