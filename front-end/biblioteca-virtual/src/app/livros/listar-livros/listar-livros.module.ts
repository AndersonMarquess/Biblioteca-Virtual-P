import { CommonModule } from '@angular/common';
import { HttpClientModule } from '@angular/common/http';
import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { VisivelApenasParaCriadorDirective } from 'src/app/compartilhados/diretivas/visivel-apenas-para-criador.directive';
import { ListarLivrosComponent } from './listar-livros.component';

@NgModule({
	// Componentes e diretivas
	declarations: [
		ListarLivrosComponent,
		VisivelApenasParaCriadorDirective
	],
	imports: [
		CommonModule,
		HttpClientModule,
		RouterModule
	]
})
export class ListarLivrosModule { }