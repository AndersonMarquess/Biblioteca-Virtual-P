import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Livro } from 'src/app/compartilhados/models/livro';

@Component({
	selector: 'bvp-form-novo-livro',
	templateUrl: 'form-novo-livro.component.html'
})
export class FormNovoLivroComponent implements OnInit {

	formNovoLivro: FormGroup;
	tituloPagina = "Compartilhar livro";

	constructor(private formBuilder: FormBuilder) { }

	ngOnInit(): void {
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
		console.log(`Montou o livro: ${livro.isbn} ${livro.titulo} ${livro.categorias}`);
	}
}