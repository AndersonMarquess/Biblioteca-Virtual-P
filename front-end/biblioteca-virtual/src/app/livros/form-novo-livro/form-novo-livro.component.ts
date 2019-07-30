import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Title } from '@angular/platform-browser';
import { Router } from '@angular/router';
import { Livro } from 'src/app/compartilhados/models/livro';
import { UsuariosService } from 'src/app/usuarios/usuarios.service';
import { LivrosService } from '../livros.service';

@Component({
	selector: 'bvp-form-novo-livro',
	templateUrl: 'form-novo-livro.component.html'
})
export class FormNovoLivroComponent implements OnInit {

	formNovoLivro: FormGroup;
	tituloPagina = "Compartilhar livro";

	constructor(private formBuilder: FormBuilder, private livrosService: LivrosService,
		private title: Title, private router: Router, private usuarioService: UsuariosService) { }

	ngOnInit(): void {
		this.title.setTitle(this.tituloPagina);
		this.formNovoLivro = this.formBuilder.group({
			isbn: ['', [
				Validators.required,
				Validators.pattern('^[0-9]*$'),
				Validators.maxLength(13)
			]],
			titulo: ['', [
				Validators.required,
				Validators.minLength(5),
				Validators.maxLength(150)
			]],
			descricao: ['',
				Validators.maxLength(150)
			],
			urlCapa: ['', [
				Validators.required,
				Validators.maxLength(250)
			]],
			categorias: ['',
				Validators.required
			]
		});
	}

	montarECompartilharLivro(): void {
		const livro = this.formNovoLivro.getRawValue() as Livro;
		this.adicionarIdDoDono(livro);
		livro.categorias = [{ id: null, nome: this.formNovoLivro.get("categorias").value }];
		console.log(`Montou o livro ${JSON.stringify(livro)}`);

		this.livrosService.cadastrar(livro)
			.subscribe(
				() => {
					console.log("Cadastrado com sucesso");
					this.router.navigate(['/livros', 'all']);
				},
				() => console.log("Erro ao tentar salvar livro")
			);
	}

	private adicionarIdDoDono(livro: Livro): void {
		this.usuarioService.getUsuarioLogado()
			.subscribe(userResp => {
				if (userResp != null)
					livro.idDonoLivro = userResp.id;
			});
	}
}