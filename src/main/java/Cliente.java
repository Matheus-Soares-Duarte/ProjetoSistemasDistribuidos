package main.java;

public class Cliente {
    public static void main(String[] args){
        final int PORTA = Integer.parseInt( ManipuladorArquivo.arquivoConfiguracao().getProperty("Porta.TCP") );

        ConexaoCliente conexao = new ConexaoCliente();
        String ipServidor = conexao.buscaServidor();
        conexao.criarSocketTCP(ipServidor, PORTA);
    }
}
