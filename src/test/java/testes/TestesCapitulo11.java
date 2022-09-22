package testes;

import java.util.List;

import javax.persistence.Query;

import org.junit.Test;

import com.algaworks.ecommerce.dto.ProdutoDTO;
import com.algaworks.ecommerce.model.Produto;

public class TestesCapitulo11 extends EntityManagerTests{
	
	@Test
	public void queryNativaComNamedNativeQueryComResultSetMapping() {
		
		Query query = manager.createNamedQuery("listarComResultSetMapping");
		
		List<Produto> prds = query.getResultList();
		
		prds.forEach(p -> System.out.println(String.format("Produto => %d | %s | %s", p.getId(), p.getNome(), p.getDescricao())));
	}
	
	@Test
	public void queryNativaComNamedNativeQuery() {
		
		Query query = manager.createNamedQuery("listar");
		
		List<Produto> prds = query.getResultList();
		
		prds.forEach(p -> System.out.println(String.format("Produto => %d | %s | %s", p.getId(), p.getNome(), p.getDescricao())));
	}

	@Test
	public void queryNativaComColumnResultRetornandoDTO(){
		String sql = "select * from tab_teste_produtos order by idx";
		
		Query query = manager.createNativeQuery(sql, "MapeandoProdutoComColumnResult");
		
		List<ProdutoDTO> prds = query.getResultList();
		
		prds.forEach(p -> System.out.println(String.format("ProdutoDTO => %d | %s | %s", p.getId(), p.getNome(), p.getDescricao())));
	}
	
	@Test
	public void queryNativaComFieldResult(){
		String sql = "select * from tab_teste_produtos";
		
		Query query = manager.createNativeQuery(sql, "MapeandoProdutoComFieldResult");
		
		List<Produto> prds = query.getResultList();
		
		prds.forEach(p -> System.out.println(p.getNome()));
	}
	
	@Test
	public void queryNativaComSqlResultSetMapping2() {
		
		String sql = "select p.*, c.* from tab_produtos p, tab_produtos_categorias tpc, tab_categorias c where p.id = tpc.id_produto and c.id = tpc.id_categoria";
		Query query = manager.createNativeQuery(sql, "MapProdutoCategoria");
		
		List<Object[]> prods = query.getResultList();
		
		prods.forEach(p-> System.out.println(String.format("Resultado: %s", p[0])));
	}
	
	@Test
	public void queryNativaComSqlResultSetMapping1() {
		
		String sql = "select * from tab_produtos";
		Query query = manager.createNativeQuery(sql, "MapProduto");
		
		List<Produto> prods = query.getResultList();
		
		prods.forEach(p-> System.out.println(String.format("Resultado: %s | %s", p.getNome(), p.getDescricao())));
	}
	
	@Test
	public void queryNativaComParametro() {
		String sql = "select nome, descricao from tab_produtos where nome like concat(:nome,'%')";
		
		Query query = manager.createNativeQuery(sql);
		
		query.setParameter("nome", "Produto");
		
		List<Object[]> res = query.getResultList();
		
		res.forEach(r -> System.out.println(String.format("Resultado - %s | %s",r[0],r[1])));
	}
	
	@Test
	public void queryNativaRetornaEntidade() {
		String sql = "select id, nome, data_inclusao_cadastro,descricao,foto_produto,preco_custo_produto,preco_venda_produto from tab_produtos";
		Query query = manager.createNativeQuery(sql, Produto.class);
		
		List<Produto> prods = query.getResultList();
		
		prods.forEach(p-> System.out.println(p.getNome()));
	}
	
	@Test
	public void executarQueryNativaSimples() {
		
		String sql = "select id, nome from tab_produtos order by 1";
		
		Query query = manager.createNativeQuery(sql);
		
		List<Object[]> res = query.getResultList();
		
		res.forEach(r-> System.out.printf("Resultado: ID %d e Nome Produto é %s \n",r[0],r[1]));

	}
	

}
