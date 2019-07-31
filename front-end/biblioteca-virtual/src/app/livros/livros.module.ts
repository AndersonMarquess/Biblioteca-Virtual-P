import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { TokenRequestInterceptorModule } from '../seguranca/token/token-request.interceptor.module';
import { UsuariosModule } from '../usuarios/usuarios.module';
import { FormLivroModule } from './form-livro/form-livro.module';
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
		FormLivroModule,
		LivrosRoutingModule,
	],
	providers: [LivrosService]
})
export class LivrosModule { }