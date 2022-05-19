package testes;

import org.junit.Assert;
import org.junit.Test;

import com.algaworks.ecommerce.enums.EnumSexo;
import com.algaworks.ecommerce.model.Cliente;

public class ExercicioCRUDClientes extends EntityManagerTests{
	
	//READ
	@Test
	public void consultarCliente() {
		Cliente cliente = manager.find(Cliente.class, 1);
		Assert.assertEquals(cliente.getNome(), "Edir");
		System.out.println(cliente.toString());
	}
	//CREATE
	@Test
	public void criarCliente() {
		Cliente cliente = new Cliente();
		
		cliente.setId(2);
		cliente.setNome("Daniele");
		cliente.setSobrenome("Malta de Aguiar");
		cliente.setSexo(EnumSexo.FEMININO);
		
		manager.getTransaction().begin();
		manager.merge(cliente);
		manager.getTransaction().commit();
		
		Cliente clienteVerify = manager.find(Cliente.class, 2);
		Assert.assertEquals(clienteVerify.getNome(), "Daniele");
		System.out.println(clienteVerify.toString());
	}
	
	//UPDATE
	@Test
	public void atualizarCliente() {
		Cliente cliente = manager.find(Cliente.class, 1);
//		Cliente cliente = new Cliente();

//		cliente.setId(1);
		cliente.setNome("Outro Nome");
		
		manager.getTransaction().begin();
		manager.merge(cliente);
		manager.getTransaction().commit();
		
		manager.clear();
		
		Cliente clienteVerify = manager.find(Cliente.class, 1);
		Assert.assertEquals(clienteVerify.getNome(), "Outro Nome");
		System.out.println(clienteVerify.toString());
	}
	
	//DELETE
	@Test
	public void excluirCliente() {
		Cliente cliente = manager.find(Cliente.class, 1);
		
		manager.getTransaction().begin();
		manager.remove(cliente);
		manager.getTransaction().commit();
		
		Cliente clienteVerify = manager.find(Cliente.class, 1); 
		
		Assert.assertNull(clienteVerify);
	}
}
