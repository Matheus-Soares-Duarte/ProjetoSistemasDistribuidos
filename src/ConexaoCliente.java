import java.io.*;
import java.net.*;

public class ConexaoCliente {
    byte[] inBuf = new byte[256];
    Socket socket = null;
    ObjectOutputStream out;
    ObjectInputStream in;

    String buscaServidor(){
        String ipServidor="";
        try {
            MulticastSocket socket = new MulticastSocket(8888);
            InetAddress address = InetAddress.getByName("224.2.2.3");
            socket.joinGroup(address);
            System.out.println("Meu IP = "+InetAddress.getLocalHost().getHostAddress() );
            System.out.println("Tentando se Conectar ao Servidor." );
            DatagramPacket inPacket = new DatagramPacket(inBuf, inBuf.length);
            socket.receive(inPacket);
            ipServidor = new String(inBuf, 0, inPacket.getLength());
            socket.leaveGroup(address);
        }catch (IOException ioe) {
            System.out.println(ioe);
        }
        return ipServidor;
    }

    void criarSocketTCP(String ipServidor, int porta){
        try {
            socket = new Socket(ipServidor, porta);
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());

            System.out.println("O cliente "+ InetAddress.getLocalHost().getHostAddress()+" se conectou ao servidor "+ipServidor+"!");
            Recebedor recebedor = new Recebedor(in);
            new Thread(recebedor).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void enviaMesagem(Mensagem mensagem){
        try {
            out.writeObject(mensagem);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}