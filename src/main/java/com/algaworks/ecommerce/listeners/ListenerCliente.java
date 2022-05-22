package com.algaworks.ecommerce.listeners;

import javax.persistence.PostPersist;

import com.algaworks.ecommerce.model.Cliente;

public class ListenerCliente {
	
	@PostPersist
	public void listenerAoGerarCliente(Cliente cliente) {
		System.out.println("Listener Cliente - Cliente " + cliente.getNome() + " " + cliente.getSobrenome() + " gerado");
	}

}
