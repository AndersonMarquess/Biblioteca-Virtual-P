import { NgModule } from '@angular/core';
import { RouterModule, Routes } from "@angular/router";

const rotas: Routes = [
	{
		path: '',
		pathMatch: 'full',
		redirectTo: ''
	},
	{
		path: '',
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
	}
];

@NgModule({
	imports: [RouterModule.forRoot(rotas)],
	exports: [RouterModule]
})
export class AppRoutingModule { }