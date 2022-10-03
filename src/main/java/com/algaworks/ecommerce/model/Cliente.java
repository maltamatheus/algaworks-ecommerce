package com.algaworks.ecommerce.model;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ForeignKey;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.MapKeyColumn;
import javax.persistence.OneToMany;
import javax.persistence.PostLoad;
import javax.persistence.PostRemove;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.SecondaryTable;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

import com.algaworks.ecommerce.enums.EnumSexo;
import com.algaworks.ecommerce.listeners.ListenerCliente;
import com.algaworks.ecommerce.listeners.ListenerGeral;
import com.algaworks.ecommerce.model.converters.ConversorBooleanToSimNao;

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
@Table(name = "tab_clientes"
      ,uniqueConstraints = {@UniqueConstraint(columnNames = {"cpf"},name = "unq_cpf"),
                            @UniqueConstraint(columnNames = {"cnpj"},name = "unq_cnpj")}
      ,indexes = @Index(columnList = "nome",name = "idx_nome_cliente"))
@SecondaryTable(name = "tab_clientes_detalhe"
               ,pkJoinColumns = @PrimaryKeyJoinColumn(name="id_cliente")
               ,foreignKey = @ForeignKey(name="fk_clientes_detalhe_cliente"))
public class Cliente extends EntidadeBase{

//	@EqualsAndHashCode.Include
//	@Id
//	@GeneratedValue(strategy = GenerationType.IDENTITY)
//	@ToString.Include
//	private Integer id;
	
	@ToString.Include
	@Column(length = 100)
	private String nome;
	
	@ToString.Include
	private String sobrenome;
	
	@ToString.Include
	@Column(length = 11)
	private String cpf;
	
	@ToString.Include
	@Column(length = 14)
	private String cnpj;
	
	@Transient
	private String nomeCompleto;
	
	@Enumerated(EnumType.STRING)
	@javax.validation.constraints.NotNull
	@ToString.Include
	@Column(nullable = false)
	private EnumSexo sexo;
	
	@OneToMany(mappedBy = "cliente")
	@Column(table = "tab_clientes_detalhe")
	private List<Pedido> pedidos;
	
	@ElementCollection
	@CollectionTable(name = "tab_contatos_cliente", joinColumns = @JoinColumn(name = "id_cliente"))
	@MapKeyColumn(name = "tipo_contato")
	@Column(name = "descricao_contato",table = "tab_clientes_detalhe")
	@ToString.Include
	private Map<String, String> contatos;
	
	@Column(table = "tab_clientes_detalhe",name = "outros")
	@ToString.Include
	@Transient
	private String propriedadeAdicional;
	
	@Column(table = "tab_clientes_detalhe",name = "data_nascto")
	@ToString.Include
	private LocalDate dataNascto;
	
	@NotNull
	@Column(length = 3, nullable = false)
	@Convert(converter = ConversorBooleanToSimNao.class)
	@ToString.Include
	private Boolean ativo = false;
	
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

	@PostLoad
	public void geraNomeCompleto() {
		this.nomeCompleto = this.nome + " " + this.sobrenome;
		
		System.out.println("PostLoad da Classe Cliente - " + this.nomeCompleto);
	}
}
