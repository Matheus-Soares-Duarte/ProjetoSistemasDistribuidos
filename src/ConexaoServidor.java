import java.io.IOException;
import java.io.PrintStream;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ConexaoServidor {
    private int portaTCP;
    private List<Socket> clientes;

    public static void main(String[] args) throws IOException {
        // inicia o servidor
        new Thread(() -> {
            byte[] outBuf;
            final int portaMulticast = 8888;
            try {
                DatagramSocket socket = new DatagramSocket();
                String ipServidor = InetAddress.getLocalHost().getHostAddress();
                outBuf = (ipServidor).getBytes();
                InetAddress address = InetAddress.getByName("224.2.2.3");
                while (true) {
                    DatagramPacket outPacket = new DatagramPacket(outBuf, outBuf.length, address, portaMulticast);
                    socket.send(outPacket);
                    try { Thread.sleep(500); }catch (InterruptedException ie) {}
                }
            } catch (IOException ioe) { System.out.println(ioe); }
        }).start();

        new ConexaoServidor(12345).executa();
    }

    public ConexaoServidor (int porta) {
        this.portaTCP = porta;
        this.clientes = new ArrayList<Socket>();
    }

    public void executa () throws IOException {
        ServerSocket servidor = new ServerSocket(this.portaTCP);
        System.out.println("Porta "+this.portaTCP+" aberta!");

        while (true) {
            // aceita um cliente
            Socket cliente = servidor.accept();
            System.out.println("Nova conexão com o cliente " + cliente.getInetAddress().getHostAddress() );

            // adiciona socket do cliente à lista
            this.clientes.add(cliente);

            // cria tratador de cliente numa nova thread
            TrataCliente tc = new TrataCliente(cliente, this);
            new Thread(tc).start();
        }
    }

    public void respondeMensagem(Socket cliente, String msg) {
        // envia msg para todo mundo
        System.out.println("Mensagem do cliente "+cliente.getInetAddress()+"="+msg+".");
        try {
            PrintStream ps = new PrintStream(cliente.getOutputStream());
            String com[] = msg.split(":");
            if(com.length > 2){
                ps.println("Comando inválido");
            }
            if(com[0].equals("-criar")){
                ps.println("criando"); //Mesa m = new Mesa(Integer.parseInt(com[1]), )
            }
            else if(com[0].equals("-entrar")){
                ps.println("entrando");//addJogador
            }else{
                ps.println("Comando não encontrado");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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
}