import { CommonModule } from '@angular/common';
import { HttpClientModule } from '@angular/common/http';
import { NgModule } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { LoginComponent } from './login.component';
import { LoginService } from './login.service';

@NgModule({
	declarations: [LoginComponent],
	imports: [
		CommonModule,
		// Usado para validar os inputs
		ReactiveFormsModule,
		HttpClientModule
	],
	providers: [LoginService]
})
export class LoginModule { }