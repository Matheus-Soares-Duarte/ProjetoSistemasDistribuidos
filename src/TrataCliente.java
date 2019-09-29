import java.io.IOException;
import java.io.ObjectInputStream;
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
        try {
            while (true) {
                ObjectInputStream in = new ObjectInputStream(cliente.getInputStream());
                Mensagem mensagem = (Mensagem) in.readObject();
                servidor.respondeMensagem(cliente, mensagem);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}