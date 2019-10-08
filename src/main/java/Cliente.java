package main.java;

public class Cliente {
    public static void main(String[] args) {
        final int PORTA = 12345;

        ConexaoCliente conexao = new ConexaoCliente();
        String ipServidor = conexao.buscaServidor();
        conexao.criarSocketTCP(ipServidor, PORTA);
    }
}
