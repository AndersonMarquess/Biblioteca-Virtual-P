import { NgModule } from '@angular/core';
import { RouterModule, Routes } from "@angular/router";

const rotas: Routes = [
	{
		path: '',
		pathMatch: 'full',
		redirectTo: 'bvp'
	},
	{
		// As rotas filhas ficaram após o path ex. { /bvp/cadastrar ou /bvp/login }
		path: 'bvp',
		// Componente ou módulo que será carregado na rota filha.
		loadChildren: './seguranca/seguranca.module#SegurancaModule'
	}
];

@NgModule({
	imports: [RouterModule.forRoot(rotas)],
	exports: [RouterModule]
})
export class AppRoutingModule { }