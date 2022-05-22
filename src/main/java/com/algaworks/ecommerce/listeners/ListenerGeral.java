package com.algaworks.ecommerce.listeners;

import javax.persistence.PostLoad;

public class ListenerGeral {
	
	@PostLoad
	public void postLoadingClass(Object obj) {
		System.out.println("Listener Geral - Classe " + obj.getClass().getSimpleName() + " carregada");
	}

}
