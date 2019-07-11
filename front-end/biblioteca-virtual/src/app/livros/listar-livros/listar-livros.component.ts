import { Component, ElementRef, HostListener, OnInit, Renderer } from '@angular/core';
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

	constructor(private livrosService: LivrosService, private usuarioService: UsuariosService,
		private elementRef: ElementRef, private renderer: Renderer) { }

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

	private mostrarModal(index: string): void {
		let elementoHTML = this.elementRef.nativeElement.querySelector("#modal-background" + index);
		this.renderer.setElementStyle(elementoHTML, "display", "flex");
	}

	private esconderModal(index: string): void {
		let elementoHTML = this.elementRef.nativeElement.querySelector("#modal-background" + index);
		this.renderer.setElementStyle(elementoHTML, "display", "none");
	}
}