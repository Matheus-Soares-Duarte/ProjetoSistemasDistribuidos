import io.grpc.Server;
import io.grpc.ServerBuilder;
import java.io.IOException;

public class Servidor {
    public static void main(String[] args) {
        final int portaMulticast = Integer.parseInt( ManipuladorArquivo.arquivoConfiguracao().getProperty("Porta.Multicast") );

        ConexaoServidor conexao = new ConexaoServidor();
        new Thread(() -> {
            IpCorreto.espalharServidor(portaMulticast);
        }).start();

        try {
            Server servidor = ServerBuilder.forPort(conexao.getPorta()).addService(conexao).build();
            servidor.start();
            System.out.println("Servidor iniciado na porta "+conexao.getPorta()+".");
            servidor.awaitTermination();
        } catch (IOException e) {
            System.err.println("ERRO ao tentar iniciar o Servidor na porta "+conexao.getPorta()+".");
            System.err.println(e);
        } catch (InterruptedException e) {
            System.err.println("ERRO ao tentar iniciar o Servidor na porta "+conexao.getPorta()+".");
            System.err.println(e);
        }

    }
}
