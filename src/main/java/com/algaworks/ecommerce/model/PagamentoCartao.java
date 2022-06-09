package com.algaworks.ecommerce.model;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.algaworks.ecommerce.enums.EnumStatusPagamento;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@DiscriminatorValue(value = "CARTAO")
//@Table(name = "tab_pagamento_cartao")
public class PagamentoCartao extends Pagamento{

//	@EqualsAndHashCode.Include
//	@Id
//	@GeneratedValue(strategy = GenerationType.IDENTITY)
//	private Integer id;
//
//	@OneToOne(optional = false)
//	@JoinColumn(name = "id_pedido")
//	private Pedido pedido;
//	
//	private EnumStatusPagamento status;
	
	@Column(name = "numero_cartao")
	private String numeroCartao;
	
}
