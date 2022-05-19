package testes;

import java.time.LocalDateTime;

import org.junit.Test;

import com.algaworks.ecommerce.enums.EnumStatusPedido;
import com.algaworks.ecommerce.enums.EnumTipoComplemento;
import com.algaworks.ecommerce.enums.EnumTipoImovelComplemento;
import com.algaworks.ecommerce.enums.EnumTipoLogradouro;
import com.algaworks.ecommerce.model.EnderecoEntregaPedido;
import com.algaworks.ecommerce.model.Pedido;

public class TesteMapeamentoObjetoEmbutido extends EntityManagerTests{
	
	@Test
	public void analisarMapeamenoObjetoEmbutido() {
		EnderecoEntregaPedido endEntrega = new EnderecoEntregaPedido();
		
		endEntrega.setTipoLogradouro(EnumTipoLogradouro.RUA);
		endEntrega.setLogradouro("Comendador Antônio Nagib Ibrahim");
		endEntrega.setNumero("60");
		endEntrega.setBairro("Núcleo Habitacional Brigadeiro Faria Lima");
		endEntrega.setTipoComplemento(EnumTipoComplemento.BLOCO);
		endEntrega.setComplemento("F");
		endEntrega.setTipoImovelComplemento(EnumTipoImovelComplemento.APARTAMENTO);
		endEntrega.setImovelComplemento("24");
		endEntrega.setCep("13345350");
		
		Pedido pedido = new Pedido();
		
		pedido.setId(1);
		pedido.setDataConclusao(LocalDateTime.now());
		pedido.setEnderecoEntrega(endEntrega);
		pedido.setStatus(EnumStatusPedido.AGUARDANDO);
		
		manager.getTransaction().begin();
		manager.persist(pedido);
		manager.getTransaction().commit();
		
		manager.clear();
		
		Pedido pedidoVerify = manager.find(Pedido.class, 1);
		
		System.out.println(pedidoVerify.toString());
		
	}

}
