import { HttpEvent, HttpHandler, HttpInterceptor, HttpRequest, HTTP_INTERCEPTORS } from '@angular/common/http';
import { Injectable, NgModule } from '@angular/core';
import { Observable } from 'rxjs';
import { TokenService } from './token.service';

/*
 * Intercepta cada request e adicionar o token. (funciona igual um filter).
 */
@Injectable()
export class TokenRequestInterceptor implements HttpInterceptor {

	constructor(private tokenService: TokenService) { }

	intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
		console.log("USANDO O INTERCEPTOR");

		if (this.tokenService.possuiToken()) {
			//Adiciona o token ao header Authorization
			req = req.clone({
				setHeaders: { "Authorization": this.tokenService.getToken() }
			});
		}

		// Continua a requisição
		return next.handle(req);
	}
}

// Para usar o interceptor basta importa seu modulo.
@NgModule({
	providers: [
		{
			// Habilita o interceptor personalizado.
			provide: HTTP_INTERCEPTORS,
			useClass: TokenRequestInterceptor,
			// Caso exista outro interceptador, ele chama o próximo.
			multi: true
		}
	]
})
export class TokenRequestInterceptorModule { }