import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppComponent } from './app.component';
import { SegurancaModule } from './seguranca/seguranca.module';

@NgModule({
	declarations: [
		AppComponent
	],
	imports: [
		BrowserModule,
		SegurancaModule
	],
	bootstrap: [AppComponent]
})
export class AppModule { }
