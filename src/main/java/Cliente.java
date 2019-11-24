public class Cliente {
    public static void main(String[] args){
        ConexaoCliente conexao = new ConexaoCliente();
        conexao.criarConexaoGRPC(conexao.getIpServidor());
    }
}
