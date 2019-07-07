import { CommonModule } from '@angular/common';
import { HttpClientModule } from '@angular/common/http';
import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { ListarLivrosComponent } from './listar-livros.component';

@NgModule({
	declarations: [ListarLivrosComponent],
	imports: [
		CommonModule,
		HttpClientModule,
		RouterModule
	]
})
export class ListarLivrosModule { }