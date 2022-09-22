package com.algaworks.ecommerce.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EntityResult;
import javax.persistence.FieldResult;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.SqlResultSetMappings;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

import com.algaworks.ecommerce.dto.ProdutoDTO;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NamedNativeQueries({
	@NamedNativeQuery(name = "listar"
						,query = "select * from tab_produtos order by id"
						,resultClass = Produto.class),
	@NamedNativeQuery(name = "listarComResultSetMapping"
						,query = "select * from tab_teste_produtos order by idx"
						,resultSetMapping = "MapeandoProdutoComFieldResult")	
})
@SqlResultSetMappings({
	@SqlResultSetMapping(name="MapProduto",entities = {@EntityResult(entityClass=Produto.class)}),
	@SqlResultSetMapping(name="MapProdutoCategoria"
							,entities = {@EntityResult(entityClass=Produto.class),
								@EntityResult(entityClass = Categoria.class)}),
	@SqlResultSetMapping(name="MapeandoProdutoComFieldResult"
							,entities = {@EntityResult(entityClass = Produto.class
															,fields = {
																@FieldResult(name = "id", column = "idx"),
																@FieldResult(name = "nome", column = "nome_produto"),
																@FieldResult(name = "dataInclusaoCadastro", column = "dtInclusao"),
																@FieldResult(name = "descricao", column = "descricao_prd"),
																@FieldResult(name = "fotoProduto", column = "foto"),
																@FieldResult(name = "precoCusto", column = "precocusto"),
																@FieldResult(name = "precoVenda", column = "precovenda"),
															})}),
	@SqlResultSetMapping(name = "MapeandoProdutoComColumnResult"
							,classes = {
									@ConstructorResult(targetClass = ProdutoDTO.class
															,columns = {
																	@ColumnResult(name = "idx",type = Integer.class),
																	@ColumnResult(name = "nome_produto", type = String.class),
																	@ColumnResult(name = "descricao_prd", type = String.class)
															})
							})
	})
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NamedQueries({
	@NamedQuery(name = "Produto.listarPorNomeProduto",query = "select p from Produto p where p.nome = :pNome"),
	@NamedQuery(name = "Produto.listarPorNomeCategoria",query = "select p from Produto p join p.categorias c where c.nome = :pNome")
})
@ToString
@Entity
@Table(name = "tab_produtos",uniqueConstraints = @UniqueConstraint(name = "unq_produto01",columnNames = {"nome"}))
@AllArgsConstructor
@NoArgsConstructor
public class Produto extends EntidadeBase{
	
//	@EqualsAndHashCode.Include
//	@Id
//	@GeneratedValue(strategy = GenerationType.IDENTITY)
//	private Integer id;
	
	@Column(length = 100,nullable = false, unique = true)
	private String nome;
	
	@Column(columnDefinition = "varchar(275) not null default 'Insira aqui a descrição do produto' ")
	private String descricao;
	
	@Column(name = "preco_custo_produto",scale = 2, precision = 10)
	private BigDecimal precoCusto;
	
	@Column(name = "preco_venda_produto",scale = 2, precision = 10)
	private BigDecimal precoVenda;
	
	@ManyToMany
	@JoinTable(name = "tab_produtos_categorias"
	          ,joinColumns = @JoinColumn(name = "id_produto",foreignKey = @ForeignKey(name="fk_produto_categoria"),nullable = false)
	          ,inverseJoinColumns = @JoinColumn(name = "id_categoria",foreignKey = @ForeignKey(name="fk_categoria_produto"),nullable = false))
	private List<Categoria> categorias;
	
	@OneToOne(mappedBy = "produto")
	private Estoque estoque;
	
	@ElementCollection
	@CollectionTable(name = "tab_tags_produto",joinColumns = @JoinColumn(name="id_produto",foreignKey = @ForeignKey(name="fk_produto_tagsproduto")))
	@Column(name = "tags_produto")
	private List<String> tagsProduto;
	
	@ElementCollection
	@CollectionTable(name = "tab_caracteristicas"
	                ,joinColumns = @JoinColumn(name = "id_caracteristica",foreignKey = @ForeignKey(name="fk_produto_caracteristicas")))
	@Column(name = "caracteristica")
	private List<Caracteristica> caracteristicas;
	
	@Lob
	@ToString.Exclude
	@Column(name = "foto_produto")
	private byte[] fotoProduto;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "data_inclusao_cadastro" ,columnDefinition = "timestamp(17)")
	private Date dataInclusaoCadastro;

}
