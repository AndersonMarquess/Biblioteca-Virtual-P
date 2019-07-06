import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { LoginService } from './login.service';
import { Router } from '@angular/router';

@Component({
	selector: 'bvp-login',
	templateUrl: './login.component.html',
	styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

	formularioDeLogin: FormGroup;
	erroLogin = false;

	constructor(private formBuilder: FormBuilder, private loginService: LoginService, private router: Router) { }

	ngOnInit(): void {
		this.formularioDeLogin = this.formBuilder.group({
			email: ['', Validators.required],
			senha: ['', Validators.required]
		});
	}

	login(): void {
		const email = this.formularioDeLogin.get("email").value;
		const senha = this.formularioDeLogin.get("senha").value;

		this.loginService.autenticar(email, senha)
			.subscribe(
				suc => this.router.navigate(['livros']),
				err => {
					console.log(err.message);
					this.erroLogin = true;
					this.formularioDeLogin.reset();
				}
			);
	}
}
