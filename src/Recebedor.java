import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Recebedor implements Runnable {
    ConexaoCliente cliente;

    public Recebedor(ConexaoCliente cliente) {
        this.cliente = cliente;
    }

    public void run() {
        // recebe msgs do servidor e imprime na tela
        try {
            while (true) {
                Mensagem mensagem = (Mensagem) cliente.in.readObject();
                cliente.analisaMesagem(mensagem);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}