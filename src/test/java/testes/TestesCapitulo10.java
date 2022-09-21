package testes;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TemporalType;
import javax.persistence.Tuple;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

import org.junit.Test;

import com.algaworks.ecommerce.dto.ProdutoDTO;
import com.algaworks.ecommerce.model.Categoria;
import com.algaworks.ecommerce.model.Categoria_;
import com.algaworks.ecommerce.model.Cliente;
import com.algaworks.ecommerce.model.Cliente_;
import com.algaworks.ecommerce.model.ItemPedido;
import com.algaworks.ecommerce.model.ItemPedido_;
import com.algaworks.ecommerce.model.Pedido;
import com.algaworks.ecommerce.model.Pedido_;
import com.algaworks.ecommerce.model.Produto;
import com.algaworks.ecommerce.model.Produto_;

public class TestesCapitulo10 extends EntityManagerTests {
	
	@Test
	public void deletandoObjetos() {
		
		CriteriaBuilder criteriaBuilder = manager.getCriteriaBuilder();
		CriteriaDelete<Produto> criteriaDelete = criteriaBuilder.createCriteriaDelete(Produto.class);
		Root<Produto> root = criteriaDelete.from(Produto.class);
		
		criteriaDelete.where(criteriaBuilder.like(root.get(Produto_.nome), "Produto 1%"));
		
		Query delete = manager.createQuery(criteriaDelete);

		manager.getTransaction().begin();
		
		int regs = delete.executeUpdate();
		
		manager.getTransaction().commit();
		
		System.out.println(regs);
		
	}

	@Test
	public void atualizandoObjetos() {
		
		CriteriaBuilder criteriaBuilder = manager.getCriteriaBuilder();
		CriteriaUpdate<Produto> criteriaUpdate = criteriaBuilder.createCriteriaUpdate(Produto.class);
		Root<Produto> root = criteriaUpdate.from(Produto.class);
		
		criteriaUpdate.set(root.get(Produto_.precoVenda)
							, criteriaBuilder.prod(root.get(Produto_.precoVenda)
													, new BigDecimal("1.1"))
				);
		
		Subquery<Integer> subquery = criteriaUpdate.subquery(Integer.class);
		Root<Produto> subqueryRoot = subquery.correlate(root);
		Join<Produto,Categoria> joinCategoria = subqueryRoot.join(Produto_.categorias);
		subquery.select(subqueryRoot.get(Produto_.id));
		subquery.where(criteriaBuilder.equal(joinCategoria.get(Categoria_.nome), "Eletrônicos"));
		
		criteriaUpdate.where(criteriaBuilder.exists(subquery));
		
		Query update = manager.createQuery(criteriaUpdate);

		manager.getTransaction().begin();
		
		update.executeUpdate();
		
		manager.getTransaction().commit();
		
	}

	@Test
	public void subqueryComAny() {
		CriteriaBuilder criteriaBuilder = manager.getCriteriaBuilder();
		CriteriaQuery criteriaQuery = criteriaBuilder.createQuery(String.class);
		Root<Produto> root = criteriaQuery.from(Produto.class);
		
		criteriaQuery.select(root.get(Produto_.nome));
		
		Subquery<String> subquery = criteriaQuery.subquery(String.class);
		Root<Produto> subqueryRoot = subquery.from(Produto.class);
		subquery.select(subqueryRoot.get(Produto_.nome));
		subquery.where(criteriaBuilder.like(subqueryRoot.get(Produto_.descricao),"Descrição%"));

		criteriaQuery.where(criteriaBuilder.equal(root.get(Produto_.nome),criteriaBuilder.any(subquery)));
		criteriaQuery.orderBy(criteriaBuilder.asc(root.get(Produto_.id)));
		
		TypedQuery<String> typedQuery = manager.createQuery(criteriaQuery);
		List<String> produtos = typedQuery.getResultList();
		
		produtos.forEach(p -> System.out.println(p));
	}
	
	@Test
	public void subqueryComAll() {
		CriteriaBuilder criteriaBuilder = manager.getCriteriaBuilder();
		CriteriaQuery criteriaQuery = criteriaBuilder.createQuery(String.class);
		Root<Produto> root = criteriaQuery.from(Produto.class);
		
		criteriaQuery.select(root.get(Produto_.nome));
		
		Subquery<String> subquery = criteriaQuery.subquery(String.class);
		Root<Produto> subqueryRoot = subquery.from(Produto.class);
		subquery.select(subqueryRoot.get(Produto_.nome));
		subquery.where(criteriaBuilder.like(subqueryRoot.get(Produto_.descricao),"Descrição Produto 1"));

		criteriaQuery.where(criteriaBuilder.equal(root.get(Produto_.nome),criteriaBuilder.all(subquery)));
		
		TypedQuery<String> typedQuery = manager.createQuery(criteriaQuery);
		List<String> produtos = typedQuery.getResultList();
		
		produtos.forEach(p -> System.out.println(p));
	}
	
