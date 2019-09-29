import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class TrataCliente implements Runnable {

    private Socket cliente;
    private ConexaoServidor servidor;

    public TrataCliente(Socket cliente, ConexaoServidor servidor) {
        this.cliente = cliente;
        this.servidor = servidor;
    }

    public void run() {
        Scanner s = null;
        try {
            s = new Scanner(this.cliente.getInputStream());
            while (s.hasNextLine()) {
                servidor.respondeMensagem(cliente, s.nextLine());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            s.close();
        }
    }
}