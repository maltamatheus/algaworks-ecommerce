package com.algaworks.ecommerce.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "tab_categorias")
public class Categoria extends EntidadeBase{

//	@EqualsAndHashCode.Include
//	@Id
//	@GeneratedValue(strategy = GenerationType.IDENTITY)
//	@ToString.Include
//	private Integer id;
	
	@ToString.Include
	private String nome;
	
	@ManyToOne
	@JoinColumn(name = "id_categoria_pai")
	@ToString.Include
	private Categoria categoriaPai;
	
	@OneToMany(mappedBy = "categoriaPai")
	private List<Categoria> categoriasFilho;
	
	@ManyToMany(mappedBy = "categorias")
	private List<Produto> produtos;
}
