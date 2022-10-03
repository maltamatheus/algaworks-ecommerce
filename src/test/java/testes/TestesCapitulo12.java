package testes;

import org.junit.Test;

import com.algaworks.ecommerce.enums.EnumSexo;
import com.algaworks.ecommerce.model.Cliente;

public class TestesCapitulo12 extends EntityManagerTests{
	
	@Test
	public void inserirClienteAtivoComConversor() {
		
		Cliente cli = new Cliente();
		cli.setAtivo(true);
		cli.setNome("Matheus");
		cli.setSobrenome("Malta de Aguiar");
		cli.setSexo(EnumSexo.MASCULINO);
		
		manager.getTransaction().begin();
		manager.persist(cli);
		manager.getTransaction().commit();
		
		manager.clear();
		Cliente cliente = manager.find(Cliente.class, 2);
		
		System.out.println(cliente.toString());
		
	}
	
	@Test
	public void validarCliente() {
		manager.getTransaction().begin();
		Cliente cli = new Cliente();
		manager.merge(cli);
		manager.getTransaction().commit();
	}

}
