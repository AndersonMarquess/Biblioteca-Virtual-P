import { Component, ElementRef, OnInit, Renderer } from '@angular/core';
import { Title } from '@angular/platform-browser';
import { Router } from '@angular/router';
import { UsuariosService } from 'src/app/usuarios/usuarios.service';
import { LivrosService } from '../livros.service';
import { LivroComContato } from 'src/app/compartilhados/models/livro-com-contato';

@Component({
	selector: 'bvp-listar-livros',
	templateUrl: './listar-livros.component.html',
	styleUrls: ['./listar-livros.component.css']
})
export class ListarLivrosComponent implements OnInit {

	todosOsLivros: Array<LivroComContato> = [];
	tituloPagina = "Listagem de livros";
	isModalAtivo = false;
	numPagina = 0;
	possuiMaisLivros = true;

	constructor(private livrosService: LivrosService, private usuarioService: UsuariosService,
		private elementRef: ElementRef, private renderer: Renderer, private router: Router, private title: Title) { }

	ngOnInit(): void {
		this.title.setTitle(this.tituloPagina);
		this.buscarMaisLivros();
	}

	buscarMaisLivros() {
		this.livrosService
			.buscarTodosOsLivrosComPaginacao(this.numPagina++, 5)
			.subscribe(
				livrosComContatos => {
					if (livrosComContatos && livrosComContatos.length > 0) {
						livrosComContatos.forEach(lc => this.todosOsLivros.push(lc))
					} else {
						this.possuiMaisLivros = false;
					}
				},
				err => console.log(err)
			);
	}

	exibirEOcultarModal(index: string): void {
		let elementoHTML = this.elementRef.nativeElement.querySelector("#modal-background" + index);
		let valorDisplay = this.isModalAtivo ? "none" : "flex";
		this.isModalAtivo = !this.isModalAtivo;
		this.renderer.setElementStyle(elementoHTML, "display", valorDisplay);
	}

	removerLivroComId(idLivro: string): void {
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