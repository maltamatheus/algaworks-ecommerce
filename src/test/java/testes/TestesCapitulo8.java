package testes;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.algaworks.ecommerce.enums.EnumSexo;
import com.algaworks.ecommerce.enums.EnumStatusPedido;
import com.algaworks.ecommerce.model.Categoria;
import com.algaworks.ecommerce.model.Cliente;
import com.algaworks.ecommerce.model.EnderecoEntregaPedido;
import com.algaworks.ecommerce.model.ItemPedido;
import com.algaworks.ecommerce.model.Pedido;
import com.algaworks.ecommerce.model.Produto;
import com.algaworks.ecommerce.model.chavescompostas.ItemPedidoId;

public class TestesCapitulo8 extends EntityManagerTests {
	
	@Test
	public void excluirPedidoEItemPedidoComCascade() {
		inserirItemPedidoEPedidoComCascade();
		Pedido pedido = manager.find(Pedido.class, 1);
		
		manager.getTransaction().begin();
		manager.remove(pedido);
		manager.getTransaction().commit();
		
		manager.clear();
		
		Assert.assertNull(manager.find(Pedido.class, 1));
		
	}

	@Test
	public void atualizarPedidoComCascade() {

		inserirItemPedidoEPedidoComCascade();

		Pedido pedido = manager.find(Pedido.class, 1);
		LocalDateTime dt1 = pedido.getDataAtualizacao();
		manager.clear();

		EnderecoEntregaPedido address = new EnderecoEntregaPedido();

		address.setCidade("Indaiatuba");

		pedido.setEnderecoEntrega(address);

		manager.getTransaction().begin();
		pedido.setDataAtualizacao(LocalDateTime.now());
		manager.merge(pedido);
		manager.getTransaction().commit();

		manager.clear();

		Pedido pedidoVerify = manager.find(Pedido.class, 1);

		System.out.println(dt1);
		System.out.println(pedidoVerify.getDataAtualizacao());

	}

	@Test
	public void inserirCategoriaNoProdutoComCascade() {

		Categoria categoria = new Categoria();
		categoria.setNome("Tablets");

		Produto produto = manager.find(Produto.class, 2);
		manager.clear();

		produto.setCategorias(Arrays.asList(categoria));

		manager.getTransaction().begin();
		manager.merge(produto);
		manager.getTransaction().commit();

		manager.clear();

		Produto produtoVerify = manager.find(Produto.class, produto.getId());

		produtoVerify.getCategorias().forEach(c -> System.out.println(c.getNome().toString()));

	}

	@Test
	public void inserirItemPedidoEPedidoComCascade() {

		Cliente cliente = manager.find(Cliente.class, 1);
		Produto produto = manager.find(Produto.class, 4);

		Pedido pedido = new Pedido();
		pedido.setCliente(cliente);
		pedido.setStatus(EnumStatusPedido.AGUARDANDO);
		pedido.setDataPedido(LocalDateTime.now());
		pedido.setDataAtualizacao(pedido.getDataPedido());

		List<ItemPedido> itens = new ArrayList<ItemPedido>();

		for (int i = 1; i <= 4; i++) {

			ItemPedido item = new ItemPedido();
			item.setId(new ItemPedidoId());
			item.setIdPedido(pedido);
			item.setIdProduto(manager.find(Produto.class, i));
			item.setQuantidade(10 * i);
			
			itens.add(item);

		}
		
		manager.getTransaction().begin();
		itens.forEach(item -> manager.persist(item));
		manager.getTransaction().commit();

		manager.clear();

		Pedido pedidoVerify = manager.find(Pedido.class, 1);
		
		pedidoVerify.getItensPedido().forEach(i -> System.out.println(i.toString()));

//		System.out.println(itemVerify.toString());

	}

	@Test
	public void inserirPedidoEClienteComCascade() {
		Cliente cliente = new Cliente();
		cliente.setNome("Matheus");
		cliente.setSobrenome("Malta");
		cliente.setSexo(EnumSexo.MASCULINO);
		cliente.setCpf("08766985773");

		Pedido pedido = new Pedido();
		pedido.setCliente(cliente);
		pedido.setStatus(EnumStatusPedido.AGUARDANDO);
		pedido.setDataPedido(LocalDateTime.now());

		Produto produto = manager.find(Produto.class, 2);

//		ItemPedido item = new ItemPedido();
//		item.setId(new ItemPedidoId());
//		item.setIdPedido(pedido);
//		item.setIdProduto(produto);
//		item.setQuantidade(10);

		manager.getTransaction().begin();
		manager.persist(pedido);
		manager.getTransaction().commit();

		manager.clear();

		Pedido pedidoVerify = manager.find(Pedido.class, 1);
		Cliente clienteVerify = manager.find(Cliente.class, pedidoVerify.getCliente().getId());

		System.out.println(clienteVerify.toString());

	}

	@Test
	public void inserirPedidoEItensPedidoComCascade() {

		Cliente cliente = manager.find(Cliente.class, 1);

		Pedido pedido = new Pedido();
		pedido.setCliente(cliente);
		pedido.setStatus(EnumStatusPedido.AGUARDANDO);
		pedido.setDataPedido(LocalDateTime.now());

		Produto produto = manager.find(Produto.class, 1);

		ItemPedido item = new ItemPedido();
		item.setId(new ItemPedidoId());
		item.setIdPedido(pedido);
		item.setIdProduto(produto);
		item.setQuantidade(3);

		pedido.setItensPedido(Arrays.asList(item));

		manager.getTransaction().begin();
		manager.persist(pedido);
//		manager.persist(item);
		manager.getTransaction().commit();

		manager.clear();

		Pedido pedidoVerify = manager.find(Pedido.class, 1);

		pedidoVerify.getItensPedido().forEach(i -> System.out.println(i.toString()));

	}

}
