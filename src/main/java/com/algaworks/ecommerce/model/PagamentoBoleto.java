package com.algaworks.ecommerce.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.algaworks.ecommerce.enums.EnumStatusPagamento;
import com.algaworks.ecommerce.enums.EnumStatusPedido;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@DiscriminatorValue(value = "BOLETO")
@Entity
//@Table(name = "tab_pagamento_boleto")
public class PagamentoBoleto extends Pagamento{

//	@EqualsAndHashCode.Include
//	@Id
//	@GeneratedValue(strategy = GenerationType.IDENTITY)
//	private Integer id;
//
//	@Column(name = "id_pedido")
//	private Integer pedidoId;
//	
//	private EnumStatusPagamento status;

	@Column(name = "codigo_boleto",length = 47)
	private String codigoBoleto;
	
}
