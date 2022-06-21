package com.algaworks.ecommerce.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class ProdutoDTO {
	
	private Integer id;
	private String nome;
	private String descricao;

}
