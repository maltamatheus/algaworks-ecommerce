package com.algaworks.ecommerce.model;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;

import com.algaworks.ecommerce.model.chavescompostas.ItemPedidoId;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
//@IdClass(ItemPedidoId.class)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "tab_itens_pedido")
public class ItemPedido {

	@EmbeddedId
    private ItemPedidoId id;
	
	@MapsId("idPedido")
	@ManyToOne(optional = false)
	@JoinColumn(name = "id_pedido")
	private Pedido idPedido;
	
	@MapsId("idProduto")
	@ManyToOne(optional = false)
	@JoinColumn(name = "id_produto")
	private Produto idProduto;

	/*
	 * Estrutura para o IdClass
	@EqualsAndHashCode.Include
	@Id
	@Column(name = "id_pedido",insertable = false, updatable = false)
	private Integer idPedido;
	
	@EqualsAndHashCode.Include
	@Id
	@Column(name = "id_produto",insertable = false, updatable = false)
	private Integer idProduto;
	*/

	@Column(nullable = false)
	private Integer quantidade;
	
}
