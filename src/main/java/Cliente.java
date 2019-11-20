public class Cliente {
    public static void main(String[] args){
        String ipServidor = IpCorreto.buscaServidor();

        ConexaoCliente conexao = new ConexaoCliente();
        conexao.criarConexaoGRPC(ipServidor);
    }
}
