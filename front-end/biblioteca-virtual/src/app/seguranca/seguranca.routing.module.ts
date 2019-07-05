import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { CadastroComponent } from './cadastro/cadastro.component';
import { LoginComponent } from './login/login.component';
import { SegurancaComponent } from './seguranca.component';

const rotasSeguranca: Routes = [
	{
		path: '',
		component: SegurancaComponent,
		// Rotas renderizadas dentro do <router-outlet>
		children: [
			{ path: '', component: LoginComponent },
			{ path: 'login', component: LoginComponent },
			{ path: 'cadastrar', component: CadastroComponent }
		]
	}
];

@NgModule({
	imports: [RouterModule.forChild(rotasSeguranca)],
	exports: [RouterModule]
})
export class SegurancaRoutingModule { }