import { HttpEvent, HttpHandler, HttpInterceptor, HttpRequest } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { TokenService } from './token.service';

/*
 * Intercepta cada request e adicionar o token. (funciona igual um filter).
 */
@Injectable()
export class TokenRequestInterceptor implements HttpInterceptor {

	constructor(private tokenService: TokenService) { }

	intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {

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