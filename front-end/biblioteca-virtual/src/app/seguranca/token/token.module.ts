import { CommonModule } from '@angular/common';
import { HTTP_INTERCEPTORS } from '@angular/common/http';
import { NgModule } from '@angular/core';
import { TokenRequestInterceptor } from './token-request.interceptor';
import { TokenService } from './token.service';

@NgModule({
	imports: [CommonModule],
	providers: [
		{
			// Habilita o interceptor personalizado.
			provide: HTTP_INTERCEPTORS,
			useClass: TokenRequestInterceptor,
			// Caso exista outro interceptador, ele chama o próximo.
			multi: true
		},
		TokenService
	]
})
export class TokenModule { }