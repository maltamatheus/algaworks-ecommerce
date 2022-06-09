package com.algaworks.ecommerce.enums;

public enum EnumDirectories {
	
	COMMONS_XML {
		public String getDiretorio() {
			return "src/test/resources/commons/xml";
		}
	};
	public abstract String getDiretorio();
}