	@Test
	public void subqueryComExists() {
		CriteriaBuilder criteriaBuilder = manager.getCriteriaBuilder();
		CriteriaQuery criteriaQuery = criteriaBuilder.createQuery(Pedido.class);
		Root<Pedido> root = criteriaQuery.from(Pedido.class);
		
		criteriaQuery.select(root);
		
		Subquery<ItemPedido> subquery = criteriaQuery.subquery(ItemPedido.class);
		Root<ItemPedido> subqueryRoot = subquery.from(ItemPedido.class);
		Join<ItemPedido,Produto> joinItemPedidoProduto = subqueryRoot.join(ItemPedido_.idProduto);
		Join<Produto,Categoria> joinProdutoCategoria = joinItemPedidoProduto.join(Produto_.categorias);
		subquery.select(subqueryRoot);
		subquery.where(criteriaBuilder.equal(joinProdutoCategoria.get(Categoria_.nome), "Eletrônicos"));
		
		criteriaQuery.where(criteriaBuilder.exists(subquery));
		criteriaQuery.orderBy(criteriaBuilder.asc(root.get(Pedido_.id)));
		
		TypedQuery<Pedido> typedQuery = manager.createQuery(criteriaQuery);
		List<Pedido> pedidos = typedQuery.getResultList();
		
		pedidos.forEach(p -> System.out.println(p.getId()));
		
	}
	
	@Test
	public void subqueryComIn() {
		CriteriaBuilder criteriaBuilder = manager.getCriteriaBuilder();
		CriteriaQuery<Pedido> criteriaQuery = criteriaBuilder.createQuery(Pedido.class);
				
		Root<Pedido> root = criteriaQuery.from(Pedido.class);
		
		criteriaQuery.select(root);
		
		//Subquery Categoria
		Subquery<Produto> subqueryProduto = criteriaQuery.subquery(Produto.class);
		Root<Produto> subqueryRootProduto = subqueryProduto.from(Produto.class);
		
		Subquery<Categoria> subqueryCategoria = subqueryProduto.subquery(Categoria.class);
		Root<Categoria> subqueryRootCategoria = subqueryCategoria.from(Categoria.class);
		
		subqueryCategoria.select(subqueryRootCategoria);
		subqueryCategoria.where(criteriaBuilder.equal(subqueryRootCategoria.get(Categoria_.nome), "Eletrônicos"));
		
		subqueryProduto.select(subqueryRootProduto);
		subqueryProduto.where(subqueryRootProduto.in(subqueryCategoria));
		
		criteriaQuery.where(root.in(subqueryProduto));
		
		TypedQuery<Pedido> typedQuery = manager.createQuery(criteriaQuery);
		
		List<Pedido> pedidos = typedQuery.getResultList();
		
		pedidos.forEach(p -> System.out.println(p.toString()));
	}
	
	@Test
	public void subqueryComQueryPrincipal() {
	/* Método de exercício de subquery com query principal
	 * Listar Clientes que tenham comprado valor superior à R$ 500,00
	 */	
		
		CriteriaBuilder criteriaBuilder = manager.getCriteriaBuilder();
		
		CriteriaQuery criteriaQuery = criteriaBuilder.createQuery(Cliente.class);
		
		Root<Cliente> root = criteriaQuery.from(Cliente.class);
		
		//Busca os Clientes
		criteriaQuery.select(root);
		
		/* Subquery para retornar a soma dos produtos que cada cliente comprou
		 * Para chegar ao valor dos produtos é preciso das tabelas de Pedido, ItemPedido e Produto
		 */
		Subquery<BigDecimal> subquery = criteriaQuery.subquery(BigDecimal.class);
		Root<Pedido> subqueryRoot = subquery.from(Pedido.class);
		Join<Pedido,ItemPedido> joinPedidoItemPedido = subqueryRoot.join(Pedido_.itensPedido);
		Join<ItemPedido,Produto> joinItemPedidoProduto = joinPedidoItemPedido.join(ItemPedido_.idProduto);
		
		subquery.select(criteriaBuilder.sum(joinItemPedidoProduto.get(Produto_.precoVenda)));
		subquery.where(criteriaBuilder.equal(root.get(Cliente_.id),subqueryRoot.get(Pedido_.cliente).get(Cliente_.id)));
		
		criteriaQuery.where(criteriaBuilder.greaterThanOrEqualTo(subquery, new BigDecimal(500)));
		
		TypedQuery<Cliente> typedQuery = manager.createQuery(criteriaQuery);
		List<Cliente> clientes = typedQuery.getResultList();
		clientes.forEach(c -> System.out.println(c.getNomeCompleto()));
	}
	
	@Test
	public void selecionandoListas() {
		CriteriaBuilder criteriaBuilder = manager.getCriteriaBuilder();
		CriteriaQuery cqCategoria = criteriaBuilder.createQuery(Categoria.class);
		Root<Categoria> rootCategoria = cqCategoria.from(Categoria.class);

		//Lista Produtos de uma Categoria
		cqCategoria.select(rootCategoria.get(Categoria_.produtos));

		TypedQuery<Produto> tqCategoria = manager.createQuery(cqCategoria);
		
		List<Produto> prods = tqCategoria.getResultList();
		
		prods.forEach(p -> System.out.println(p.getNome()));
	}
	
	@Test
	public void usandoSubqueries() {
		CriteriaBuilder criteriaBuilder = manager.getCriteriaBuilder();
		CriteriaQuery criteriaQuery = criteriaBuilder.createQuery(Produto.class);
		
		Root<Produto> root = criteriaQuery.from(Produto.class);
		
		//Listando todos os Produtos
		criteriaQuery.select(root);
		
		Subquery<BigDecimal> subquery = criteriaQuery.subquery(BigDecimal.class);
		
		Root<Produto> subqueryRoot = subquery.from(Produto.class);

		//Subquery deve retornar a média geral dos preços dos produtos
		subquery.select(criteriaBuilder.avg(subqueryRoot.get(Produto_.precoVenda)).as(BigDecimal.class));
		
		criteriaQuery.where(criteriaBuilder.greaterThan(root.get(Produto_.precoVenda), subquery));
		
		TypedQuery<Produto> typedQuery = manager.createQuery(criteriaQuery);
		
		List<Produto> produtos = typedQuery.getResultList();
		
		produtos.forEach(p -> System.out.println(p.getDescricao() + " | " + p.getPrecoVenda()));
	}
	
