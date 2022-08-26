package testes;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.TemporalType;
import javax.persistence.Tuple;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Root;

import org.junit.Test;

import com.algaworks.ecommerce.dto.ProdutoDTO;
import com.algaworks.ecommerce.model.Categoria;
import com.algaworks.ecommerce.model.Produto;

public class TestesCapitulo10 extends EntityManagerTests{
	
	@Test
	public void usandoLike() {
		CriteriaBuilder criteriaBuilder = manager.getCriteriaBuilder();
		
		CriteriaQuery criteriaQuery = criteriaBuilder.createQuery(Produto.class);
		
		Root<Produto> root = criteriaQuery.from(Produto.class);
		
		root.fetch("estoque",JoinType.LEFT);
		root.fetch("categorias",JoinType.LEFT);

		criteriaQuery.where(criteriaBuilder.like(root.get("nome"), "Produto%"));
		
		TypedQuery<Produto> typedQuery = manager.createQuery(criteriaQuery);
		
		List<Produto> recs = typedQuery.getResultList();
		
		System.out.println();
		System.out.println("DATA_INCLUSAO | NOME_PRODUTO | DESCRICAO_PRODUTO | CATEGORIA_PRODUTO");
		recs.forEach(r -> System.out.println(r.getDataInclusaoCadastro() + "|" + r.getNome() + "|" + r.getDescricao() + "|" + r.getCategorias()));
		
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
		
		root.fetch("estoque",JoinType.LEFT);
		root.fetch("categorias",JoinType.LEFT);

		ParameterExpression<Date> pData = criteriaBuilder.parameter(Date.class, "pData");
		
		Calendar data = Calendar.getInstance();
		
		criteriaQuery.where(criteriaBuilder.greaterThan(root.get("dataInclusaoCadastro"), pData));
		
		TypedQuery<Produto> typedQuery = manager.createQuery(criteriaQuery);
		
		typedQuery.setParameter("pData", data.getTime(),TemporalType.TIMESTAMP);
		
		List<Produto> recs = typedQuery.getResultList();
		
		System.out.println();
		System.out.println("DATA_INCLUSAO | NOME_PRODUTO | DESCRICAO_PRODUTO | CATEGORIA_PRODUTO");
		recs.forEach(r -> System.out.println(r.getDataInclusaoCadastro() + "|" + r.getNome() + "|" + r.getDescricao() + "|" + r.getCategorias()));
		System.out.println("Data de Parâmetro é: " + data.getTime());
		
	}

	@Test
	public void usandoJoinFetch() {
	
		CriteriaBuilder criteriaBuilder = manager.getCriteriaBuilder();
		
		CriteriaQuery criteriaQuery = criteriaBuilder.createQuery(Produto.class);
		
		Root<Produto> root = criteriaQuery.from(Produto.class);
		
		root.fetch("estoque",JoinType.LEFT);
		root.fetch("categorias",JoinType.LEFT);
		
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
		
		Join<Produto,Categoria> joinCategoria = root.join("categorias",JoinType.LEFT);
		
		joinCategoria.on(criteriaBuilder.equal(joinCategoria.get("nome"),"Eletrônicos"));
		
		criteriaQuery.multiselect(root.get("nome").alias("nome_produto"),
								  root.get("descricao").alias("descricao_produto"),
								  joinCategoria.get("nome").alias("nome_categoria"));
		
		TypedQuery<Tuple> typedQuery = manager.createQuery(criteriaQuery);
		List<Tuple> recs = typedQuery.getResultList();
		
		System.out.println();
		System.out.println("NOME_PRODUTO | DESCRICAO_PRODUTO | CATEGORIA_PRODUTO");
		recs.forEach(r -> System.out.println(r.get("nome_produto") + "|" + r.get("descricao_produto") + "|" + r.get("nome_categoria")));
	}
	
	@Test
	public void usandoJoinsComOn() {
	
		CriteriaBuilder criteriaBuilder = manager.getCriteriaBuilder();
		
		CriteriaQuery criteriaQuery = criteriaBuilder.createQuery(Tuple.class);
		
		Root<Produto> root = criteriaQuery.from(Produto.class);
		
		Join<Produto,Categoria> joinCategoria = root.join("categorias");
		
		joinCategoria.on(criteriaBuilder.equal(joinCategoria.get("nome"),"Eletrônicos"));
		
		criteriaQuery.multiselect(root.get("nome").alias("nome_produto"),
								  root.get("descricao").alias("descricao_produto"),
								  joinCategoria.get("nome").alias("nome_categoria"));
		
		TypedQuery<Tuple> typedQuery = manager.createQuery(criteriaQuery);
		List<Tuple> recs = typedQuery.getResultList();
		
		System.out.println();
		System.out.println("NOME_PRODUTO | DESCRICAO_PRODUTO | CATEGORIA_PRODUTO");
		recs.forEach(r -> System.out.println(r.get("nome_produto") + "|" + r.get("descricao_produto") + "|" + r.get("nome_categoria")));
	}
	
	@Test
	public void usandoJoins() {
		CriteriaBuilder criteriaBuilder = manager.getCriteriaBuilder();
		
		CriteriaQuery criteriaQuery = criteriaBuilder.createQuery(Tuple.class);
		
		Root<Produto> root = criteriaQuery.from(Produto.class);
		
		Join<Produto,Categoria> joinCategoria = root.join("categorias");
		
		criteriaQuery.select(criteriaBuilder.construct(Tuple.class, root.get("nome").alias("nome_produto")
				                                                  , root.get("descricao").alias("descricao_produto")
				                                                  ,joinCategoria.get("nome").alias("categoria_produto")));
		
		TypedQuery<Tuple> query = manager.createQuery(criteriaQuery);
		
		List<Tuple> rec = query.getResultList();
		
//		rec.forEach(r -> System.out.println(r.getNome() + "|" + r.getCategoriaPai()));
		rec.forEach(r -> System.out.println(r.get("nome_produto") + "|" + r.get("descricao_produto") + "|" + r.get("categoria_produto")));
	}
	
	@Test
	public void retornandoMaisDeUmAtributoComDTO() {
		CriteriaBuilder criteriaBuilder = manager.getCriteriaBuilder();
		CriteriaQuery criteriaQuery = criteriaBuilder.createQuery(ProdutoDTO.class);
		Root<Produto> root = criteriaQuery.from(Produto.class);
		
		criteriaQuery.select(criteriaBuilder.construct(ProdutoDTO.class, root.get("id"),root.get("nome"),root.get("descricao")));
		
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
		criteriaQuery.multiselect(root.get("nome").alias("nome_produto"),root.get("descricao").alias("desc_produto"));
		
//		criteriaQuery.where(criteriaBuilder.equal(root.get("id"), 2));
		
		TypedQuery<Tuple> typedQuery = manager.createQuery(criteriaQuery);
		
		List<Tuple> tuplas = typedQuery.getResultList();

		System.out.println();
		System.out.println("NOME | DESCRICAO");
//		tuplas.forEach(t-> System.out.println(t.get(0) + "|" + t.get(1)));
		tuplas.forEach(t-> System.out.println(t.get("nome_produto") + "|" + t.get("desc_produto")));
	}

	@Test
	public void retornandoMaisDeUmAtributo() {
		CriteriaBuilder cb = manager.getCriteriaBuilder();
		
		CriteriaQuery cq = cb.createQuery(Object[].class);
		
		Root<Produto> root = cq.from(Produto.class);
		
		cq.multiselect(root.get("id"),root.get("nome"),root.get("descricao"));
		
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
