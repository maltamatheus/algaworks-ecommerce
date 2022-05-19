package testes;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.algaworks.ecommerce.model.Produto;

public class EntityManagerTests {
	
	protected static EntityManagerFactory managerFactory;
	
	protected EntityManager manager;
	
	@BeforeClass
	public static void setUpBeforeClass() {
		managerFactory = Persistence.createEntityManagerFactory("cursojpa");
		System.out.println("Criou o Manager Factory");
	}
	
	@AfterClass
	public static void tearDownAfterClass() {
		managerFactory.close();
		System.out.println("Fechou o Manager Factory");
	}
	
	@Before
	public void setUp() {
		manager = managerFactory.createEntityManager();
		System.out.println("Criou o Manager");
	}

	@After
	public void tearDown() {
		manager.close();
		System.out.println("Fechou o Manager");
	}

}
