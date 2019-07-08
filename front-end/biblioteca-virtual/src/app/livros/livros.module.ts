import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { TokenRequestInterceptorModule } from '../seguranca/token/token-request.interceptor.module';
import { UsuariosModule } from '../usuarios/usuarios.module';
import { FormNovoLivroModule } from './form-novo-livro/form-novo-livro.module';
import { ListarLivrosModule } from './listar-livros/listar-livros.module';
import { LivrosComponent } from './livros.component';
import { LivrosRoutingModule } from './livros.rounting.module';
import { LivrosService } from './livros.service';

@NgModule({
	declarations: [LivrosComponent],
	imports: [
		CommonModule,
		ListarLivrosModule,
		TokenRequestInterceptorModule,
		UsuariosModule,
		FormNovoLivroModule,
		LivrosRoutingModule,
	],
	providers: [LivrosService]
})
export class LivrosModule { }