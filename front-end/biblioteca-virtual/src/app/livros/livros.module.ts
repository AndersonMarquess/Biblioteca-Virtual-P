import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { ListarLivrosModule } from './listar-livros/listar-livros.module';
import { LivrosComponent } from './livros.component';
import { LivrosRoutingModule } from './livros.rounting.module';
import { LivrosService } from './livros.service';
import { TokenRequestInterceptorModule } from '../seguranca/token/token-request.interceptor.module';

@NgModule({
	declarations: [LivrosComponent],
	imports: [
		CommonModule,
		ListarLivrosModule,
		TokenRequestInterceptorModule,
		LivrosRoutingModule,
	],
	providers: [LivrosService]
})
export class LivrosModule { }