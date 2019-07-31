import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { FormLivroComponent } from './form-livro/form-livro.component';
import { ListarLivrosComponent } from './listar-livros/listar-livros.component';
import { LivrosComponent } from './livros.component';

const rotas: Routes = [
	{
		path: '',
		component: LivrosComponent,
		children: [
			//rotas filhas de /livros
			{ path: '', redirectTo: 'all' },
			{ path: 'all', component: ListarLivrosComponent },
			{ path: 'novo', component: FormLivroComponent },
			{ path: 'editar/:idDono/:idLivro', component: FormLivroComponent }
		]
	}
];

@NgModule({
	imports: [RouterModule.forChild(rotas)],
	exports: [RouterModule]
})
export class LivrosRoutingModule { }