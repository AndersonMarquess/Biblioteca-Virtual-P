import { HttpClientModule } from '@angular/common/http';
import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { AppComponent } from './app.component';
import { AppRoutingModule } from './app.routing.module';
import { BarraDeNavegacaoComponent } from './compartilhados/componentes/barra-de-navegacao/barra-de-navegacao.component';
import { PaginaNaoEncontradaComponent } from './compartilhados/componentes/pagina-nao-encontrada/pagina-nao-encontrada.component';
import { TokenRequestInterceptorModule } from './seguranca/token/token-request.interceptor.module';

@NgModule({
	declarations: [
		AppComponent,
		PaginaNaoEncontradaComponent,
		BarraDeNavegacaoComponent
	],
	imports: [
		BrowserModule,
		HttpClientModule,
		TokenRequestInterceptorModule,
		// Modulo de rotas personalizadas
		AppRoutingModule
	],
	bootstrap: [AppComponent]
})
export class AppModule { }
