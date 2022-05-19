package testes;

import java.math.BigDecimal;

import org.junit.Assert;
import org.junit.Test;

import com.algaworks.ecommerce.model.Produto;

public class IniciandoComJPA extends EntityManagerTests{
	
	@Test
	public void inserirObjetoComMerge() {
		
		Produto produto = new Produto();
		
		produto.setId(5);
		produto.setNome("Câmera Canon");
		produto.setDescricao("A melhor definição para as suas fotos");
		produto.setPrecoVenda(new BigDecimal(5000));
		
		manager.getTransaction().begin();
		manager.merge(produto);
		manager.getTransaction().commit();
		
		manager.clear();
		
		Produto produtoVerify = manager.find(Produto.class, produto.getId());

		Assert.assertNotNull(produtoVerify);
		
		System.out.println(produtoVerify.toString());
		
	}

	@Test
	public void atualizarObjeto() {
		
		Produto produto = manager.find(Produto.class, 4);
		
		String nome = produto.getNome();
		produto.setNome(produto.getDescricao());
		produto.setDescricao(nome);
		
		manager.getTransaction().begin();
//		manager.merge(produto);
		manager.getTransaction().commit();
		
		manager.clear();
		
		Produto produtoVerify = manager.find(Produto.class, 4);
		
		Assert.assertNotNull(produtoVerify);
		
		Assert.assertEquals(nome, produtoVerify.getDescricao());
	}
	
	@Test
	public void removerObjeto() {

		Produto produto = manager.find(Produto.class, 4);

		manager.getTransaction().begin();
		manager.remove(produto);
		manager.getTransaction().commit();
		
		Produto produtoVerify = manager.find(Produto.class, 4);
		
		Assert.assertNull(produtoVerify);
	}
	
	@Test
	public void inserirPrimeiroObjeto() {
		
		Produto produto = new Produto();
		
		produto.setId(5);
		produto.setNome("Câmera Canon");
		produto.setDescricao("A melhor definição para as suas fotos");
		produto.setPrecoVenda(new BigDecimal(5000));
		
		manager.getTransaction().begin();
		manager.persist(produto);
		manager.getTransaction().commit();
		
		manager.clear();
		
		Produto produtoVerify = manager.find(Produto.class, produto.getId());

		Assert.assertNotNull(produtoVerify);
		
		System.out.println(produtoVerify.toString());
		
	}
	
	
	@Test
	public void abrirEFecharTransacao() {
		
	manager.getTransaction().begin(); // A transação sempre começa com begin
	
	manager.getTransaction().commit(); // A transação termina começa com commit		
		
	}

}
