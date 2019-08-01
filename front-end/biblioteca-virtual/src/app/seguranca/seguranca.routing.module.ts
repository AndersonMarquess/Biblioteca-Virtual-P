import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { CadastroComponent } from './cadastro/cadastro.component';
import { LoginComponent } from './login/login.component';
import { SegurancaComponent } from './seguranca.component';
import { RedirecionaAoAcessarLogadoGuard } from './redireciona-ao-acessar-logado.guard';

const rotasSeguranca: Routes = [
	{
		path: '',
		component: SegurancaComponent,
		canActivate: [RedirecionaAoAcessarLogadoGuard],
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