import { NgModule } from '@angular/core';
import { RouterModule, Routes } from "@angular/router";
import { PaginaNaoEncontradaComponent } from './compartilhados/componentes/pagina-nao-encontrada/pagina-nao-encontrada.component';
import { LoginObrigatorioGuard } from './seguranca/login-obrigatorio.guard';

const rotas: Routes = [
	{
		path: '',
		pathMatch: 'full',
		redirectTo: 'inicio'
	},
	{
		path: 'inicio',
		// Módulo que será carregado na rota filha.
		loadChildren: './seguranca/seguranca.module#SegurancaModule'
	},
	{
		path: '',
		pathMatch: 'full',
		redirectTo: 'livros'
	},
	{
		// As rotas filhas são chamadas após o path ex. { /livros/todos ou /livros/novo }
		path: 'livros',
		loadChildren: './livros/livros.module#LivrosModule',
		canActivate: [LoginObrigatorioGuard]
	},
	{ path: 'nao-encontrado', component: PaginaNaoEncontradaComponent },
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