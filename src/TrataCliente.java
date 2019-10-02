import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class TrataCliente implements Runnable {
    private Socket socket;
    private ConexaoServidor servidor;

    public TrataCliente(Socket socket, ConexaoServidor servidor) {
        this.setSocket(socket);
        this.setServidor(servidor);
    }

    public void setSocket(Socket socket) { this.socket = socket; }
    public void setServidor(ConexaoServidor servidor) { this.servidor = servidor; }

    public Socket getSocket() { return socket; }
    public ConexaoServidor getServidor() { return servidor; }

    public void run() {
        try {
            ObjectOutputStream out = new ObjectOutputStream(getSocket().getOutputStream());
            ObjectInputStream in = new ObjectInputStream(getSocket().getInputStream());
            while (true) {
                Mensagem mensagem = (Mensagem) in.readObject();
                this.getServidor().respondeMensagem(getSocket(), in, out, mensagem);
            }
        } catch (IOException e) {
//            System.out.println("Erro na comunicação com o cliente "+getSocket().getInetAddress());
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}