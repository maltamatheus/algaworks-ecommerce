package com.algaworks.ecommerce.model;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
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
	@JoinColumn(name = "id_pedido",nullable = false,foreignKey = @ForeignKey(name="fk_notafiscal_pedido"))
	private Pedido pedido;
	
	@Lob
	@ToString.Exclude
	@Column(name = "xml_nota",nullable = false)
	private byte[] xml;
	
	@Column(name = "data_emissao_nf",columnDefinition = "timestamp(14) not null")
	private Date dataEmissao;
	
	@Column(name = "valor_total",nullable = false,scale=2,precision=19)
	private BigDecimal valorTotal;
	
}
