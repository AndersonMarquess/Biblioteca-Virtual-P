import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Title } from '@angular/platform-browser';
import { ActivatedRoute, Router } from '@angular/router';
import { Observable } from 'rxjs';
import { Livro } from 'src/app/compartilhados/models/livro';
import { UsuariosService } from 'src/app/usuarios/usuarios.service';
import { LivrosService } from '../livros.service';

@Component({
	selector: 'bvp-form-novo-livro',
	templateUrl: 'form-livro.component.html'
})
export class FormLivroComponent implements OnInit {

	formNovoLivro: FormGroup;
	tituloPagina = "Compartilhar livro";
	idDonoLivro: string;
	idLivro: string;
	livro: Livro;

	constructor(private formBuilder: FormBuilder, private title: Title, private activatedRoute: ActivatedRoute,
		private router: Router, private livrosService: LivrosService, private usuarioService: UsuariosService) { }

	ngOnInit(): void {
		this.idDonoLivro = this.activatedRoute.snapshot.params.idDono;
		this.idLivro = this.activatedRoute.snapshot.params.idLivro;

		if (this.idLivro) {
			this.tituloPagina = "Editar livro";
			this.recuperarLivroParaEdicao();
		}

		this.montarFormulario();
		this.title.setTitle(this.tituloPagina);
	}

	private recuperarLivroParaEdicao(): void {
		this.livrosService.buscarLivrosDoUsuario(this.idDonoLivro)
			.subscribe(livros => {
				this.livro = livros.find(l => l.id == this.idLivro);
				if (this.livro)
					this.adicionarDadosDoLivroAoFormulario();
			});
	}

	private adicionarDadosDoLivroAoFormulario(): void {
		this.formNovoLivro.setValue({
			"isbn": this.livro.isbn,
			"titulo": this.livro.titulo,
			"descricao": this.livro.descricao,
			"urlCapa": this.livro.urlCapa,
			"categorias": this.livro.categorias[0].nome,
		});
	}

	private montarFormulario(): void {
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
		this.montarEAdicionarObjCategorias(livro);

		if (this.idLivro) {
			livro.id = this.idLivro;
			console.log(`[Edição] Montou o livro ${JSON.stringify(livro)}`);
			this.enviarDadosDoLivro(this.livrosService.atualizar(livro));
		} else {
			console.log(`[Cadastro] Montou o livro ${JSON.stringify(livro)}`);
			this.enviarDadosDoLivro(this.livrosService.cadastrar(livro));
		}
	}

	private montarEAdicionarObjCategorias(livro: Livro) {
		livro.categorias = [{ id: null, nome: this.formNovoLivro.get("categorias").value }];
	}

	private enviarDadosDoLivro(operacao: Observable<Livro>): void {
		operacao.subscribe(
			() => {
				console.log("Livro compartilhado com sucesso");
				this.router.navigate(['/livros', 'todos']);
			},
			() => console.log("Erro ao tentar salvar livro")
		);
	}

	private adicionarIdDoDono(livro: Livro): void {
		if (this.idDonoLivro) {
			livro.idDonoLivro = this.idDonoLivro;
		} else {
			this.usuarioService.getUsuarioLogado()
				.subscribe(userResp => {
					if (userResp != null)
						livro.idDonoLivro = userResp.id;
				});
		}
	}
}