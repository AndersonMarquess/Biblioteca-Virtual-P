import { CommonModule } from '@angular/common';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { NgModule } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { CadastroComponent } from './cadastro/cadastro.component';
import { CadastroService } from './cadastro/cadastro.service';
import { LoginComponent } from './login/login.component';
import { LoginService } from './login/login.service';
import { SegurancaComponent } from './seguranca.component';
import { SegurancaRoutingModule } from './seguranca.routing.module';
import { RequestInterceptor } from './token/request.interceptor';


@NgModule({
	declarations: [
		LoginComponent,
		CadastroComponent,
		SegurancaComponent
	],
	imports: [
		CommonModule,
		// Usado para validar os inputs
		ReactiveFormsModule,
		HttpClientModule,
		SegurancaRoutingModule
	],
	providers: [
		{
			// Habilita o interceptor personalizado.
			provide: HTTP_INTERCEPTORS,
			useClass: RequestInterceptor,
			// Caso exista outro interceptador, ele chama o próximo.
			multi: true
		},
		// Service é declarado dentro de providers.
		LoginService,
		CadastroService
	],
})
export class SegurancaModule { }