package com.algaworks.ecommerce.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.PostRemove;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

import com.algaworks.ecommerce.enums.EnumSexo;
import com.algaworks.ecommerce.listeners.ListenerCliente;
import com.algaworks.ecommerce.listeners.ListenerGeral;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@EntityListeners({ListenerCliente.class,ListenerGeral.class})
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "tab_clientes")
public class Cliente {

	@EqualsAndHashCode.Include
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@ToString.Include
	private Integer id;
	
	@ToString.Include
	private String nome;
	
	@ToString.Include
	private String sobrenome;
	
	@Enumerated(EnumType.STRING)
	@ToString.Include
	private EnumSexo sexo;
	
	@OneToMany(mappedBy = "cliente")
	private List<Pedido> pedidos;
	
	private String propriedadeAdicional;
	
	@PrePersist
	public void aoPersistir() {
		System.out.println("Cliente salvo com sucesso");
	}
	
	@PreUpdate
	public void aoAtualizar() {
		System.out.println("Informação atualizada");	
	}
	
	@PreRemove
	public void aoRemover() {
		System.out.println("Cliente " + this.nome + " será removido");
	}

	@PostRemove
	public void aposRemover() {
		System.out.println("Informação excluída");
	}
}
