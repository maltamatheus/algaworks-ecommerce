package com.algaworks.ecommerce.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
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
@Table(name = "tab_notas_fiscais")
public class NotaFiscal {

	@EqualsAndHashCode.Include
	@Id
//	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_pedido")
	private Integer id;

	@MapsId
	@ManyToOne(optional = false)
	@JoinColumn(name = "id_pedido")//,insertable = false,updatable = false)
	private Pedido pedido;
	
	private String xml;
	
	@Column(name = "data_emissao_nf")
	private Date dataEmissao;
	
}
