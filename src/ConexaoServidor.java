import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

public class ConexaoServidor {
    private int portaTCP;
    private static List<Socket> clientes;
    private static List<Jogador> jogadores;
    private static List<Mesa> mesas = new ArrayList<Mesa>();

    public ConexaoServidor (int porta) {
        this.portaTCP = porta;
        this.clientes = new ArrayList<Socket>();
        this.jogadores = new ArrayList<Jogador>();
    }

    void setMesas(List<Mesa> mesas){ this.mesas = mesas; }
    List<Socket> getClientes(){ return this.clientes; }
    List<Jogador> getJogadores(){ return this.jogadores; }
    List<Mesa> getMesas(){ return this.mesas; }

    void addCliente(Socket socket){ this.clientes.add(socket); }
    void addJogador(Jogador jogador){ this.jogadores.add(jogador); }
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

    int buscaJogador(String ip){
        for (Jogador jogador : this.getJogadores() ){
            if(jogador.getIp().equals(ip)){
                return this.getJogadores().indexOf(jogador);
            }
        }
        return -1;
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
        if(index==-1){
            Mesa mesa = new Mesa(numero, jogador);
            this.addMesas(mesa);
            jogador.setMesa(numero);
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
        System.out.println("Servidor Iniciado no IP "+InetAddress.getLocalHost().getHostAddress()+" e Porta "+this.portaTCP+". Esperando Conexões!");

        while (true) {
            // aceita um cliente
            Socket cliente = servidor.accept();
            System.out.println("Nova conexão com o cliente " + cliente.getInetAddress().getHostAddress() );

            // adiciona socket do cliente à lista
            this.addCliente(cliente);

            // cria tratador de cliente numa nova thread
            TrataCliente tc = new TrataCliente(cliente, this);
            new Thread(tc).start();
        }
    }

    public void respondeMensagem(Socket socket, ObjectOutputStream saida, Mensagem mensagemRecebida) {
//        System.out.println("Mensagem do cliente "+cliente.getInetAddress()+"="+mensagem+".");
        try {
            Mensagem mensagemResposta;

            String tipo = mensagemRecebida.getTipo();
            if(tipo.equals("String")) {
                String conteudo = (String) mensagemRecebida.getObjeto();
                String com[] = conteudo.split(":");
                if (com.length > 2) {
                    mensagemResposta = new Mensagem("String", "Comando inválido");
                    saida.writeObject(mensagemResposta);
                }
                else if (com[0].equals("criar")) {
                    int numero = Integer.parseInt(com[1]);
                    System.out.println("Tentando criar mesa "+numero);
                    int index = buscaJogador( socket.getInetAddress().toString().replace("/", "") );
                    if(index>=0){
                        Jogador jogador = this.getJogadores().get(index);
                        mensagemResposta = new Mensagem("String", this.criarMesa(numero, jogador));
                    } else {
                        mensagemResposta = new Mensagem("String", "Desculpe, mas tivemos problemas para encontrar seu jogador, por favor reinicie o jogo!");
                    }
                    saida.writeObject(mensagemResposta);
                } else if (com[0].equals("entrar")) {
                    mensagemResposta = new Mensagem("String", "Entrando na Sala!");
                    saida.writeObject(mensagemResposta);
                } else {
                    mensagemResposta = new Mensagem("String", "Entrando na Sala!");
                    saida.writeObject(mensagemResposta);
                }
            } else if(tipo.equals("Jogador")){
                Jogador jogador = (Jogador)mensagemRecebida.getObjeto();
                this.addJogador( jogador );
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}