import io.grpc.Server;
import io.grpc.ServerBuilder;
import java.io.IOException;

public class Servidor {
    public static void main(String[] args) {
        final int portaTCP = Integer.parseInt( ManipuladorArquivo.arquivoConfiguracao().getProperty("Porta.TCP") );
        final int portaMulticast = Integer.parseInt( ManipuladorArquivo.arquivoConfiguracao().getProperty("Porta.Multicast") );

        ConexaoServidor conexao = new ConexaoServidor(portaTCP);
        new Thread(() -> {
            IpCorreto.espalharServidor(portaMulticast);
        }).start();

        try {
            Server servidor = ServerBuilder.forPort(portaTCP).addService(conexao).build();
            servidor.start();
            System.out.println("Servidor iniciado na porta "+portaTCP+".");
            servidor.awaitTermination();
        } catch (IOException e) {
            System.err.println("ERRO ao tentar iniciar o Servidor na porta "+portaTCP+".");
            System.err.println(e);
        } catch (InterruptedException e) {
            System.err.println("ERRO ao tentar iniciar o Servidor na porta "+portaTCP+".");
            System.err.println(e);
        }

    }
}
