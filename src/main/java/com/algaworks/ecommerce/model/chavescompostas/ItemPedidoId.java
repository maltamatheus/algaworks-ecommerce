package com.algaworks.ecommerce.model.chavescompostas;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import com.algaworks.ecommerce.model.ItemPedido;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor	
@Embeddable
@ToString
public class ItemPedidoId implements Serializable{
	
	@EqualsAndHashCode.Include
	@Column(name = "id_pedido")
	private Integer idPedido;
	
	@EqualsAndHashCode.Include
	@Column(name = "id_produto")
	private Integer idProduto;
	
	/* 
	 * Essa é a estrutura do IdClass
	@EqualsAndHashCode.Include
	private Integer idPedido;
	
	@EqualsAndHashCode.Include
	private Integer idProduto;
	*
	*/
}
