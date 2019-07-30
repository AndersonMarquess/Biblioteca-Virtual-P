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
			this.router.navigate(['/auth', 'login']);
			return false;
		} else {
			return true;
		}
	}
}