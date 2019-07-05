import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { AppComponent } from './app.component';
import { AppRoutingModule } from './app.routing.module';


@NgModule({
	declarations: [
		AppComponent
	],
	imports: [
		BrowserModule,
		// Modulo de rotas personalizadas
		AppRoutingModule
	],
	bootstrap: [AppComponent]
})
export class AppModule { }
