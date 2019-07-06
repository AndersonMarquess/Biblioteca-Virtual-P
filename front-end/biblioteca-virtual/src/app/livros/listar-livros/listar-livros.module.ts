import { CommonModule } from '@angular/common';
import { HttpClientModule } from '@angular/common/http';
import { NgModule } from '@angular/core';
import { ListarLivrosComponent } from './listar-livros.component';

@NgModule({
	declarations: [ListarLivrosComponent],
	imports: [
		CommonModule,
		HttpClientModule
	]
})
export class ListarLivrosModule { }