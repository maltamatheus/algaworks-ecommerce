package testes;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.algaworks.ecommerce.model.Produto;

public class TestesCapitulo13 {
	
	protected static EntityManagerFactory entityManagerFactory;
	
	@BeforeClass
	public static void setUpBeforeClass() {
		entityManagerFactory = Persistence.createEntityManagerFactory("cursojpa");
	}
	
	@AfterClass
	public static void tearDownAfterClass() {
		entityManagerFactory.close();
	}
	
	@Test
	public static void buscarDoCache() {
		EntityManager entityManager1 = entityManagerFactory.createEntityManager();
		EntityManager entityManager2 = entityManagerFactory.createEntityManager();
		
		System.out.println("Buscando da Instância 1");
		
		entityManager1.find(Produto.class, 1);
		
		System.out.println("Buscando da Instância 1 de novo");
		
	}

}
