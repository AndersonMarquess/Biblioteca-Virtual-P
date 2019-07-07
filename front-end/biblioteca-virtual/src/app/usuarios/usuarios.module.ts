import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { UsuariosService } from './usuarios.service';

@NgModule({
	imports: [CommonModule],
	providers: [UsuariosService]
})
export class UsuariosModule { }