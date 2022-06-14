package com.algaworks.ecommerce.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Transient;

import com.algaworks.ecommerce.enums.EnumTipoComplemento;
import com.algaworks.ecommerce.enums.EnumTipoImovelComplemento;
import com.algaworks.ecommerce.enums.EnumTipoLogradouro;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Embeddable
@Setter
@Getter
@ToString
public class EnderecoEntregaPedido {

	@Enumerated(EnumType.STRING)
	@Column(name = "tipo_logradouro",length = 50)
	private EnumTipoLogradouro tipoLogradouro;
	
	@Column(length = 100)
	private String logradouro;
	
	@Column(length = 10)
	private String numero;
	
	@Column(length = 50)
	private String bairro;
	
	@Enumerated(EnumType.STRING)
	private EnumTipoComplemento tipoComplemento; // bloco, casa
	
	@Transient
	private String complemento;
	
	@Enumerated(EnumType.STRING)
	private EnumTipoImovelComplemento tipoImovelComplemento; // apto, casa

	@Column(length=50)
	private String imovelComplemento;

	@Column(length=9)
	private String cep;
	
	@Column(length = 100)
	private String cidade;
	
	@Column(length=2)
	private String uf;
}
