import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class TrataCliente implements Runnable {
    private Socket cliente;
    private ConexaoServidor servidor;

    public TrataCliente(Socket cliente, ConexaoServidor servidor) {
        this.cliente = cliente;
        this.servidor = servidor;
    }

    public void run() {
        try {
            ObjectOutputStream out = new ObjectOutputStream(cliente.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(cliente.getInputStream());
            while (true) {
                Mensagem mensagem = (Mensagem) in.readObject();
                servidor.respondeMensagem(cliente, in, out, mensagem);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}