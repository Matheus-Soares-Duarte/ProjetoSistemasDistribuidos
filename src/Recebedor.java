import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

public class Recebedor implements Runnable {
    private Socket socket;

    public Recebedor(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        // recebe msgs do servidor e imprime na tela
        try {
            while (true) {
                ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
                Mensagem mensagem = (Mensagem) in.readObject();
                String tipo = mensagem.getTipo();
                if(tipo.equals("String") || tipo.equals("Jogador")){
                    System.out.println((String)mensagem.getObjeto());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}