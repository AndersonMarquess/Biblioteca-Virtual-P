import { Component, ElementRef, OnInit, Renderer } from '@angular/core';
import { Title } from '@angular/platform-browser';
import { Router } from '@angular/router';
import { Contato } from 'src/app/compartilhados/models/contato';
import { Livro } from 'src/app/compartilhados/models/livro';
import { LivroComContato } from 'src/app/compartilhados/models/livro-com-contato';
import { UsuariosService } from 'src/app/usuarios/usuarios.service';
import { LivrosService } from '../livros.service';

@Component({
	selector: 'bvp-listar-livros',
	templateUrl: './listar-livros.component.html',
	styleUrls: ['./listar-livros.component.css']
})
export class ListarLivrosComponent implements OnInit {

	todosOsLivros: Array<LivroComContato> = [];
	tituloPagina = "Listagem de livros";
	isModalAtivo = false;

	constructor(private livrosService: LivrosService, private usuarioService: UsuariosService,
		private elementRef: ElementRef, private renderer: Renderer, private router: Router, private title: Title) { }

	ngOnInit(): void {
		this.title.setTitle(this.tituloPagina);
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

	private exibirEOcultarModal(index: string): void {
		let elementoHTML = this.elementRef.nativeElement.querySelector("#modal-background" + index);
		let valorDisplay = this.isModalAtivo ? "none" : "flex";
		this.isModalAtivo = !this.isModalAtivo;
		this.renderer.setElementStyle(elementoHTML, "display", valorDisplay);
	}

	private removerLivroComId(idLivro: string): void {
		this.livrosService
			.removerLivroComId(idLivro)
			.subscribe(
				sucesso => this.router.navigate(['']),
				erro => {
					console.log(erro.message);
					this.router.navigate(['/livros', 'todos']);
				}
			);
	}
}