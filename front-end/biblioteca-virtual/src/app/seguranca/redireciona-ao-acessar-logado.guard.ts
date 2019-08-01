import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot } from '@angular/router';
import { Observable } from 'rxjs';
import { TokenService } from './token/token.service';

@Injectable({ providedIn: "root" })
export class RedirecionaAoAcessarLogadoGuard implements CanActivate {

	constructor(private tokenService: TokenService, private router: Router) { }

	/*
	 * true para permitir acesso à rota.
	 */
	canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean | Observable<boolean> {
		if (!this.tokenService.tokenEstaExpirado()) {
			this.redirecionaParaListagemDeLivros();
			return false;
		} else {
			return true;
		}
	}

	/**
	 * Vai para página de listagem de livros
	 */
	private redirecionaParaListagemDeLivros(): void {
		this.router.navigate(['/livros', 'todos']);
	}
}