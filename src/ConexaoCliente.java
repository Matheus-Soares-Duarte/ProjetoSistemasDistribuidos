import java.io.*;
import java.net.*;
import java.util.Scanner;

public class ConexaoCliente {
    public static void main(String[] args) {
        final int port = 12345;
        byte[] inBuf = new byte[256];
        try {
            MulticastSocket socket = new MulticastSocket(8888);
            InetAddress address = InetAddress.getByName("224.2.2.3");
            socket.joinGroup(address);
            System.out.println("Meu IP = "+InetAddress.getLocalHost().getHostAddress() );
            System.out.println("Tentando se Conectar ao Servidor." );
            DatagramPacket inPacket = new DatagramPacket(inBuf, inBuf.length);
            socket.receive(inPacket);
            String ipServidor = new String(inBuf, 0, inPacket.getLength());
            socket.leaveGroup(address);

            Socket cliente = new Socket(ipServidor, port);
            System.out.println("O cliente "+InetAddress.getLocalHost().getHostAddress()+" se conectou ao servidor "+ipServidor+" na porta "+port+"!");

            Recebedor r = new Recebedor(cliente.getInputStream());
            new Thread(r).start();

            // lê msgs do teclado e manda pro servidor
            Scanner teclado = new Scanner(System.in);
            PrintStream saida = new PrintStream(cliente.getOutputStream());
            while (true) {
                System.out.print("Digite o comando: ");
                teclado.hasNextLine();
                saida.println(teclado.nextLine());
                //r.wait(500);
            }
        }catch (IOException ioe) {
            System.out.println(ioe);
        }
//        catch (InterruptedException e) {
//            e.printStackTrace();
//        }
    }

    public static class Recebedor implements Runnable {

        private InputStream servidor;

        public Recebedor(InputStream servidor) {
            this.servidor = servidor;
        }

        public void run() {
            // recebe msgs do servidor e imprime na tela
            Scanner s = new Scanner(this.servidor);
            while (s.hasNextLine()) {
                System.out.print("Resposta do servidor: ");
                System.out.println(s.nextLine());
            }
        }
    }
}