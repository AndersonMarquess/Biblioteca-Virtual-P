import { Component, OnInit } from '@angular/core';
import { LivrosService } from '../livros.service';

@Component({
	selector: 'bvp-listar-livros',
	templateUrl: './listar-livros.component.html'
})
export class ListarLivrosComponent implements OnInit {

	constructor(private livrosService: LivrosService) { }

	ngOnInit(): void {
		console.log("Tentando buscar todos os livros");
		
		this.livrosService
			.buscarTodosOsLivros()
			.subscribe(
				suc => console.log(suc),
				err => console.log(err)
			);
	}
}