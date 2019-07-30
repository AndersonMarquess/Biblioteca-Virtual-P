import { CommonModule } from '@angular/common';
import { HttpClientModule } from '@angular/common/http';
import { NgModule } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { CadastroComponent } from './cadastro/cadastro.component';
import { CadastroService } from './cadastro/cadastro.service';
import { LoginComponent } from './login/login.component';
import { LoginService } from './login/login.service';
import { SegurancaComponent } from './seguranca.component';
import { SegurancaRoutingModule } from './seguranca.routing.module';

@NgModule({
	declarations: [
		SegurancaComponent,
		CadastroComponent,
		LoginComponent
	],
	imports: [
		CommonModule,
		RouterModule,
		// Usado para validar os inputs
		ReactiveFormsModule,
		HttpClientModule,
		SegurancaRoutingModule
	],
	providers: [
		CadastroService,
		LoginService
	]
})
export class SegurancaModule { }