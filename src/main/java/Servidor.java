import io.grpc.Server;
import io.grpc.ServerBuilder;

public class Servidor {

    public static void main(String[] args) {
        ConexaoServidor conexao = new ConexaoServidor();

        try {
            Server servidor = ServerBuilder.forPort(conexao.getPorta()).addService(conexao).build();
            servidor.start();
            System.out.println("CONFIGURAÇÃO: Servidor iniciado no IP "+conexao.getIp()+" e na Porta "+conexao.getPorta()+".");

            conexao.setFuncoesAtomix( new FuncoesAtomix(conexao, conexao.getIdCluster(), conexao.getIpsCluster(), conexao.getPortasCluster()) );

            conexao.criarFingerTable();

            conexao.solicitarEntradaNaRede();

            servidor.awaitTermination();
        } catch (Exception e) {
            System.err.println("CONFIGURAÇÃO: Erro ao tentar iniciar o Servidor na porta "+conexao.getPorta()+".");
            System.err.println(e);
        }
    }

}
