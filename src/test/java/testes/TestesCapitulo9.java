package testes;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Query;
import javax.persistence.TemporalType;
import javax.persistence.TypedQuery;

import org.junit.Test;

import com.algaworks.ecommerce.dto.ProdutoDTO;
import com.algaworks.ecommerce.model.Categoria;
import com.algaworks.ecommerce.model.Produto;

public class TestesCapitulo9 extends EntityManagerTests {

	@Test
	public void carregandoArquivosEmLote() {
		
		List<Produto> produtos = new ArrayList<Produto>();

		try {
		
			File arq = new File("src//main//resources//files//ArqCargaProdutos.txt");

			BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(arq)));
			
			String l = null;
			
			while((l = reader.readLine()) != null) {
				Produto p = new Produto();

				p.setNome(l.split(";")[0]);
				p.setDescricao(l.split(";")[1]);
				p.setPrecoVenda(new BigDecimal(Long.valueOf(l.split(";")[2])));
				
				produtos.add(p);
			}
			
			reader.close();
			
			int inserts = 0;

			manager.getTransaction().begin();
			
			for (Produto produto : produtos) {
				
				manager.persist(produto);
				
				inserts++;
				
				if(inserts < 5) {
					manager.getTransaction().commit();
					
					inserts = 0;
				}

			}
			manager.getTransaction().commit(); //Commit das últimas linhas
			
