import { NgModule } from '@angular/core';
import { RouterModule, Routes } from "@angular/router";
import { PaginaNaoEncontradaComponent } from './compartilhados/componentes/pagina-nao-encontrada/pagina-nao-encontrada.component';

const rotas: Routes = [
	{
		path: '',
		pathMatch: 'full',
		redirectTo: 'auth'
	},
	{
		path: 'auth',
		// Módulo que será carregado na rota filha.
		loadChildren: './seguranca/seguranca.module#SegurancaModule'
	},
	{
		path: '',
		pathMatch: 'full',
		redirectTo: 'livros'
	},
	{
		// As rotas filhas são chamadas após o path ex. { /livros/all ou /livros/add }
		path: 'livros',
		loadChildren: './livros/livros.module#LivrosModule'
	},
	{
		// Lida com rotas inexistentes.
		path: '**',
		component: PaginaNaoEncontradaComponent
	}
];

@NgModule({
	imports: [RouterModule.forRoot(rotas)],
	exports: [RouterModule]
})
export class AppRoutingModule { }