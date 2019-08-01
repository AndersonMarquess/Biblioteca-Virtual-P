import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { CadastroService } from './cadastro.service';

@Component({
	selector: 'bvp-cadastro',
	templateUrl: './cadastro.component.html'
})
export class CadastroComponent implements OnInit {

	formCadastro: FormGroup;
	erroCadastro = false;

	constructor(private formBuilder: FormBuilder, private cadastroService: CadastroService,
		private router: Router) { }

	ngOnInit(): void {
		this.formCadastro = this.formBuilder.group({
			nome: ['', [
				Validators.required,
				Validators.maxLength(200)
			]],
			senha: ['', [
				Validators.required,
				Validators.minLength(3),
				Validators.maxLength(80)
			]],
			email: ['', [
				Validators.required,
				Validators.email,
				Validators.maxLength(200)
			]],
			endereco: ['', [
				Validators.required,
				Validators.maxLength(200)
			]],
			tipo: ['', [
				Validators.required,
				Validators.max(2)
			]]
		});
	}

	cadastrar(): void {
		const novoUsuario = this.montarUsuario();

		this.cadastroService
			.cadastrar(novoUsuario)
			.subscribe(
				suc => {
					this.router.navigate(['/inicio', 'login']);
					console.log("Cadastrado com sucesso");
				},
				err => {
					console.log(err.message);
					this.erroCadastro = true;
				}
			);
	}

	private montarUsuario() {
		const nome = this.formCadastro.get('nome').value;
		const senha = this.formCadastro.get('senha').value;
		const email = this.formCadastro.get('email').value;
		const endereco = this.formCadastro.get('endereco').value;
		const tipo = this.formCadastro.get('tipo').value;
		const novoUsuario = {
			nome: nome,
			senha: senha,
			email: email,
			contatos: [
				{
					tipo: tipo,
					endereco: endereco
				}
			]
		};
		return novoUsuario;
	}
}