			manager.close();
			
		} catch (FileNotFoundException e) {
			
			e.printStackTrace();
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		
	}
	
	@Test
	public void usandoSubQueryComAny() {
		
		String jpql = "select p.descricao, c.nome "
				+ "from Produto p join p.categorias c "
				+ "where c.nome = any (select nome from Categoria)";
		
		TypedQuery<Object[]> query = manager.createQuery(jpql, Object[].class);
//		query.setParameter("categorias", categorias);
		List<Object[]> res = query.getResultList();
		
		res.forEach(r-> {
			int i = 0;
			System.out.println(r[i++] + " | " + r[i++]);
		});
	}
	
	@Test
	public void usandoSubQueryComAll() {
		
//		String jpqlLista = "select c.nome from Categoria c where c.id in (1,3)";
//		TypedQuery<String> queryCategorias = manager.createQuery(jpqlLista, String.class);
//		List<String> categorias = queryCategorias.getResultList();

		String jpql = "select p.descricao, c.nome "
				+ "from Produto p join p.categorias c "
//				+ "where c.nome = all (select nome from Categoria where nome in (:categorias)) ";
				+ "where c.nome = all (select nome from Categoria where id = 1)";
		
		TypedQuery<Object[]> query = manager.createQuery(jpql, Object[].class);
//		query.setParameter("categorias", categorias);
		List<Object[]> res = query.getResultList();
		
		res.forEach(r-> {
			int i = 0;
			System.out.println(r[i++] + " | " + r[i++]);
		});
	}
	
	@Test
	public void usandoSubQueryComExists() {
		
		String jpqlLista = "select c.nome from Categoria c where c.id in (1,3)";
		TypedQuery<String> queryCategorias = manager.createQuery(jpqlLista, String.class);
		List<String> categorias = queryCategorias.getResultList();

		String jpql = "select p.descricao, c.nome "
				+ "from Produto p join p.categorias c "
				+ "where exists (select nome from Categoria where nome in (:categorias)) ";
		
		TypedQuery<Object[]> query = manager.createQuery(jpql, Object[].class);
		query.setParameter("categorias", categorias);
		List<Object[]> res = query.getResultList();
		
		res.forEach(r-> {
			int i = 0;
			System.out.println(r[i++] + " | " + r[i++]);
		});
	}
	
	@Test
	public void funcaoDistinct() {
		
		String jpql = "select distinct c.nome from Categoria c join c.produtos";
		TypedQuery<String> query = manager.createQuery(jpql, String.class);
		List<String> res = query.getResultList();
		
		res.forEach(r -> System.out.println(r));
	}
	
	@Test
	public void funcaoIn() {
		
		String jpqlLista = "select c.nome from Categoria c where c.id in (1,3)";
		TypedQuery<String> queryCategorias = manager.createQuery(jpqlLista, String.class);
		List<String> categorias = queryCategorias.getResultList();

		String jpql = "select p.descricao, c.nome "
				+ "from Produto p join p.categorias c "
				+ "where c.nome in (:categorias) ";
		
		TypedQuery<Object[]> query = manager.createQuery(jpql, Object[].class);
		query.setParameter("categorias", categorias);
		List<Object[]> res = query.getResultList();
		
		res.forEach(r-> {
			int i = 0;
			System.out.println(r[i++] + " | " + r[i++]);
		});
	}
	
	@Test
	public void funcaoCase() {

		String jpql = "select p.descricao, "
				+ "case when c.nome like 'Eletr%' then 'PRECO TEM QUE SER MÉDIO'"
				+ "     when c.nome like 'Acessórios%' then 'PRECO TEM QUE SER BARATO' "
				+ " else 'PRECO JÁ É MAIS ALTO' end as tipoPreco from Produto p join p.categorias c";
		
		TypedQuery<Object[]> query = manager.createQuery(jpql, Object[].class);
		List<Object[]> res = query.getResultList();
		
		res.forEach(r-> {
			int i = 0;
			System.out.println(r[i++] + " | " + r[i++]);
		});
	}

	@Test
	public void funcoesAgregadorasHaving() {

		String jpql = "select c.nome, sum(p.precoVenda) "
				+ "from Categoria c join c.produtos p group by c.nome"
				+ " having sum(p.precoVenda) > (select sum(pr.precoVenda) "
				+ "from Produto pr join pr.categorias ca where ca.nome like 'Acessórios%')";
		
		TypedQuery<Object[]> query = manager.createQuery(jpql, Object[].class);
		List<Object[]> res = query.getResultList();
		
		res.forEach(r-> {
			int i = 0;
			System.out.println(r[i++] + " | " + r[i++]); 
		});
	}
	
	@Test
	public void funcoesAgregadorasGroupBy() {
		//avg, sum,count,max,min
		String jpql = "select c.nome, sum(p.precoVenda) "
				+ "from Categoria c join c.produtos p group by c.nome";
		
		TypedQuery<Object[]> query = manager.createQuery(jpql, Object[].class);
		List<Object[]> res = query.getResultList();
		
		res.forEach(r-> {
			int i = 0;
			System.out.println(r[i++] + " | " + r[i++]);
		});
	}
	
	@Test
	public void funcoesAgregadoras() {
		//avg, sum,count,max,min
		String jpql = "select avg(p.precoVenda), sum(p.precoVenda),count(p.precoVenda),max(p.precoVenda),min(p.precoVenda) from Produto p";
		
		TypedQuery<Object[]> query = manager.createQuery(jpql, Object[].class);
		List<Object[]> res = query.getResultList();
		
		res.forEach(r-> {
			int i = 0;
			System.out.println(r[i++] + " | " + r[i++] + " | " + r[i++] + " | " + r[i++] + " | " + r[i++]);
		});
	}

	@Test
	public void funcoesNumericas() {
		
//		String jpql = "select size(p.categorias), p.id, 2 from Produto p";
		String jpql = "select size(c.produtos) ,c.id, 2 from Categoria c";
		
		TypedQuery<Object[]> query = manager.createQuery(jpql, Object[].class);
//		TypedQuery<Produto> query = manager.createQuery(jpql, Produto.class);
//		TypedQuery<Categoria> query = manager.createQuery(jpql, Categoria.class);
		
		List<Object[]> res = query.getResultList();
//		List<Produto> res = query.getResultList();
//		List<Categoria> res = query.getResultList();
		
		res.forEach(r -> System.out.println(r[0] + " | " + r[1] + " | " + r[2]));
//		res.forEach(r -> System.out.println(r.getNome() + " | " + r.getDescricao() + " | " + r.getDataInclusaoCadastro()));
	}
	
	@Test
	public void funcoesData() {
		
		String jpql = "select hour(p.dataInclusaoCadastro), minute(dataInclusaoCadastro), second(dataInclusaoCadastro) from Produto p";
		
		TypedQuery<Object[]> query = manager.createQuery(jpql, Object[].class);
//		TypedQuery<Produto> query = manager.createQuery(jpql, Produto.class);
		
		List<Object[]> res = query.getResultList();
//		List<Produto> res = query.getResultList();
		
		res.forEach(r -> System.out.println(r[0] + " | " + r[1] + " | " + r[2]));
//		res.forEach(r -> System.out.println(r.getNome() + " | " + r.getDescricao() + " | " + r.getDataInclusaoCadastro()));
	}

	@Test
	public void funcoesString() {
		StringBuilder sql = new StringBuilder();
		
		sql.append("select c.nome            ");
//		sql.append("     ,upper(c.nome) ");
//		sql.append("     ,concat('|',concat('   ',c.nome,'   '),'|') ");
//		sql.append("     ,concat('|',trim(concat('   ',c.nome,'   ')),'|') ");
//		sql.append("     ,length(c.nome) ");
//		sql.append("     ,locate('A',c.nome)");
//		sql.append("     ,substring(c.nome,-3,3) ");
		sql.append("     ,substring(c.nome,3) ");
//		sql.append("     ,substring(c.nome,3,3) ");
		sql.append("from Categoria c");
		
		String jpql = sql.toString();

		TypedQuery<Object[]> tq = manager.createQuery(jpql, Object[].class);
		
		List<Object[]> res = tq.getResultList();
		
		for (Object[] o : res) {
			
			int idx = 0;
			System.out.println(o[idx++] + " - " + o[idx++]);
		}
	}

	@Test
	public void testePaginacao() {
		StringBuilder sql = new StringBuilder();
		sql.append("select prd.nome ");
		sql.append(",prd.descricao ");
		sql.append(",cat.nome ");
		sql.append(",ctp.nome "); // Categoria Pai
		sql.append(",ctf.nome "); // Categoria Filho
		sql.append("from Produto prd ");
		sql.append("right join prd.categorias cat ");
		sql.append("left join cat.categoriaPai ctp ");
		sql.append("left  join cat.categoriasFilho ctf ");
		sql.append("order by prd.id ");

		String jpql = sql.toString();

		TypedQuery<Object[]> objetos = manager.createQuery(jpql, Object[].class);

		// Fórmula: IdxInicio = totalPorPagina * (numPagina - 1)
		/*
		 * 5ª pag / Total por página = 5 / IdxInicio = 36
		 */

		for (int i = 1; i <= objetos.getResultList().size(); i++) {
			
			int idx1 = 2 * (i - 1);

			objetos.setFirstResult(idx1); // Seta o início da collection (ou subcollection) a ser exibida
			objetos.setMaxResults(2); // Seta o total de registros a ser trazido na collection

			List<Object[]> res = objetos.getResultList();

			for (Object[] objects : res) {
				int chave = 0;
				System.out.println(objects[chave++] + " - " + objects[chave++]);
			}
			
			System.out.println("------------ próxima volta -----------");
		}
	}

	@Test
	public void testeOperadoresLogicos() {
		String jpql = "select p.nome from Produto p right join p.categorias cat";

		TypedQuery<Produto> tq = manager.createQuery(jpql, Produto.class);

		List<Produto> produtos = tq.getResultList();

		for (Produto prd : produtos) {

			System.out.println(prd.getDescricao());

		}
	}

	@Test
	public void testeUsandoMaiorMenorComDatas() {
		String jpql = "select p from Produto p where p.dataInclusaoCadastro > :pDataIni or p.dataInclusaoCadastro < :pDataFim";

		TypedQuery<Produto> tq = manager.createQuery(jpql, Produto.class);

		List<Produto> res = tq.setParameter("pDataIni", new Date()).setParameter("pDataFim", new Date())
				.getResultList();

		if (!(res.size() <= 0) || !res.isEmpty()) {

			for (Produto prd : res) {

				System.out.println(prd.getDescricao());

			}
		} else {
			System.out.println("Sua busca não encontrou resultados");
		}
	}

	@Test
	public void testeUsandoMaiorMenor() {
		String jpql = "select p from Produto p where coalesce(p.precoVenda,0) >= coalesce(:precoCusto,0)";

		TypedQuery<Produto> tq = manager.createQuery(jpql, Produto.class);

		List<Produto> res = tq.setParameter("precoCusto", new BigDecimal("1000")).getResultList();

		if (!(res.size() <= 0) || !res.isEmpty()) {

			for (Produto prd : res) {

				System.out.println(prd.getDescricao());

			}
		} else {
			System.out.println("Sua busca não encontrou resultados");
		}
	}

	@Test
	public void testeUsandoIsEmpty() {
		String jpql = "select c from Categoria c where c.produtos is empty";

		TypedQuery<Produto> tq = manager.createQuery(jpql, Produto.class);

		List<Produto> res = tq.getResultList();

		for (Produto cat : res) {

			System.out.println(cat.getNome());

		}
	}

	@Test
	public void testeUsandoIsNull() {
		String jpql = "select c from Categoria c where c.categoriaPai is null";

		TypedQuery<Produto> tq = manager.createQuery(jpql, Produto.class);

		List<Produto> res = tq.getResultList();

		for (Produto cat : res) {

			System.out.println(cat.getNome());

		}
	}

	@Test
	public void testeUsandoLike() {
		String jpql = "select p.descricao, p.dataInclusaoCadastro " + "from Produto p "
				+ "where p.descricao like concat('%',:nome,'%') " + "or p.id = :idProduto "
				+ "and p.dataInclusaoCadastro <= :dataInclusaoCadastro";

		TypedQuery<Object[]> tq = manager.createQuery(jpql, Object[].class);

//		tq.setParameter(1, "Kindle");

		List<Object[]> res = tq.setParameter("nome", "Apple").setParameter("idProduto", 3)
				.setParameter("dataInclusaoCadastro", new Date(), TemporalType.TIMESTAMP).getResultList();

		for (Object[] o : res) {
			int chave = 0;

			String descricao = (String) o[chave++];
			Date data = (Date) o[chave++];

			System.out.println(descricao + " - " + data);

		}
	}

	@Test
	public void testeUsandoParametrosDeData() {
		String jpql = "select p.descricao, p.dataInclusaoCadastro " + "from Produto p " + "where p.nome = :nome or "
				+ "p.id = :idProduto " + "and p.dataInclusaoCadastro <= :dataInclusaoCadastro";

		TypedQuery<Object[]> tq = manager.createQuery(jpql, Object[].class);

//		tq.setParameter(1, "Kindle");

		List<Object[]> res = tq.setParameter("nome", "IPhone 12").setParameter("idProduto", 3)
				.setParameter("dataInclusaoCadastro", new Date(), TemporalType.TIMESTAMP).getResultList();

		int chave = 0;

		for (Object[] o : res) {

			String descricao = (String) o[chave++];
			Date data = (Date) o[chave++];

			System.out.println(descricao + " - " + data);

		}
	}

	@Test
	public void testeUsandoParametros() {
		String jpql = "select p.descricao from Produto p where p.nome = :nome or p.id = :idProduto ";

		TypedQuery<String> tq = manager.createQuery(jpql, String.class);

//		tq.setParameter(1, "Kindle");

		List<String> res = tq.setParameter("nome", "IPhone 12").setParameter("idProduto", 3).getResultList();

		for (String o : res) {

			System.out.println(o);

		}
	}

	@Test
	public void testePathExpressions() {
		String jpql = "select p.categorias.nome from Produto p where p.id = 1 ";

		TypedQuery<Object[]> tq = manager.createQuery(jpql, Object[].class);

		List<Object[]> res = tq.getResultList();

		for (Object[] o : res) {

			System.out.println(o[0]);

		}
	}

	@Test
	public void testeFetchJoin() {

		StringBuilder sql = new StringBuilder();

		sql.append(" select prd                       ");
		sql.append(" from Produto prd                 ");
		sql.append("    left join fetch prd.categorias cat  ");

		TypedQuery<Produto> tq = manager.createQuery(sql.toString(), Produto.class);

		List<Produto> produtos = tq.getResultList();

		for (Produto prd : produtos) {

			int i = 0;

			Categoria cat = prd.getCategorias().get(i);
			System.out.println(cat.getNome());
			i++;
		}

	}

	@Test
	public void getSelectComMaisUmaEntidade() {
		StringBuilder sql = new StringBuilder();
		sql.append("select prd.nome ");
		sql.append(",prd.descricao ");
		sql.append(",cat.nome ");
		sql.append(",ctp.nome "); // Categoria Pai
		sql.append(",ctf.nome "); // Categoria Filho
		sql.append("from Produto prd ");
		sql.append("right join prd.categorias cat ");
		sql.append("left join cat.categoriaPai ctp ");
		sql.append("left  join cat.categoriasFilho ctf ");

		String jpql = sql.toString();

		TypedQuery<Object[]> objetos = manager.createQuery(jpql, Object[].class);

		List<Object[]> res = objetos.getResultList();

		System.out.println();

		int volta = 1;

		for (Object[] o : res) {

			int chave = 0;

			String produto = (String) o[chave++];
//			System.out.println(chave++);
			String descricao = (String) o[chave++];
//			System.out.println(chave++);
			String categoria = (String) o[chave++];
//			System.out.println(chave++);
			String categoriaPai = (String) o[chave++];
//			System.out.println(chave++);
			String categoriaFilho = (String) o[chave++];
//			System.out.println(chave++);

			produto = (produto != null) ? produto : "Sem Produto";
			descricao = (descricao != null) ? descricao : "Sem Descrição";
			categoria = (categoria != null) ? categoria : "Sem Categoria";
			categoriaPai = (categoriaPai != null) ? categoriaPai : "Sem Categoria Pai";
			categoriaFilho = (categoriaFilho != null) ? categoriaFilho : "Sem Categoria Filho";

			System.out.println(
					produto + "- " + descricao + "- " + categoria + "- " + categoriaPai + "- " + categoriaFilho);
			volta++;
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
			descricao = (descricao != null) ? descricao : "Sem Descrição";
			categoria = (categoria != null) ? categoria : "Sem Categoria";

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
			descricao = (descricao != null) ? descricao : "Sem Descrição";
			categoria = (categoria != null) ? categoria : "Sem Categoria";

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
			System.out.println(objects[linha++] + " - " + objects[linha++] + " - " + objects[linha++] + " - "
					+ objects[linha++] + " - " + objects[linha++]);

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