	@Test
	public void usandoDistinct() {
		CriteriaBuilder criteriaBuilder = manager.getCriteriaBuilder();
		CriteriaQuery criteriaQuery = criteriaBuilder.createQuery(Tuple.class);
		Root<Produto> rootProduto = criteriaQuery.from(Produto.class);

		criteriaQuery.multiselect(criteriaBuilder.literal("nome").alias("nome_produto"),
				                  criteriaBuilder.literal("descricao").alias("descricao"));
		
		criteriaQuery.distinct(true);
		
		TypedQuery<Tuple> typedQuery = manager.createQuery(criteriaQuery);
		
		List<Tuple> recs = typedQuery.getResultList();
		
		recs.forEach(r -> System.out.println(r.get("nome_produto") + "|" + r.get("descricao")));
	}

	@Test
	public void usandoIn() {
		CriteriaBuilder criteriaBuilder = manager.getCriteriaBuilder();
//		CriteriaQuery criteriaQuery = criteriaBuilder.createQuery(Tuple.class);
		CriteriaQuery cqCategoria = criteriaBuilder.createQuery(Categoria.class);
//		Root<Produto> rootProduto = criteriaQuery.from(Produto.class);
		Root<Categoria> rootCategoria = cqCategoria.from(Categoria.class);

		//Lista nome e descrição do produto
//		criteriaQuery.multiselect(rootProduto.get(Produto_.nome).alias("nome_produto")
//				,rootProduto.get(Produto_.descricao).alias("descricao")
//				);
		
		//Lista Produtos de uma Categoria
		cqCategoria.select(rootCategoria.get(Categoria_.produtos));

//		criteriaQuery.where(rootProduto.get(Produto_.id).in(rootCategoria.getCompoundSelectionItems().contains(rootCategoria));
//		subqueryProduto.select(criteriaBuilder.in(null))
		
		
//		TypedQuery<Tuple> typedQuery = manager.createQuery(criteriaQuery);
		
		TypedQuery<Categoria> tqCategoria = manager.createQuery(cqCategoria);
		
//		List<Tuple> recs = typedQuery.getResultList();
		
		List<Categoria> cats = tqCategoria.getResultList();
		
//		recs.forEach(r -> System.out.println(r.get("nome_produto") + "|" + r.get("descricao")));
		
		cats.forEach(c-> {
			c.getProdutos().forEach(cp -> System.out.println(cp.getNome()));
		});
	}

	@Test
	public void usandoCase(){
		CriteriaBuilder criteriaBuilder = manager.getCriteriaBuilder();
		CriteriaQuery criteriaQuery = criteriaBuilder.createQuery(Tuple.class);
		Root<Produto> root = criteriaQuery.from(Produto.class);
		
		criteriaQuery.multiselect(
				 root.get(Produto_.nome).alias("nome_produto")
				,criteriaBuilder.selectCase()
				.when(criteriaBuilder.like(root.get(Produto_.nome), "Produto %"),"Produto Novo")
				.otherwise("Produto Antigo").alias("classificacao_produto")
				);
		
		TypedQuery<Tuple> typedQuery = manager.createQuery(criteriaQuery);
		List<Tuple> res = typedQuery.getResultList();
		res.forEach(t -> System.out.println(t.get("nome_produto") + " | " + t.get("classificacao_produto")));
	}
	
	@Test
	public void usandoFuncoesAgregacao() {
	
		CriteriaBuilder criteriaBuilder = manager.getCriteriaBuilder();
		CriteriaQuery   criteriaQuery = criteriaBuilder.createQuery(Tuple.class);
		Root<Produto> root = criteriaQuery.from(Produto.class);
		Join<Produto,Categoria> joinProdutoCategoria = root.join(Produto_.categorias);

		criteriaQuery.multiselect(
				joinProdutoCategoria.get(Categoria_.nome).alias("categoria")
			   ,criteriaBuilder.sum(root.get(Produto_.precoVenda)).alias("totalProdutos")
				);
		criteriaQuery.groupBy(joinProdutoCategoria.get(Categoria_.nome));
		
		criteriaQuery.having(
				criteriaBuilder.greaterThan(
						criteriaBuilder.sum(root.get(Produto_.precoVenda)) 
					   ,new BigDecimal(3000))
				);
        
		TypedQuery<Tuple> typedQuery = manager.createQuery(criteriaQuery);
		
		List<Tuple> lista = typedQuery.getResultList();
		
		int i = 0;
		
		System.out.println();
		
		lista.forEach(o->System.out.println(o.get("categoria") + " | " + o.get("totalProdutos")));		
	}
	
