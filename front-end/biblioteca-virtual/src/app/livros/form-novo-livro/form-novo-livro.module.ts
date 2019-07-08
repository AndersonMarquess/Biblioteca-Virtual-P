import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { FormNovoLivroComponent } from './form-novo-livro.component';

@NgModule({
	declarations: [FormNovoLivroComponent],
	imports: [
		CommonModule,
		FormsModule,
		ReactiveFormsModule
	]
})
export class FormNovoLivroModule { }