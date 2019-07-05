import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { CadastroModule } from './cadastro/cadastro.module';
import { LoginModule } from './login/login.module';
import { SegurancaComponent } from './seguranca.component';
import { SegurancaRoutingModule } from './seguranca.routing.module';

@NgModule({
	declarations: [SegurancaComponent],
	imports: [
		CommonModule,
		LoginModule,
		CadastroModule,
		SegurancaRoutingModule
	]
})
export class SegurancaModule { }