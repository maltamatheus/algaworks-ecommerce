package com.algaworks.ecommerce.listeners;

import javax.persistence.PostLoad;

public class ListenerGeral {
	
	@PostLoad
	public void postLoadingClass(Object obj) {
		System.out.println("Classe " + obj.getClass().getSimpleName() + " carregada");
	}

}
