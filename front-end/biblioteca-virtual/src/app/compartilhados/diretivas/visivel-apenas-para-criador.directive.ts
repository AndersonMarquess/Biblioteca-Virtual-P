import { Directive, ElementRef, Input, OnInit, Renderer } from '@angular/core';
import { UsuariosService } from 'src/app/usuarios/usuarios.service';
import { Usuario } from '../models/usuario';

@Directive({
	selector: '[visivelApenasParaCriador]'
})
export class VisivelApenasParaCriadorDirective implements OnInit {

	// @Input("visivelApenasParaCriador") => assim poderia passar o id diretamente para a diretiva com o bind
	@Input() idDonoLivro: string;

	constructor(private elemento: ElementRef<any>, private renderer: Renderer, private usuariosService: UsuariosService) { }

	ngOnInit(): void {
		this.usuariosService.getUsuarioLogado()
			.subscribe(usuario => {
				this.habilitarElementoParaOCriador(usuario);
			});
	}

	private habilitarElementoParaOCriador(usuario: Usuario): void {
		if (this.idDonoLivro && usuario.id == this.idDonoLivro) {
			this.renderer.setElementStyle(this.elemento.nativeElement, "display", "block");
		}
	}
}