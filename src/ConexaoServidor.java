import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ConexaoServidor {
    private int portaTCP;
    private List<Mesa> mesas = new ArrayList<Mesa>();
    private Map<Socket,Jogador> clientes;

    public ConexaoServidor (int porta) {
        this.portaTCP = porta;
    }

    void setMesas(List<Mesa> mesas){ this.mesas = mesas; }
    List<Mesa> getMesas(){ return this.mesas; }

    Map<Socket,Jogador> getClientes(){ return this.clientes; }

    void addCliente(Socket socket, Jogador jogador){ this.clientes.put(socket, jogador); }
    void addMesas(Mesa mesa){ this.mesas.add(mesa); }

    boolean addJogadorMesa(int numero, Jogador jogador){
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

    String criarMesa(int numero, Jogador jogador){
        int index = buscaMesa(numero);
        String saida;
        if(index==-1){
            Mesa mesa = new Mesa(numero, jogador);
            this.addMesas(mesa);
            jogador.setMesa(mesa);
            return "A sala de numero "+numero+" foi criada com sucesso!";
        } else {
            return "A sala de numero "+numero+" ja foi criada!";
        }
    }

    public void espalharServidor(){
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
    }

    public void executa () throws IOException {
        ServerSocket servidor = new ServerSocket(this.portaTCP);
        System.out.println("Porta "+this.portaTCP+" aberta!");

        while (true) {
            // aceita um cliente
            Socket cliente = servidor.accept();
            System.out.println("Nova conexão com o cliente " + cliente.getInetAddress().getHostAddress() );

            // cria tratador de cliente numa nova thread
            TrataCliente tc = new TrataCliente(cliente, this);
            new Thread(tc).start();
        }
    }

    public void respondeMensagem(Socket socket, Mensagem mensagemRecebida) {
//        System.out.println("Mensagem do cliente "+cliente.getInetAddress()+"="+mensagem+".");
        try {
            ObjectOutputStream saida = new ObjectOutputStream(socket.getOutputStream());
            Mensagem mensagemResposta = null;

            String tipo = mensagemRecebida.getTipo();
            if(tipo.equals("String")) {
                String conteudo = (String) mensagemRecebida.getObjeto();
                String com[] = conteudo.split(":");
                if (com.length > 2) {
                    mensagemResposta = new Mensagem("String", "Comando inválido");
                    saida.writeObject(mensagemResposta);
                }
                if (com[0].equals("criar")) {
                    int numero = Integer.parseInt(com[1]);
                    Jogador jogador = this.getClientes().get(socket);
                    mensagemResposta = new Mensagem("String", this.criarMesa(numero, jogador));
                    saida.writeObject(mensagemResposta);
                } else if (com[0].equals("entrar")) {
                    mensagemResposta = new Mensagem("String", "Entrando na Sala!");
                    saida.writeObject(mensagemResposta);
                } else {
                    mensagemResposta = new Mensagem("String", "Entrando na Sala!");
                    saida.writeObject(mensagemResposta);
                }
            } else if(tipo.equals("Jogador")){
                this.addCliente(socket, (Jogador)mensagemRecebida.getObjeto() );
//                saida.writeObject(mensagemResposta);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}