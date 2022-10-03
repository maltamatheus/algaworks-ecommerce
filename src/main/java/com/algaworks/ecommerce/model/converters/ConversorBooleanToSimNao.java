package com.algaworks.ecommerce.model.converters;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class ConversorBooleanToSimNao implements AttributeConverter<Boolean, String>{

	@Override
	public String convertToDatabaseColumn(Boolean attribute) {
		return Boolean.TRUE.equals(attribute) ? "SIM" : "NAO";
	}

	@Override
	public Boolean convertToEntityAttribute(String dbData) {
		return "SIM".equals(dbData) ? Boolean.TRUE : Boolean.FALSE;
	}

}
