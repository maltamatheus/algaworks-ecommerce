package testes;

import java.util.List;

import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.junit.Test;

import com.algaworks.ecommerce.dto.ProdutoDTO;
import com.algaworks.ecommerce.model.Produto;

public class TestesCapitulo9 extends EntityManagerTests {

	@Test
	public void getSelectComMaisUmaEntidade() {
		StringBuilder sql = new StringBuilder();
		sql.append("select prd.nome, ");
		sql.append("prd.descricao, ");
		sql.append("cat.nome,");
		sql.append("ctp.nome");
		sql.append("from Produto prd ");
		sql.append("right join prd.categorias cat ");
		sql.append("right join Categoria ctp ");
		
		String jpql = sql.toString();
		
		TypedQuery<Object[]> objetos = manager.createQuery(jpql, Object[].class);
		
		List<Object[]> res = objetos.getResultList();
		
		for (Object[] o : res) {
			
			int chave = 0;
			
			String produto = (String) o[chave++];
			String descricao = (String) o[chave++];
			String categoria = (String) o[chave++];
			String categoriaPai = (String) o[chave++];
			
			produto = (produto != null) ? produto : "Sem Produto"; 
			descricao = (descricao!= null) ? descricao : "Sem Descrição";
			categoria = (categoria!= null) ? categoria : "Sem Categoria";
			categoriaPai = (categoriaPai!= null) ? categoriaPai : "Sem Categoria Pai";
			
			System.out.println(produto + "- " + descricao + "- " + categoria + "- " + categoriaPai);
		}
	}
	
	@Test
	public void getSelectComRightJoin() {
		StringBuilder sql = new StringBuilder();
		sql.append("select prd.nome, ");
		sql.append("prd.descricao, ");
		sql.append("cat.nome ");
		sql.append("from Produto prd ");
		sql.append("right join prd.categorias cat ");
		
		String jpql = sql.toString();
		
		TypedQuery<Object[]> objetos = manager.createQuery(jpql, Object[].class);
		
		List<Object[]> res = objetos.getResultList();
		
		for (Object[] o : res) {
			
			int chave = 0;
			
			String produto = (String) o[chave++];
			String descricao = (String) o[chave++];
			String categoria = (String) o[chave++];
			
			produto = (produto != null) ? produto : "Sem Produto"; 
			descricao = (descricao!= null) ? descricao : "Sem Descrição";
			categoria = (categoria!= null) ? categoria : "Sem Categoria";
			
			System.out.println(produto + "- " + descricao + "- " + categoria);
		}
	}
	
	@Test
	public void getSelectComLeftJoin() {
		StringBuilder sql = new StringBuilder();
		sql.append("select prd.nome, ");
		sql.append("prd.descricao, ");
		sql.append("cat.nome ");
		sql.append("from Produto prd ");
		sql.append("left join prd.categorias cat ");
		
		String jpql = sql.toString();
		
		TypedQuery<Object[]> objetos = manager.createQuery(jpql, Object[].class);
		
		List<Object[]> res = objetos.getResultList();
		
		for (Object[] o : res) {
			
			int chave = 0;
			
			String produto = (String) o[chave++];
			String descricao = (String) o[chave++];
			String categoria = (String) o[chave++];
			
			produto = (produto != null) ? produto : "Sem Produto"; 
			descricao = (descricao!= null) ? descricao : "Sem Descrição";
			categoria = (categoria!= null) ? categoria : "Sem Categoria";
			
			System.out.println(produto + "- " + descricao + "- " + categoria);
		}
	}
	
	@Test
	public void getSelectComInnerJoin() {
		
		StringBuilder sql = new StringBuilder();
		
		sql.append("select itm.idPedido                           ");
		sql.append("      ,cli.nome||' '||cli.sobrenome           ");
		sql.append("      ,prd.nome                               ");
		sql.append("      ,prd.precoCusto                         ");
		sql.append("      ,prd.precoVenda * itm.quantidade        ");
		sql.append("from ItemPedido itm                           ");
		sql.append("   join Pedido ped on itm.idPedido   = ped.id ");
		sql.append("   join Produto prd on itm.idProduto = prd.id ");
		sql.append("   join Cliente cli on ped.cliente   = cli.id ");
		
		String jpql = sql.toString();
		
		TypedQuery<Object[]> typedQuery = manager.createQuery(jpql, Object[].class);
		
		List<Object[]> resultado = typedQuery.getResultList();
		
		for (Object[] objects : resultado) {
			int linha = 0;
			System.out.println(objects[linha++] + " - " + objects[linha++] + " - " + 
			                   objects[linha++] + " - " + objects[linha++] + " - " + 
					           objects[linha++]);
			
		}
	}

	@Test
	public void getObjectDTO() {

		String jpql = "select new com.algaworks.ecommerce.dto.ProdutoDTO(p.id,p.nome,p.descricao) from Produto p";

		TypedQuery<ProdutoDTO> typedQuery = manager.createQuery(jpql, ProdutoDTO.class);
		
		List<ProdutoDTO> lista = typedQuery.getResultList();
		
		lista.forEach(p -> System.out.println(p.getId() + " - " + p.getNome() + " - " + p.getDescricao()));

	}
	
	@Test
	public void getObjectList() {

		String jpql = "select id, nome,descricao from Produto p";

		TypedQuery<Object[]> typedQuery = manager.createQuery(jpql, Object[].class);
		
		List<Object[]> lista = typedQuery.getResultList();
		
		for (Object[] objects : lista) {
			int i = 0;
			System.out.println(objects[i++] + " - " + objects[i++] + " - " + objects[i++]);
		}

	}
	
	@Test
	public void getAtributoFromTypedQuery() {

		String jpql = "select p from Produto p";

		TypedQuery<Produto> typedQuery = manager.createQuery(jpql, Produto.class);
		
		List<Produto> produto = typedQuery.getResultList();
		
		produto.forEach(p -> System.out.println(p.toString()));

	}

	@Test
	public void diferencaQueryTypedQuery() {
		if (1 == 1) {

			TypedQuery<Produto> typedQuery = manager.createQuery("select p from Produto p where p.id = 2",
					Produto.class);
			Produto produto = typedQuery.getSingleResult();
			System.out.println("Com TypedQuery " + produto.getDescricao());
		}

		Query query = manager.createQuery("select p from Produto p where p.id = 2", Produto.class);
		Produto produto = (Produto) query.getSingleResult();
		System.out.println("Com Query " + produto.getDescricao());
	}

	@Test
	public void primeiraConsultaJpql() {

		TypedQuery<Produto> typedQuery = manager.createQuery("select p from Produto p where p.id = 1", Produto.class);

		Produto produto = typedQuery.getSingleResult();

		System.out.println(produto.toString());
	}

}