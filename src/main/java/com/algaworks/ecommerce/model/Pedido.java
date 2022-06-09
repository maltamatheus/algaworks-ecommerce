package com.algaworks.ecommerce.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.algaworks.ecommerce.enums.EnumStatusPedido;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "tab_pedidos")
public class Pedido implements Serializable{

	@EqualsAndHashCode.Include
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@ToString.Include
	private Integer id;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "id_cliente")
	@ToString.Include
	private Cliente cliente;
	
	@Column(name = "id_categoria_pai")
	@ToString.Include
	private Integer categoriaPaiId;
	
	@Column(name ="data_realizacao_pedido")
	@ToString.Include
	private LocalDateTime dataPedido;
	
	@Column(name = "data_conclusao_pedido")
	@ToString.Include
	private LocalDateTime dataConclusao;
	
	@OneToMany(mappedBy = "idPedido")
	private List<ItemPedido> itensPedido;
	
	@ToString.Include
	private EnumStatusPedido status;
	
	@Embedded
	@ToString.Include
	private EnderecoEntregaPedido enderecoEntrega;
	
	@OneToOne(mappedBy = "pedido")
	private Pagamento pagamento;
	
	@OneToMany(mappedBy = "pedido")
	private List<NotaFiscal> notasPedido;
}
