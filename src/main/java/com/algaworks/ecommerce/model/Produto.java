package com.algaworks.ecommerce.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
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
