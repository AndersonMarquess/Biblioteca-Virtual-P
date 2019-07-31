import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { FormLivroComponent } from './form-livro.component';

@NgModule({
	declarations: [FormLivroComponent],
	imports: [
		CommonModule,
		FormsModule,
		ReactiveFormsModule
	]
})
export class FormLivroModule { }