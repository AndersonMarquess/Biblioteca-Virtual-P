import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';

@Component({
	selector: 'bvp-login',
	templateUrl: './login.component.html',
	styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

	formularioDeLogin: FormGroup;

	constructor(private formBuilder: FormBuilder) { }

	ngOnInit(): void {
		this.formularioDeLogin = this.formBuilder.group({
			email: ['', Validators.required],
			senha: ['', Validators.required]
		});
	}

	login(): void {
		const email = this.formularioDeLogin.get("email").value;
		const senha = this.formularioDeLogin.get("senha").value;

		console.log(email);
		console.log(senha);
	}
}
