import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { AppComponent } from './app.component';
import { AppRoutingModule } from './app.routing.module';
import { PaginaNaoEncontradaComponent } from './compartilhados/componentes/pagina-nao-encontrada/pagina-nao-encontrada.component';

@NgModule({
	declarations: [
		AppComponent,
		PaginaNaoEncontradaComponent
	],
	imports: [
		BrowserModule,
		// Modulo de rotas personalizadas
		AppRoutingModule
	],
	bootstrap: [AppComponent]
})
export class AppModule { }
