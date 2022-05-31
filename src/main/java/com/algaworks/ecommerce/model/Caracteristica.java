package com.algaworks.ecommerce.model;

import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class Caracteristica {
	
	private String dominio;

	private String caracteristica;
	
	private String descricao;

}
