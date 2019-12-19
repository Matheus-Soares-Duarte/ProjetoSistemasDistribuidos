import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;

public class Servidor {
    public static void main(String[] args) {
        ConexaoServidor conexao = new ConexaoServidor();

        try {
            Server servidor = ServerBuilder.forPort(conexao.getPorta()).addService(conexao).build();
            servidor.start();
            System.out.println("Servidor iniciado no IP "+conexao.getIp()+" e na porta "+conexao.getPorta()+"." +
                    " Chave Hash do Servidor = "+conexao.getChaveHash()+" .");
            conexao.solicitarEntradaNaRede();
            servidor.awaitTermination();
        } catch (Exception e) {
            System.err.println("ERRO ao tentar iniciar o Servidor na porta "+conexao.getPorta()+".");
            System.err.println(e);
        }
    }
}
