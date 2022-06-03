package testes;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.algaworks.ecommerce.enums.EnumSexo;
import com.algaworks.ecommerce.enums.EnumStatusPagamento;
import com.algaworks.ecommerce.model.Caracteristica;
import com.algaworks.ecommerce.model.Categoria;
import com.algaworks.ecommerce.model.Cliente;
import com.algaworks.ecommerce.model.ItemPedido;
import com.algaworks.ecommerce.model.NotaFiscal;
import com.algaworks.ecommerce.model.PagamentoCartao;
import com.algaworks.ecommerce.model.Pedido;
import com.algaworks.ecommerce.model.Produto;
import com.algaworks.ecommerce.model.chavescompostas.ItemPedidoId;

public class TestesMapeamentoDeEntidades extends EntityManagerTests {
	
	@Test
	public void carregaXmlNotaFiscal() {
		testeMapsId();
		
		NotaFiscal nf = manager.find(NotaFiscal.class,1);
		
		try {
			
			File file = new File("../resources/commons/xml/NFPedido01.xml");
			FileInputStream entrada = new FileInputStream(file);
			
			byte[] dados = new byte[1024];
					
			int existe = 0;
			
			while (existe!= -1) {
				existe = entrada.read(dados);
			}
			
			entrada.close();

			nf.setXml(dados);
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		manager.getTransaction().begin();
		manager.persist(nf);
		manager.getTransaction().commit();
		
		NotaFiscal nfVerify = manager.find(NotaFiscal.class, 1);
		
		File saida = new File("../resources/commons/xml/NFSaida.xml");
		
		try {
			FileOutputStream caneta = new FileOutputStream(saida);
			byte[] dados = nfVerify.getXml();
			caneta.write(dados);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void testeInsereContatosCliente() {
		
		Cliente cliente = manager.find(Cliente.class, 1);
		
		cliente.setContatos(Collections.singletonMap("E-mail", "maltamatheus@gmail.com"));
		
		manager.getTransaction().begin();
		manager.persist(cliente);
		manager.getTransaction().commit();
		
		manager.clear();
		
		Cliente clienteVerify = manager.find(Cliente.class, 1);
		
		System.out.println(clienteVerify.toString());
	}


	
	@Test
	public void testeInsereCaracteristicas() {
		
		Caracteristica caracteristica = new Caracteristica();
		
		Produto produto = manager.find(Produto.class, 2);
		
		caracteristica.setDominio(produto.getClass().getSimpleName());
		caracteristica.setCaracteristica("Tela");
		caracteristica.setDescricao("7\"");
		
		produto.setCaracteristicas(Arrays.asList(caracteristica));
		
		manager.getTransaction().begin();
		manager.persist(produto);
		manager.getTransaction().commit();
		
		manager.clear();
		
		Produto produtoVerify = manager.find(Produto.class, 2);
		
		System.out.println(produtoVerify.toString());
	}
	
	@Test
	public void testeMapsId() {
		Cliente cliente = manager.find(Cliente.class, 1);

		manager.getTransaction().begin();
		
		Pedido pedido = new Pedido();
		pedido.setCliente(cliente);
		pedido.setDataPedido(LocalDateTime.now());
		
		manager.persist(pedido); //Cria o pedido no banco
		
		
		NotaFiscal nf = new NotaFiscal();
		
		System.out.println("O ID do pedido é " + pedido.getId());		
//		nf.setId(pedido.getId());
		
		nf.setPedido(pedido);
		
//		nf.setXml("XML DA NOTA FISCAL");
		
		System.out.println(nf.toString());
		
		manager.persist(nf);
		
		manager.getTransaction().commit();
		
	}
	
	@Test
	public void aplicaTagProduto() {
		Produto produto = manager.find(Produto.class, 1);
		produto.setTagsProduto(Arrays.asList("Eletrônicos","Livro Digital","Ebook","Kindle"));
		manager.getTransaction().begin();
		manager.persist(produto);
		manager.getTransaction().commit();
		manager.clear();
		
		Cliente produtoVerify = manager.find(Cliente.class, 1);
		
		System.out.println(produtoVerify.toString());
		
	}
		

	@Test
	public void testesCallBack() {
		Cliente cliente = new Cliente();
		
		cliente.setNome("Matheus");
		cliente.setSobrenome("Malta de Aguiar");
		
		manager.getTransaction().begin();
		manager.persist(cliente);
		manager.getTransaction().commit();
		
		manager.clear();
		
		cliente = manager.find(Cliente.class, 2);
		
		cliente.setSobrenome("Malta de Aguiar");
		
		manager.getTransaction().begin();
		manager.persist(cliente);
		manager.getTransaction().commit();
		
		manager.clear();
		
		cliente = manager.find(Cliente.class, 2);
		
//		manager.getTransaction().begin();
//		manager.remove(cliente);
//		manager.getTransaction().commit();
		
	}

	@Test(expected = Exception.class)
	public void testeFlush() {

		try {
			
			manager.getTransaction().begin();

			Cliente cliente = manager.find(Cliente.class, 1);

			cliente.setSexo(EnumSexo.FEMININO);
			
			manager.flush();

			if (cliente.getPropriedadeAdicional() == null) {
				throw new RuntimeException("Propriedade ainda não definida");
			}
			
			manager.getTransaction().commit();

		} catch (Exception e) {
			
			manager.getTransaction().rollback();
			
			throw e;
		}
	}

	@Test
	public void testeManyNotaFiscalToOnePedido() {
		List<NotaFiscal> nfs = new ArrayList<NotaFiscal>();

		NotaFiscal nf1 = new NotaFiscal();
		nf1.setDataEmissao(new Date());
//		nf1.setXml("Nota Fiscal do Sabonete");

		NotaFiscal nf2 = new NotaFiscal();
		nf2.setDataEmissao(new Date());
//		nf2.setXml("Nota Fiscal do Shampoo");

		NotaFiscal nf3 = new NotaFiscal();
		nf3.setDataEmissao(new Date());
//		nf3.setXml("Nota Fiscal do Condicionador");

		nfs.add(nf1);
		nfs.add(nf2);
		nfs.add(nf3);

		Cliente cliente = new Cliente();

		Pedido pedido = new Pedido();
		pedido.setNotasPedido(nfs);
		pedido.setCliente(cliente);

		manager.getTransaction().begin();
		manager.persist(cliente);
		manager.persist(pedido);
		manager.getTransaction().commit();

		nfs.forEach(notas -> notas.setPedido(pedido));

		manager.getTransaction().begin();
		nfs.forEach(notas -> manager.persist(notas));
		manager.getTransaction().commit();

		Pedido pedidoVerify = manager.find(Pedido.class, pedido.getId());

		pedidoVerify.getNotasPedido().forEach(notas -> System.out.println(notas.toString()));
	}

	@Test
	public void testeOneToOne() {

		Pedido pedido = new Pedido();

		PagamentoCartao cartao = new PagamentoCartao();

		cartao.setNumero("123456");
		cartao.setStatus(EnumStatusPagamento.PROCESSANDO);
		cartao.setPedido(pedido);

		pedido.setPagamento(cartao);

		manager.getTransaction().begin();
		manager.persist(pedido);
		manager.persist(cartao);
		manager.getTransaction().commit();

		manager.clear();

		PagamentoCartao cartaoVerify = manager.find(PagamentoCartao.class, 1);

		Assert.assertTrue(cartaoVerify.getPedido().getPagamento().getNumero().equalsIgnoreCase("123456"));

	}

	@Test
	public void testeManyToMany() {
		// Inserindo o Produto
		Produto produto = new Produto();

		produto.setNome("Pastel de Carne");
		produto.setPrecoCusto(new BigDecimal(1));
		produto.setPrecoVenda(new BigDecimal(5));
		produto.setDescricao("Pastel Frito de Carne");

		// Categoria Pai
		Categoria categoriaPai = new Categoria();
		categoriaPai.setNome("Salgado Frito");

		// Categoria Filho
		Categoria categoriaFilho = new Categoria();
		categoriaFilho.setNome("Pastel");
		categoriaFilho.setCategoriaPai(categoriaPai);

		List<Categoria> categoriasProduto = new ArrayList<Categoria>();
		categoriasProduto.add(categoriaFilho);

		produto.setCategorias(categoriasProduto);

		// List<Produto> produtosCategoria = new ArrayList<Produto>();

		manager.getTransaction().begin();
		manager.persist(categoriaPai);
		manager.persist(categoriaFilho);
		manager.persist(produto);
		manager.getTransaction().commit();

	}

	@Test
	public void testeRemoverSemCascade() {

		Cliente clienteRemover = manager.find(Cliente.class, 3);

		manager.getTransaction().begin();
		clienteRemover.getPedidos().forEach(p -> manager.remove(p));
		manager.remove(clienteRemover);
		manager.getTransaction().commit();

		manager.clear();

		Assert.assertNull(manager.find(Cliente.class, 3));

	}

	@Test
	public void testeAutoRelacionamento() {

		Categoria categoriaPai = new Categoria();

		categoriaPai.setNome("Eletrônicos");

		Categoria categoriaFilho = new Categoria();

		categoriaFilho.setCategoriaPai(categoriaPai);
		categoriaFilho.setNome("Celulares");

		manager.getTransaction().begin();
		manager.persist(categoriaPai);
		manager.persist(categoriaFilho);
		manager.getTransaction().commit();

		manager.clear();

		Categoria categoriaVerify = manager.find(Categoria.class, categoriaFilho.getId());

		Assert.assertEquals(categoriaVerify.getNome(), "Celulares");

		System.out.println(categoriaVerify.toString());
	}

	@Test
	public void testeManyItensPedidoToOnePedido() {
		Cliente cliente = new Cliente();

		cliente.setNome("Matheus");
		cliente.setSobrenome("Malta de Aguiar");
		cliente.setSexo(EnumSexo.MASCULINO);

		Pedido pedido = new Pedido();

		pedido.setCliente(cliente);
		pedido.setDataPedido(LocalDateTime.now());
		pedido.setDataConclusao(LocalDateTime.now());

		ItemPedido itemPedido = new ItemPedido();

		Cliente produto = new Cliente();

		produto = manager.find(Cliente.class, 1);
		
		manager.getTransaction().begin();
		manager.persist(cliente);
		manager.persist(pedido);

//		ItemPedidoId itemPedidoId = new ItemPedidoId(pedido.getId(),produto.getId());
		
		
		
//		itemPedido.setIdPedido(pedido.getId());   // IdClass
//		itemPedido.setIdProduto(produto.getId()); // IdClass
		itemPedido.setId(new ItemPedidoId(pedido.getId(),produto.getId())); //EmbeddedId
		itemPedido.setQuantidade(10);
		
		manager.persist(itemPedido);
		
		manager.getTransaction().commit();

		manager.clear();
		
		ItemPedido itemPedidoVerify = manager.find(ItemPedido.class, new ItemPedidoId(pedido.getId(),produto.getId()));

		System.out.println(itemPedidoVerify.toString());

	}

	@Test
	public void testeManyPedidosToOneCliente() {
//		Cliente cliente = new Cliente();
//		
//		cliente.setNome("Matheus");
//		cliente.setSobrenome("Malta de Aguiar");
//		cliente.setSexo(EnumSexo.MASCULINO);

		Cliente cliente = manager.find(Cliente.class, 3);

		Pedido pedido = new Pedido();

		pedido.setCliente(cliente);
//		pedido.setNotaFiscal(123);
		pedido.setDataPedido(LocalDateTime.now());
		pedido.setDataConclusao(LocalDateTime.now());

		manager.getTransaction().begin();
		manager.persist(cliente);
		manager.persist(pedido);
		manager.getTransaction().commit();

		manager.clear();

		Pedido pedidoVerify = manager.find(Pedido.class, pedido.getId());

		Assert.assertEquals(pedidoVerify.getCliente().getNome(), "Matheus");

		System.out.println(pedidoVerify.toString());
	}

}
