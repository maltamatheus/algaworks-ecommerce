package com.algaworks.ecommerce.model;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
@Entity
@Table(name = "tab_produtos")
public class Produto {
	
	@EqualsAndHashCode.Include
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	private String nome;
	
	private String descricao;
	
	@Column(name = "preco_custo_produto")
	private BigDecimal precoCusto;
	
	@Column(name = "preco_venda_produto")
	private BigDecimal precoVenda;
	
	@ManyToMany
	@JoinTable(name = "tab_produtos_categorias"
	          ,joinColumns = @JoinColumn(name = "id_produto")
	          ,inverseJoinColumns = @JoinColumn(name = "id_categoria"))
	private List<Categoria> categorias;
	
	@OneToOne(mappedBy = "produto")
	private Estoque estoque;

}