	@Test
	public void usandoFuncoesColecoes() {
	
		CriteriaBuilder criteriaBuilder = manager.getCriteriaBuilder();
		CriteriaQuery   criteriaQuery = criteriaBuilder.createQuery(Tuple.class);
		Root<Categoria> root = criteriaQuery.from(Categoria.class);

		criteriaQuery.multiselect(
				root.get(Categoria_.nome).alias("nome")
			   ,criteriaBuilder.size(root.get(Categoria_.produtos)).alias("totalProdutos")
				);
        
		TypedQuery<Tuple> typedQuery = manager.createQuery(criteriaQuery);
		
		List<Tuple> lista = typedQuery.getResultList();
		
		int i = 0;
		
		System.out.println();
		
		lista.forEach(o->System.out.println(o.get("nome") + " | " + o.get("totalProdutos")));		
	}
	
	@Test
	public void usandoFuncoesNumericas() {
	
		CriteriaBuilder criteriaBuilder = manager.getCriteriaBuilder();
		CriteriaQuery   criteriaQuery = criteriaBuilder.createQuery(Tuple.class);
		Root<Produto> root = criteriaQuery.from(Produto.class);

		criteriaQuery.multiselect(
				root.get(Produto_.nome).alias("nome") 
			   ,root.get(Produto_.descricao).alias("descricao")
			   ,root.get(Produto_.precoVenda).alias("precoVenda")
//			   ,criteriaBuilder.sqrt(root.get(Produto_.precoVenda)).alias("raizQuadradaPrecoVenda")
			   ,criteriaBuilder.mod(root.get(Produto_.precoVenda).as(Integer.class),2).alias("restoDivisao")
			   );
        
		TypedQuery<Tuple> typedQuery = manager.createQuery(criteriaQuery);
		
		List<Tuple> lista = typedQuery.getResultList();
		
		int i = 0;
		
		System.out.println();
		
//		lista.forEach(o->System.out.println(o.get("nome") + " | " + o.get("descricao") + " | " + o.get("precoVenda") + " | " + o.get("raizQuadradaPrecoVenda")));
		lista.forEach(o->System.out.println(o.get("nome") + " | " + o.get("descricao") + " | " + o.get("precoVenda") + " | " + o.get("restoDivisao")));
	}
	
	@Test
	public void usandoFuncoesData() {
	
		CriteriaBuilder criteriaBuilder = manager.getCriteriaBuilder();
		CriteriaQuery   criteriaQuery = criteriaBuilder.createQuery(Tuple.class);
		Root<Produto> root = criteriaQuery.from(Produto.class);

		criteriaQuery.multiselect(
				root.get(Produto_.nome).alias("nome"), 
				root.get(Produto_.descricao).alias("descricao"),
				root.get(Produto_.dataInclusaoCadastro).alias("dtInclusao"));
        
		criteriaQuery.where(criteriaBuilder.between(
						root.get(Produto_.dataInclusaoCadastro), 
								java.sql.Date.valueOf(LocalDate.now()),
								java.sql.Date.valueOf(LocalDate.now().plusMonths(3))
						)
				);

		TypedQuery<Tuple> typedQuery = manager.createQuery(criteriaQuery);
		
		List<Tuple> lista = typedQuery.getResultList();
		
		int i = 0;
		
		System.out.println();
		
		lista.forEach(o->System.out.println(o.get("nome") + " | " + o.get("descricao") + " | " + o.get("dtInclusao")));		
	}
	
	@Test
	public void usandoFuncoesString() {
	
		CriteriaBuilder criteriaBuilder = manager.getCriteriaBuilder();
		CriteriaQuery   criteriaQuery = criteriaBuilder.createQuery(Tuple.class);
		Root<Produto> root = criteriaQuery.from(Produto.class);

		criteriaQuery.multiselect(
				criteriaBuilder.concat(
						criteriaBuilder.concat(root.get(Produto_.nome), "|"),root.get(Produto_.descricao)
						).alias("usando_concat"),
				criteriaBuilder.length(root.get(Produto_.nome)).alias("usando_length"),
				criteriaBuilder.locate(root.get(Produto_.nome), "K").alias("usando_locate"), // O mesmo que indexOf
				criteriaBuilder.substring(root.get(Produto_.descricao), 5).alias("usando_substring"),
				criteriaBuilder.lower(root.get(Produto_.nome)).alias("usando_lower"),
				criteriaBuilder.upper(root.get(Produto_.nome)).alias("usando_upper"),
				criteriaBuilder.trim(
						criteriaBuilder.concat("    ", root.get(Produto_.descricao))
						).alias("usando_trim")
				);
		
		criteriaQuery.where(criteriaBuilder.equal(root.get(Produto_.id),1));
		
		TypedQuery<Tuple> typedQuery = manager.createQuery(criteriaQuery);
		
		List<Tuple> lista = typedQuery.getResultList();
		
		int i = 0;
		
		System.out.println();
		
		lista.forEach(o->System.out.println("Concat: " + o.get("usando_concat") +"\n"+ 
				                            "Length: " + o.get("usando_length") +"\n"+
				                            "Locate: " + o.get("usando_locate") +"\n"+
				                            "Substring: " + o.get("usando_substring") +"\n"+
				                            "Lower: " + o.get("usando_lower") +"\n"+
				                            "Upper: " + o.get("usando_upper") +"\n"+
				                            "Trim: " + o.get("usando_trim")
		                                   )
				);		
	}
	
