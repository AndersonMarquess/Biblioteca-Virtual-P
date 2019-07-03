package com.andersonmarques.bvp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.andersonmarques.bvp.exception.CategoriaDuplicadaException;
import com.andersonmarques.bvp.model.Categoria;
import com.andersonmarques.bvp.model.Livro;
import com.andersonmarques.bvp.service.CategoriaService;
import com.andersonmarques.bvp.service.LivroService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LivroTest {

	private Livro livro;

	@Autowired
	private LivroService livroService;
	@Autowired
	private CategoriaService categoriaService;

	@Before
	public void prepararMocks() {
		livro = new Livro();
		livro.setTitulo("Livro 1");
		livro.setIsbn("1234-5698-4512-4521");
		livro.setDescricao("Livro sobre assuntos diversificados");
		livro.setUrlCapa("https://images.pexels.com/photos/1643113/pexels-photo-1643113.jpeg");
	}

	@Test
	public void criaUmLivro() {
		Livro livro = new Livro();
		livro.setTitulo("Livro 1");
		livro.setIsbn("1234-5698-4512-4521");
		livro.setDescricao("Livro sobre assuntos diversificados");
		livro.setUrlCapa("https://images.pexels.com/photos/1643113/pexels-photo-1643113.jpeg");
	}

	@Test
	public void verificarDadosDoLivro() {
		Livro livro = new Livro("1234-5698-4512-4521", "Livro 1", "Livro sobre assuntos diversificados",
				"https://images.pexels.com/photos/1643113/pexels-photo-1643113.jpeg", null);

		assertEquals("1234-5698-4512-4521", livro.getIsbn());
		assertEquals("Livro 1", livro.getTitulo());
		assertEquals("Livro sobre assuntos diversificados", livro.getDescricao());
		assertEquals("https://images.pexels.com/photos/1643113/pexels-photo-1643113.jpeg", livro.getUrlCapa());
	}

	@Test
	public void adicionarERecuperaUmaCategoriaDoLivro() {
		Categoria categoria = new Categoria();
		categoria.setNome("Categoria 1");
		livro.adicionarCategoria(categoria);

		List<Categoria> categorias = livro.getCategorias();
		assertEquals("Categoria 1", categorias.get(0).getNome());
	}

	@Test(expected = CategoriaDuplicadaException.class)
	public void adicionarCategoriaDuplicadaLancaException() {
		Categoria categoria = new Categoria("Categoria 1");
		Categoria categoria2 = new Categoria("Categoria 1");
		livro.adicionarCategoria(categoria);
		livro.adicionarCategoria(categoria2);
	}

	@Test
	public void persistirRecuperarERemoverUmLivroSemCategoria() {
		livroService.adicionar(livro);
		Livro livroRecuperado = livroService.buscarPorTitulo("livro 1");

		assertEquals("Livro sobre assuntos diversificados", livroRecuperado.getDescricao());
		assertEquals("1234-5698-4512-4521", livroRecuperado.getIsbn());

		livroService.removerPorId(livroRecuperado.getId());
	}

	@Test
	public void persistirRecuperarERemoverUmLivroComCategoriaNoBanco() {
		livro = new Livro("55-5554-1111", "Livro padrão 11", "Descrição do livro padrão 1", "", null);
		livro.adicionarCategoria(new Categoria("Categoria 1"), new Categoria("Programação"));
		livroService.adicionar(livro);
		Livro livroRecuperado = livroService.buscarPorTitulo("Livro padrão 11");

		assertEquals("Descrição do livro padrão 1", livroRecuperado.getDescricao());
		assertEquals("55-5554-1111", livroRecuperado.getIsbn());
		
		List<Categoria> categorias = categoriaService.buscarTodasCategoriasPorIdLivro(livroRecuperado.getId());
		assertTrue(categorias.stream().anyMatch(p -> p.getNome().equals("Categoria 1")));
		assertTrue(categorias.stream().anyMatch(p -> p.getNome().equals("Programação")));

		livroService.removerPorId(livroRecuperado.getId());
	}
	
	@Test
	public void adicionarUmaCategoriaExistenteEmUmNovoLivro() {
		livro = new Livro("551", "Livro mock", "adicionar categoria existente em livro", "", null);
		livro.adicionarCategoria(new Categoria("Programação"));
		Livro livroRecuperado = livroService.adicionar(livro);
		List<Categoria> categorias = categoriaService.buscarTodasCategoriasPorIdLivro(livroRecuperado.getId());
		
		assertEquals(1, categorias.size());
		assertTrue(categorias.stream().anyMatch(p -> p.getNome().equals("Programação")));
		
		livroService.removerPorId(livroRecuperado.getId());
	}
}
