package com.andersonmarques.bvp;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.andersonmarques.bvp.exception.CategoriaDuplicadaException;
import com.andersonmarques.bvp.model.Categoria;
import com.andersonmarques.bvp.model.Livro;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LivroTest {
	
	@Test
	public void criaUmLivro() {
		Livro livro = new Livro();
		livro.setIsbn("1234-5698-4512-4521");
		livro.setNome("Livro 1");
		livro.setDescricao("Livro sobre assuntos diversificados");
		livro.setUrlCapa("https://images.pexels.com/photos/1643113/pexels-photo-1643113.jpeg");
	}
	
	@Test
	public void verificarDadosDoLivro() {
		Livro livro = new Livro("1234-5698-4512-4521", "Livro 1", "Livro sobre assuntos diversificados", 
				"https://images.pexels.com/photos/1643113/pexels-photo-1643113.jpeg");
		
		assertEquals("1234-5698-4512-4521", livro.getIsbn());
		assertEquals("Livro 1", livro.getNome());
		assertEquals("Livro sobre assuntos diversificados", livro.getDescricao());
		assertEquals("https://images.pexels.com/photos/1643113/pexels-photo-1643113.jpeg", livro.getUrlCapa());
	}
	
	@Test
	public void adicionarERecuperaUmaCategoriaDoLivro() {
		Livro livro = new Livro();
		Categoria categoria = new Categoria();
		categoria.setNome("Categoria 1");
		livro.adicionarCategoria(categoria);
		
		List<Categoria> categorias = livro.getCategorias();
		assertEquals("Categoria 1", categorias.get(0).getNome());
	}
	
	@Test(expected = CategoriaDuplicadaException.class)
	public void adicionarCategoriaDuplicadaLancaException() {
		Livro livro = new Livro();
		Categoria categoria = new Categoria("Categoria 1");
		Categoria categoria2 = new Categoria("Categoria 1");
		livro.adicionarCategoria(categoria);
		livro.adicionarCategoria(categoria2);
	}
}