	@Test
	public void usandoPaginacao() {
	
		CriteriaBuilder criteriaBuilder = manager.getCriteriaBuilder();
		CriteriaQuery   criteriaQuery = criteriaBuilder.createQuery(Produto.class);
		Root<Produto> root = criteriaQuery.from(Produto.class);
		
		criteriaQuery.select(root);
		criteriaQuery.orderBy(criteriaBuilder.asc(root.get(Produto_.id)));
		
		TypedQuery<Produto> typedQuery = manager.createQuery(criteriaQuery);
		
		// Quero a página 4
		// A fórmula da paginação é: inicioPagina = recsPorPagina * (pag - 1);
		int recsPorPagina = 3;
		int pag = 7;
		int inicioPagina = recsPorPagina * (pag -1);
		
		typedQuery.setFirstResult(inicioPagina);
		typedQuery.setMaxResults(recsPorPagina);
		
		List<Produto> lista = typedQuery.getResultList();
		
		lista.forEach(p -> System.out.println(p.getNome() + "|" + p.getDescricao()));
	}
	
	@Test
	public void usandoOrderBy() {
		CriteriaBuilder criteriaBuilder = manager.getCriteriaBuilder();

		CriteriaQuery criteriaQuery = criteriaBuilder.createQuery(Produto.class);

		Root<Produto> root = criteriaQuery.from(Produto.class);
		
		root.fetch(Produto_.estoque,JoinType.LEFT);
		root.fetch(Produto_.categorias,JoinType.LEFT);

		criteriaQuery.where(criteriaBuilder.like(root.get(Produto_.nome), "Produto %"));
//		criteriaQuery.orderBy(criteriaBuilder.asc(root.get(Produto_.id)));
		criteriaQuery.orderBy(criteriaBuilder.desc(root.get(Produto_.id)));

		TypedQuery<Produto> typedQuery = manager.createQuery(criteriaQuery);

		List<Produto> recs = typedQuery.getResultList();

		System.out.println();

		if (recs.size() > 0) {
			System.out.println("DATA_INCLUSAO | NOME_PRODUTO | DESCRICAO_PRODUTO | PRECO_VENDA");
			recs.forEach(r -> System.out.println(r.getDataInclusaoCadastro() + "|" + r.getNome() + "|"
					+ r.getDescricao() + "|" + r.getPrecoVenda()));
		} else {
			System.out.println("Nenhum registro encontrado");
		}

	}

