import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot } from '@angular/router';
import { Observable } from 'rxjs';
import { TokenService } from './token/token.service';

@Injectable({ providedIn: "root" })
export class LoginObrigatorioGuard implements CanActivate {

	constructor(private tokenService: TokenService, private router: Router) { }

	/*
	 * true para permitir acesso à rota.
	 */
	canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean | Observable<boolean> {
		if (this.tokenService.tokenEstaExpirado()) {
			this.redirecionarParaLogin(state);
			return false;
		} else {
			return true;
		}
	}

	/**
	 * Vai para tela de login, após fazer login, redireciona o usuário para rota que ele estava tentando acessar anteriormente.
	 * @param state 
	 */
	private redirecionarParaLogin(state: RouterStateSnapshot): void {
		this.router.navigate(
			['/auth', 'login'],
			// acesse redirecionarPara na query param.
			{ queryParams: { redirecionarPara: state.url } }
		);
	}
}