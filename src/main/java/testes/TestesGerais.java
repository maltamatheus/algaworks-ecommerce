package testes;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class TestesGerais {
	
	public static void main(String[] args) {
		//Escrevendo arq1
		byte[] dados = "Teste".getBytes();
		
		try {
			new FileOutputStream("c:/meusarquivos//files/saida.txt").write(dados);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
