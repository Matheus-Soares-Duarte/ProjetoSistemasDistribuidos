import java.io.IOException;
import java.io.PrintStream;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

public class ConexaoServidor {
    private int portaTCP;
    private List<Socket> clientes;
    private List<Mesa> mesas = new ArrayList<Mesa>();

    public ConexaoServidor (int porta) {
        this.portaTCP = porta;
        this.clientes = new ArrayList<Socket>();
    }

    void setMesas(List<Mesa> mesas){ this.mesas = mesas; }
    List<Mesa> getMesas(){ return this.mesas; }

    void addMesas(Mesa mesa){ this.mesas.add(mesa); }

    boolean addJogador(int numero, Jogador jogador){
        int index = buscaMesa(numero);
        if(index>=0){
            this.getMesas().get(index).addJogador(jogador);
            return true;
        } else {
            System.out.println("A sala de numero "+numero+" não existe!");
            return false;
        }
    }

    int buscaMesa(int numero){
        for (Mesa mesa : this.getMesas()){
            if(mesa.getId() == numero){
                return this.getMesas().indexOf(mesa);
            }
        }
        return -1;
    }

    boolean criarMesa(int numero, Jogador jogador){
        int index = buscaMesa(numero);
        if(index==-1){
            Mesa mesa = new Mesa(numero, jogador);
            this.addMesas(mesa);
            jogador.setMesa(mesa);
            System.out.println("A sala de numero "+numero+" foi criada com sucesso!");
            return true;
        } else {
            System.out.println("A sala de numero "+numero+" ja foi criada!");
            return false;
        }
    }

    public void espalharServidor(){
        byte[] outBuf;
        final int portaMulticast = 8888;
        try {
            DatagramSocket socket = new DatagramSocket();
//            String ipServidor = "IpDoServidor21:"+InetAddress.getLocalHost().getHostAddress();
            String ipServidor = InetAddress.getLocalHost().getHostAddress();
            outBuf = (ipServidor).getBytes();
            InetAddress address = InetAddress.getByName("224.2.2.3");
            while (true) {
                DatagramPacket outPacket = new DatagramPacket(outBuf, outBuf.length, address, portaMulticast);
                socket.send(outPacket);
                try { Thread.sleep(500); }catch (InterruptedException ie) {}
            }
        } catch (IOException ioe) { System.out.println(ioe); }
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

    public void respondeMensagem(Socket cliente, Mensagem mensagem) {
//        System.out.println("Mensagem do cliente "+cliente.getInetAddress()+"="+mensagem+".");
        try {
            PrintStream ps = new PrintStream(cliente.getOutputStream());
            String tipo = mensagem.getTipo();
            if(tipo.equals("String")) {
                String conteudo = (String) mensagem.getObjeto();
                String com[] = conteudo.split(":");
                if (com.length > 2) {
                    ps.println("Comando inválido");
                }
                if (com[0].equals("criar")) {
                    ps.println("criando"); //Mesa m = new Mesa(Integer.parseInt(com[1]), )
                } else if (com[0].equals("entrar")) {
                    ps.println("entrando");//addJogador
                } else {
                    ps.println("Comando não encontrado");
                }
            } else if(tipo.equals("Jogador")){

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}