package testes;

import org.junit.Assert;
import org.junit.Test;

import com.algaworks.ecommerce.model.Categoria;

public class TestesGeracaoChavePrimaria extends EntityManagerTests{
	
	@Test
	public void testeGeneratedTypeSequenceGenerator() {
		
		Categoria categoria = new Categoria();
		
		categoria.setNome("Bebidas");
		
		manager.getTransaction().begin();
		manager.persist(categoria);
		manager.getTransaction().commit();
		
		manager.clear();
		
		Categoria categoriaVerify = manager.find(Categoria.class, 1);
		
		Assert.assertEquals(categoriaVerify.getNome(), "Bebidas");
		
		System.out.println(categoriaVerify.toString());
	}
	
	@Test
	public void testeGeneratedTypeAuto() {
		
		Categoria categoria = new Categoria();
		
		categoria.setNome("Bebidas");
		
		manager.getTransaction().begin();
		manager.persist(categoria);
		manager.getTransaction().commit();
		
		manager.clear();
		
		Categoria categoriaVerify = manager.find(Categoria.class, 1);
		
		Assert.assertEquals(categoriaVerify.getNome(), "Bebidas");
		
		System.out.println(categoriaVerify.toString());
	}

}
