package com.algaworks.ecommerce.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
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
@Table(name = "tab_estoque")
public class Estoque {

	@EqualsAndHashCode.Include
	@Id
//	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_produto")
	private Integer id;

	@OneToOne(optional = false)
	@JoinColumn(name = "id_produto")
	@MapsId
	private Produto produto;
	
	@Column(columnDefinition = "integer default 0")
	private Integer quantidade;
	
	@Column(name = "data_inclusao",columnDefinition = "timestamp(14) not null")
	private LocalDateTime dtInclusao;
	
	@Column(name = "data_atualizacao",columnDefinition = "timestamp(14)")
	private LocalDateTime dtAtualizacao;
}
