import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { CadastroComponent } from './cadastro.component';
import { CadastroService } from './cadastro.service';

@NgModule({
	declarations: [CadastroComponent],
	imports: [
		CommonModule,
		ReactiveFormsModule
	],
	providers: [CadastroService]
})
export class CadastroModule { }