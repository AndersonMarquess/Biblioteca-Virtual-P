import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { Router } from '@angular/router';
import { Observable } from 'rxjs';
import { UsuariosService } from 'src/app/usuarios/usuarios.service';
import { Usuario } from '../../models/usuario';

@Component({
	selector: 'bv-barra-navegacao',
	templateUrl: './barra-de-navegacao.component.html',
	styleUrls: ['./barra-de-navegacao.component.css']
})
export class BarraDeNavegacaoComponent {

	usuario$: Observable<Usuario>;

	constructor(private usuarioService: UsuariosService, private router: Router) {
		this.usuario$ = this.usuarioService.getUsuarioLogado();
	}

	deslogar(): void {
		this.usuarioService.deslogar();
		this.router.navigate(['/inicio', 'login']);
	}
}