	@Test
	public void usandoOperadoresLogicos() {
		CriteriaBuilder criteriaBuilder = manager.getCriteriaBuilder();
		CriteriaQuery criteriaQuery = criteriaBuilder.createQuery(Produto.class);
		Root<Produto> root = criteriaQuery.from(Produto.class);
		
		criteriaQuery.select(root);
		
		criteriaQuery.where(
				            criteriaBuilder.equal(root.get(Produto_.nome),"Produto 1"),
				            criteriaBuilder.and(criteriaBuilder.greaterThan(root.get(Produto_.precoVenda), new BigDecimal(5000)),
				            		criteriaBuilder.or(
				            				criteriaBuilder.equal(root.get(Produto_.descricao), "Descrição Produto 1"),
				            				criteriaBuilder.equal(root.get(Produto_.descricao), "Descrição Produto 2")
				            				)
				            		)
				);
		
		try {

			TypedQuery<Produto> typedQuery = manager.createQuery(criteriaQuery);
			
			Produto produto = typedQuery.getSingleResult();
			
			System.out.println(produto.getNome() + "|" + produto.getDescricao());
			
		} catch (NoResultException e) {
			System.out.println("Nenhum Registro Encontrado");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void usandoDiferente() {
		CriteriaBuilder criteriaBuilder = manager.getCriteriaBuilder();

		CriteriaQuery criteriaQuery = criteriaBuilder.createQuery(Produto.class);

		Root<Produto> root = criteriaQuery.from(Produto.class);

		root.fetch("estoque", JoinType.LEFT);
		root.fetch("categorias", JoinType.LEFT);

		criteriaQuery.where(criteriaBuilder.notEqual(root.get(Produto_.nome), "Produto 10"));

		TypedQuery<Produto> typedQuery = manager.createQuery(criteriaQuery);

		List<Produto> recs = typedQuery.getResultList();

		System.out.println();

		if (recs.size() > 0) {
			System.out.println("DATA_INCLUSAO | NOME_PRODUTO | DESCRICAO_PRODUTO | PRECO_VENDA");
			recs.forEach(r -> System.out.println(r.getDataInclusaoCadastro() + "|" + r.getNome() + "|"
					+ r.getDescricao() + "|" + r.getPrecoVenda()));
		} else {
			System.out.println("Nenhum registro encontrado");
		}

	}
	
	@Test
	public void usandoBetween() {
		CriteriaBuilder criteriaBuilder = manager.getCriteriaBuilder();

		CriteriaQuery criteriaQuery = criteriaBuilder.createQuery(Produto.class);

		Root<Produto> root = criteriaQuery.from(Produto.class);

		root.fetch("estoque", JoinType.LEFT);
		root.fetch("categorias", JoinType.LEFT);

		criteriaQuery.where(criteriaBuilder.between(root.get(Produto_.precoVenda),new BigDecimal(5000),new BigDecimal(10000)));

		TypedQuery<Produto> typedQuery = manager.createQuery(criteriaQuery);

		List<Produto> recs = typedQuery.getResultList();

		System.out.println();

		if (recs.size() > 0) {
			System.out.println("DATA_INCLUSAO | NOME_PRODUTO | DESCRICAO_PRODUTO | PRECO_VENDA");
			recs.forEach(r -> System.out.println(r.getDataInclusaoCadastro() + "|" + r.getNome() + "|"
					+ r.getDescricao() + "|" + r.getPrecoVenda()));
		} else {
			System.out.println("Nenhum registro encontrado");
		}

	}

	@Test
	public void usandoMaiorMenorComDatas() {
		CriteriaBuilder criteriaBuilder = manager.getCriteriaBuilder();

		CriteriaQuery criteriaQuery = criteriaBuilder.createQuery(Produto.class);

		Root<Produto> root = criteriaQuery.from(Produto.class);

		root.fetch("estoque", JoinType.LEFT);
		root.fetch("categorias", JoinType.LEFT);
		
		Date hoje = new Date();

//		criteriaQuery.where(criteriaBuilder.greaterThanOrEqualTo(root.get(Produto_.dataInclusaoCadastro),hoje));
		criteriaQuery.where(criteriaBuilder.lessThanOrEqualTo(root.get(Produto_.dataInclusaoCadastro),hoje));

		TypedQuery<Produto> typedQuery = manager.createQuery(criteriaQuery);

		List<Produto> recs = typedQuery.getResultList();

		System.out.println();

		if (recs.size() > 0) {
			System.out.println("DATA_INCLUSAO | NOME_PRODUTO | DESCRICAO_PRODUTO | DATA_CADASTRO | HOJE");
			recs.forEach(r -> System.out.println(r.getDataInclusaoCadastro() + "|" + r.getNome() + "|"
					+ r.getDescricao() + "|" + r.getDataInclusaoCadastro() + "|" + hoje));
		} else {
			System.out.println("Nenhum registro encontrado");
		}

	}

	@Test
	public void usandoMaiorMenor() {
		CriteriaBuilder criteriaBuilder = manager.getCriteriaBuilder();

		CriteriaQuery criteriaQuery = criteriaBuilder.createQuery(Produto.class);

		Root<Produto> root = criteriaQuery.from(Produto.class);

		root.fetch("estoque", JoinType.LEFT);
		root.fetch("categorias", JoinType.LEFT);

		criteriaQuery.where(criteriaBuilder.greaterThanOrEqualTo(root.get(Produto_.precoVenda),new BigDecimal("5000")));
//		criteriaQuery.where(criteriaBuilder.lessThanOrEqualTo(root.get(Produto_.precoVenda),new BigDecimal("5000")));

		TypedQuery<Produto> typedQuery = manager.createQuery(criteriaQuery);

		List<Produto> recs = typedQuery.getResultList();

		System.out.println();

		if (recs.size() > 0) {
			System.out.println("DATA_INCLUSAO | NOME_PRODUTO | DESCRICAO_PRODUTO | PRECO_VENDA");
			recs.forEach(r -> System.out.println(r.getDataInclusaoCadastro() + "|" + r.getNome() + "|"
					+ r.getDescricao() + "|" + r.getPrecoVenda()));
		} else {
			System.out.println("Nenhum registro encontrado");
		}

	}
	
	@Test
	public void usandoIsEmptyIsNotEmpty() {
		CriteriaBuilder criteriaBuilder = manager.getCriteriaBuilder();

		CriteriaQuery criteriaQuery = criteriaBuilder.createQuery(Produto.class);

		Root<Produto> root = criteriaQuery.from(Produto.class);

		root.fetch("estoque", JoinType.LEFT);
		root.fetch("categorias", JoinType.LEFT);

//		criteriaQuery.where(criteriaBuilder.isEmpty(root.get(Produto_.CATEGORIAS)));
		criteriaQuery.where(criteriaBuilder.isNotEmpty(root.get(Produto_.CATEGORIAS)));

		TypedQuery<Produto> typedQuery = manager.createQuery(criteriaQuery);

		List<Produto> recs = typedQuery.getResultList();

		System.out.println();

		if (recs.size() > 0) {
			System.out.println("DATA_INCLUSAO | NOME_PRODUTO | DESCRICAO_PRODUTO | CATEGORIA_PRODUTO");
			recs.forEach(r -> System.out.println(r.getDataInclusaoCadastro() + "|" + r.getNome() + "|"
					+ r.getDescricao() + "|" + r.getCategorias()));
		} else {
			System.out.println("Nenhum registro encontrado");
		}

	}
	
	@Test
	public void usandoIsNullIsNotNull() {
		CriteriaBuilder criteriaBuilder = manager.getCriteriaBuilder();

		CriteriaQuery criteriaQuery = criteriaBuilder.createQuery(Produto.class);

		Root<Produto> root = criteriaQuery.from(Produto.class);

		root.fetch("estoque", JoinType.LEFT);
		root.fetch("categorias", JoinType.LEFT);

//		criteriaQuery.where(criteriaBuilder.isNull(root.get(Produto_.fotoProduto)));
		criteriaQuery.where(criteriaBuilder.isNotNull(root.get(Produto_.fotoProduto)));

		TypedQuery<Produto> typedQuery = manager.createQuery(criteriaQuery);

		List<Produto> recs = typedQuery.getResultList();

		System.out.println();

		if (recs.size() > 0) {
			System.out.println("DATA_INCLUSAO | NOME_PRODUTO | DESCRICAO_PRODUTO | CATEGORIA_PRODUTO");
			recs.forEach(r -> System.out.println(r.getDataInclusaoCadastro() + "|" + r.getNome() + "|"
					+ r.getDescricao() + "|" + r.getCategorias()));
		} else {
			System.out.println("Nenhum registro encontrado");
		}

	}

	@Test
	public void usandoLike() {
		CriteriaBuilder criteriaBuilder = manager.getCriteriaBuilder();

		CriteriaQuery criteriaQuery = criteriaBuilder.createQuery(Produto.class);

		Root<Produto> root = criteriaQuery.from(Produto.class);

		root.fetch("estoque", JoinType.LEFT);
		root.fetch("categorias", JoinType.LEFT);

		criteriaQuery.where(criteriaBuilder.like(root.get(Produto_.nome), "Produto%"));

		TypedQuery<Produto> typedQuery = manager.createQuery(criteriaQuery);

		List<Produto> recs = typedQuery.getResultList();

		System.out.println();
		System.out.println("DATA_INCLUSAO | NOME_PRODUTO | DESCRICAO_PRODUTO | CATEGORIA_PRODUTO");
		recs.forEach(r -> System.out.println(
				r.getDataInclusaoCadastro() + "|" + r.getNome() + "|" + r.getDescricao() + "|" + r.getCategorias()));

	}

	@Test
	public void usandoMetaModel() {

		CriteriaBuilder criteriaBuilder = manager.getCriteriaBuilder();
		CriteriaQuery criteriaQuery = criteriaBuilder.createQuery(Produto.class);
		Root<Produto> root = criteriaQuery.from(Produto.class);

	}

	@Test
	public void usandoParametros() {

		CriteriaBuilder criteriaBuilder = manager.getCriteriaBuilder();

		CriteriaQuery criteriaQuery = criteriaBuilder.createQuery(Produto.class);

		Root<Produto> root = criteriaQuery.from(Produto.class);

		root.fetch("estoque", JoinType.LEFT);
		root.fetch("categorias", JoinType.LEFT);

		ParameterExpression<Date> pData = criteriaBuilder.parameter(Date.class, "pData");

		Calendar data = Calendar.getInstance();

		criteriaQuery.where(criteriaBuilder.greaterThan(root.get("dataInclusaoCadastro"), pData));

		TypedQuery<Produto> typedQuery = manager.createQuery(criteriaQuery);

		typedQuery.setParameter("pData", data.getTime(), TemporalType.TIMESTAMP);

		List<Produto> recs = typedQuery.getResultList();

		System.out.println();
		System.out.println("DATA_INCLUSAO | NOME_PRODUTO | DESCRICAO_PRODUTO | CATEGORIA_PRODUTO");
		recs.forEach(r -> System.out.println(
				r.getDataInclusaoCadastro() + "|" + r.getNome() + "|" + r.getDescricao() + "|" + r.getCategorias()));
		System.out.println("Data de Parâmetro é: " + data.getTime());

	}

	@Test
	public void usandoJoinFetch() {

		CriteriaBuilder criteriaBuilder = manager.getCriteriaBuilder();

		CriteriaQuery criteriaQuery = criteriaBuilder.createQuery(Produto.class);

		Root<Produto> root = criteriaQuery.from(Produto.class);

		root.fetch("estoque", JoinType.LEFT);
		root.fetch("categorias", JoinType.LEFT);

		criteriaQuery.where(criteriaBuilder.equal(root.get("id"), 1));

		TypedQuery<Produto> typedQuery = manager.createQuery(criteriaQuery);
		List<Produto> recs = typedQuery.getResultList();

		System.out.println();
		System.out.println("NOME_PRODUTO | DESCRICAO_PRODUTO | CATEGORIA_PRODUTO");
		recs.forEach(r -> System.out.println(r.getNome() + "|" + r.getDescricao() + "|" + r.getCategorias()));
	}

	@Test
	public void usandoLeftJoin() {

		CriteriaBuilder criteriaBuilder = manager.getCriteriaBuilder();

		CriteriaQuery criteriaQuery = criteriaBuilder.createQuery(Tuple.class);

		Root<Produto> root = criteriaQuery.from(Produto.class);

		Join<Produto, Categoria> joinCategoria = root.join("categorias", JoinType.LEFT);

		joinCategoria.on(criteriaBuilder.equal(joinCategoria.get("nome"), "Eletrônicos"));

		criteriaQuery.multiselect(root.get("nome").alias("nome_produto"),
				root.get("descricao").alias("descricao_produto"), joinCategoria.get("nome").alias("nome_categoria"));

		TypedQuery<Tuple> typedQuery = manager.createQuery(criteriaQuery);
		List<Tuple> recs = typedQuery.getResultList();

		System.out.println();
		System.out.println("NOME_PRODUTO | DESCRICAO_PRODUTO | CATEGORIA_PRODUTO");
		recs.forEach(r -> System.out
				.println(r.get("nome_produto") + "|" + r.get("descricao_produto") + "|" + r.get("nome_categoria")));
	}

	@Test
	public void usandoJoinsComOn() {

		CriteriaBuilder criteriaBuilder = manager.getCriteriaBuilder();

		CriteriaQuery criteriaQuery = criteriaBuilder.createQuery(Tuple.class);

		Root<Produto> root = criteriaQuery.from(Produto.class);

		Join<Produto, Categoria> joinCategoria = root.join("categorias");

		joinCategoria.on(criteriaBuilder.equal(joinCategoria.get("nome"), "Eletrônicos"));

		criteriaQuery.multiselect(root.get("nome").alias("nome_produto"),
				root.get("descricao").alias("descricao_produto"), joinCategoria.get("nome").alias("nome_categoria"));

		TypedQuery<Tuple> typedQuery = manager.createQuery(criteriaQuery);
		List<Tuple> recs = typedQuery.getResultList();

		System.out.println();
		System.out.println("NOME_PRODUTO | DESCRICAO_PRODUTO | CATEGORIA_PRODUTO");
		recs.forEach(r -> System.out
				.println(r.get("nome_produto") + "|" + r.get("descricao_produto") + "|" + r.get("nome_categoria")));
	}

	@Test
	public void usandoJoins() {
		CriteriaBuilder criteriaBuilder = manager.getCriteriaBuilder();

		CriteriaQuery criteriaQuery = criteriaBuilder.createQuery(Tuple.class);

		Root<Produto> root = criteriaQuery.from(Produto.class);

		Join<Produto, Categoria> joinCategoria = root.join("categorias");

		criteriaQuery.select(criteriaBuilder.construct(Tuple.class, root.get("nome").alias("nome_produto"),
				root.get("descricao").alias("descricao_produto"),
				joinCategoria.get("nome").alias("categoria_produto")));

		TypedQuery<Tuple> query = manager.createQuery(criteriaQuery);

		List<Tuple> rec = query.getResultList();

//		rec.forEach(r -> System.out.println(r.getNome() + "|" + r.getCategoriaPai()));
		rec.forEach(r -> System.out
				.println(r.get("nome_produto") + "|" + r.get("descricao_produto") + "|" + r.get("categoria_produto")));
	}

	@Test
	public void retornandoMaisDeUmAtributoComDTO() {
		CriteriaBuilder criteriaBuilder = manager.getCriteriaBuilder();
		CriteriaQuery criteriaQuery = criteriaBuilder.createQuery(ProdutoDTO.class);
		Root<Produto> root = criteriaQuery.from(Produto.class);

		criteriaQuery.select(
				criteriaBuilder.construct(ProdutoDTO.class, root.get("id"), root.get("nome"), root.get("descricao")));

		TypedQuery<ProdutoDTO> typedQuery = manager.createQuery(criteriaQuery);

		List<ProdutoDTO> produtos = typedQuery.getResultList();

		System.out.println();
		System.out.println("ID | PRODUTO | DESCRICAO");

		produtos.forEach(p -> System.out.println(p.getId() + "|" + p.getNome() + "|" + p.getDescricao()));
	}

	@Test
	public void retornandoMaisDeUmAtributoComTupla() {
		CriteriaBuilder criteriaBuilder = manager.getCriteriaBuilder();

		CriteriaQuery criteriaQuery = criteriaBuilder.createQuery(Tuple.class);

		Root<Produto> root = criteriaQuery.from(Produto.class);

//		criteriaQuery.multiselect(root.get("nome"),root.get("descricao"));
		criteriaQuery.multiselect(root.get("nome").alias("nome_produto"), root.get("descricao").alias("desc_produto"));

//		criteriaQuery.where(criteriaBuilder.equal(root.get("id"), 2));

		TypedQuery<Tuple> typedQuery = manager.createQuery(criteriaQuery);

		List<Tuple> tuplas = typedQuery.getResultList();

		System.out.println();
		System.out.println("NOME | DESCRICAO");
//		tuplas.forEach(t-> System.out.println(t.get(0) + "|" + t.get(1)));
		tuplas.forEach(t -> System.out.println(t.get("nome_produto") + "|" + t.get("desc_produto")));
	}

	@Test
	public void retornandoMaisDeUmAtributo() {
		CriteriaBuilder cb = manager.getCriteriaBuilder();

		CriteriaQuery cq = cb.createQuery(Object[].class);

		Root<Produto> root = cq.from(Produto.class);

		cq.multiselect(root.get("id"), root.get("nome"), root.get("descricao"));

		TypedQuery<Object[]> tq = manager.createQuery(cq);

		List<Object[]> res = tq.getResultList();

		System.out.println();
		System.out.println("ID | NOME | DESCRICAO");

		res.forEach(obj -> System.out.println(obj[0] + " - " + obj[1] + " - " + obj[2]));
	}

	@Test
	public void retornandoTodosOsProdutos() {
		CriteriaBuilder cb = manager.getCriteriaBuilder();

		CriteriaQuery cq = cb.createQuery(Produto.class);  

		Root<Produto> root = cq.from(Produto.class);

		TypedQuery<Produto> tq = manager.createQuery(cq);

		List<Produto> produtos = tq.getResultList();

		produtos.forEach(p -> System.out.println(p.getDescricao()));
	}

	@Test
	public void retornandoAtributoEspecifico() {

		CriteriaBuilder cb = manager.getCriteriaBuilder();

		CriteriaQuery cq = cb.createQuery(Produto.class);

		Root<Produto> root = cq.from(Produto.class);

		cq.where(cb.equal(root.get("nome"), "Produto 02"));

		cq.select(root.get("descricao"));

		TypedQuery<String> tq = manager.createQuery(cq);

		System.out.println(tq.getSingleResult());

	}

	@Test
	public void buscaPorId() {

		CriteriaBuilder cb = manager.getCriteriaBuilder();

		CriteriaQuery<Produto> cq = cb.createQuery(Produto.class);

		Root<Produto> root = cq.from(Produto.class);

		cq.select(root);

		cq.where(cb.equal(root.get("nome"), "Produto 01"));

		TypedQuery<Produto> tq = manager.createQuery(cq);

		Produto produto = tq.getSingleResult();

		System.out.println(produto.toString());

	}

}
