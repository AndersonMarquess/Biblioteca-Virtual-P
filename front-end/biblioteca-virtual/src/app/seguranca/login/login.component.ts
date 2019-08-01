import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { LoginService } from './login.service';

@Component({
	selector: 'bvp-login',
	templateUrl: './login.component.html',
	styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

	enderecoPosLogin: string;
	formularioDeLogin: FormGroup;
	erroLogin = false;

	constructor(private formBuilder: FormBuilder, private loginService: LoginService,
		private router: Router, private activatedRouter: ActivatedRoute) { }

	ngOnInit(): void {
		this.extrairEnderecoDeRedirecionamentoPosLogin();
		this.formularioDeLogin = this.formBuilder.group({
			email: ['', Validators.required],
			senha: ['', Validators.required]
		});
	}

	extrairEnderecoDeRedirecionamentoPosLogin(): void {
		this.enderecoPosLogin = this.activatedRouter.snapshot.queryParams.redirecionarPara;
	}

	login(): void {
		const email = this.formularioDeLogin.get("email").value;
		const senha = this.formularioDeLogin.get("senha").value;

		this.loginService.autenticar(email, senha)
			.subscribe(
				suc => this.redirecionarAposLogin(),
				err => {
					console.log(err.message);
					this.erroLogin = true;
					this.formularioDeLogin.reset();
				}
			);
	}

	private redirecionarAposLogin(): void {
		if (this.enderecoPosLogin) {
			this.router.navigateByUrl(this.enderecoPosLogin);
		} else {
			this.router.navigate(['/livros','todos']);
		}
	}
}
