package com.algaworks.ecommerce.model;

import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

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
	private EnumTipoLogradouro tipoLogradouro;
	
	private String logradouro;
	
	private String numero;
	
	private String bairro;
	
	@Enumerated(EnumType.STRING)
	private EnumTipoComplemento tipoComplemento;
	
	private String complemento;
	
	@Enumerated(EnumType.STRING)
	private EnumTipoImovelComplemento tipoImovelComplemento;
	
	private String imovelComplemento;

	private String cep;
}
