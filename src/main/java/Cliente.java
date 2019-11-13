public class Cliente {
    public static void main(String[] args){
        int porta = Integer.parseInt( ManipuladorArquivo.arquivoConfiguracao().getProperty("Porta.TCP") );
        String ipServidor = IpCorreto.buscaServidor();

        ConexaoCliente conexao = new ConexaoCliente(porta);
        conexao.criarConexaoGRPC(ipServidor, porta);
    }
}
