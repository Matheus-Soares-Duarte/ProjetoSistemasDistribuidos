package main.java;

import java.io.IOException;
import java.util.Properties;

public class Cliente {
    public static void main(String[] args) throws IOException {
        Properties p = new PropertiesSalvos().retornoProp();
        final int PORTA = Integer.parseInt(p.getProperty("PORTACLIENTE"));

        ConexaoCliente conexao = new ConexaoCliente();
        String ipServidor = conexao.buscaServidor();
        conexao.criarSocketTCP(ipServidor, PORTA);
    }
}
