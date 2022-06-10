package com.algaworks.ecommerce.model;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.algaworks.ecommerce.enums.EnumStatusPagamento;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@DiscriminatorColumn(name = "tipo_pagamento",discriminatorType = DiscriminatorType.STRING)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Table(name = "tab_pagamentos")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public abstract class Pagamento extends EntidadeBase{
	
//	@EqualsAndHashCode.Include
//	@Id
//	@GeneratedValue(strategy = GenerationType.IDENTITY)
//	private Integer id;

	@MapsId
	@OneToOne(optional = false)
	@JoinColumn(name = "id_pedido")
	private Pedido pedido;
	
	private EnumStatusPagamento status;

}
