import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { TokenService } from './token.service';

@NgModule({
	imports: [CommonModule],
	providers: [TokenService]
})
export class TokenModule { }