package com.algaworks.ecommerce.util;

import java.time.LocalDateTime;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import com.algaworks.ecommerce.enums.EnumStatusPagamento;
import com.algaworks.ecommerce.model.Cliente;
import com.algaworks.ecommerce.model.PagamentoCartao;
import com.algaworks.ecommerce.model.Pedido;

public class IniciandoEntidadePersistencia {

	public static void main(String[] args) {

		EntityManagerFactory managerFactory = Persistence.createEntityManagerFactory("cursojpa");
		
		EntityManager manager = managerFactory.createEntityManager();

//		Pedido pedido = manager.find(Pedido.class, 1);
		Pedido pedido = new Pedido();
		
//		pedido.setNotaFiscal(123);
		pedido.setDataPedido(LocalDateTime.now());
		pedido.setDataConclusao(LocalDateTime.now());


//		Cliente cliente = manager.find(Cliente.class, 1); 
//		pedido.setCliente(cliente);
		
		PagamentoCartao cartao = new PagamentoCartao();
		
		cartao.setNumeroCartao("123456");
		cartao.setStatus(EnumStatusPagamento.PROCESSANDO);
		cartao.setPedido(pedido);

		manager.getTransaction().begin();
		manager.persist(pedido);
		manager.persist(cartao);
		manager.getTransaction().commit();
		
		manager.clear();
		
//		Pedido pedidoVerify = manager.find(Pedido.class, pedido.getId());
		PagamentoCartao cartaoVerify = manager.find(PagamentoCartao.class, 1);
		
		System.out.println(cartaoVerify.toString());
		
		manager.close();
		
		managerFactory.close();
	}

}
