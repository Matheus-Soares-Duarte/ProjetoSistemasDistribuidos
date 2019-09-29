import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Recebedor implements Runnable {
    ObjectInputStream in;


    public Recebedor(ObjectInputStream in) {
        this.in = in;
    }

    public void run() {
        // recebe msgs do servidor e imprime na tela
        try {
            while (true) {
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