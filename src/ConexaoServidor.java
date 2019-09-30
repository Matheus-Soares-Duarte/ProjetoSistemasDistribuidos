import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

public class ConexaoServidor {
    private int portaTCP;
    private List<Socket> clientes;
    private List<Jogador> jogadores;
    private List<Mesa> mesas = new ArrayList<Mesa>();

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

    String addJogadorMesa(int numero, Jogador jogador){
        int index = buscaMesa(numero);
        if(index>=0){
            this.getMesas().get(index).addJogador(jogador);
            System.out.println("ENTRADA NA SALA: O Jogador "+jogador.getNome()+" acaba de entrar na sala "+numero+"!");
            return "Sucesso:Inicial:Bem vindo a Sala "+numero+"!:"+numero;
        } else {
            return "Erro:Inicial:A Sala "+numero+" não existe!";
        }
    }

    int buscaJogador(Socket socket){
        for (Jogador jogador : this.getJogadores() ){
            if(jogador.getSocket().equals(socket)){
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
            Mesa mesa = new Mesa(numero, jogador, this);
            this.addMesas(mesa);
            jogador.setMesa(numero);
            return "Sucesso:Inicial:A sala de numero "+numero+" foi criada com sucesso!\nEsperando novos jogadores para iniciar o jogo!:"+numero;
        } else {
            return "Erro:Inicial:A sala de numero "+numero+" ja foi criada!";
        }
    }

    public void espalharServidor(){
        byte[] outBuf;
        final int portaMulticast = 8888;
        try {
            DatagramSocket socket = new DatagramSocket();
            String ipServidor = "IpDoServidor21:"+InetAddress.getLocalHost().getHostAddress();
            outBuf = (ipServidor).getBytes();
            InetAddress address = InetAddress.getByName("224.2.2.3");
            while (true) {
                DatagramPacket outPacket = new DatagramPacket(outBuf, outBuf.length, address, portaMulticast);
                socket.send(outPacket);
                try { Thread.sleep(2000); }catch (InterruptedException ie) {}
            }
        } catch (IOException ioe) { System.out.println(ioe); }
    }

    public void executa () throws IOException {
        ServerSocket servidor = new ServerSocket(this.portaTCP);
        System.out.println("Servidor Iniciado no IP "+InetAddress.getLocalHost().getHostAddress()+" e Porta "+this.portaTCP+".\nEsperando Conexões!");

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

    public void respondeMensagem(Socket socket, ObjectInputStream in, ObjectOutputStream out, Mensagem mensagemRecebida) {
//        System.out.println("Mensagem do cliente "+cliente.getInetAddress()+"="+mensagem+".");
        Mensagem mensagemResposta;

        String tipo = mensagemRecebida.getTipo();
        if(tipo.equals("String")) {
            String conteudo = (String) mensagemRecebida.getObjeto();
            String com[] = conteudo.split(":");
            String resposta;
            if (com[0].equals("criar")) {
                int numero = Integer.parseInt(com[1]);
                int index = buscaJogador( socket );
                if(index>=0){
                    Jogador jogador = this.getJogadores().get(index);
                    resposta = this.criarMesa(numero, jogador);
                    String textoResposta[] = resposta.split(":");
                    System.out.println("CRIAÇÃO DE SALA: "+textoResposta[0]+" ao tentar criar a sala "+numero+"! Existe(em) "+getMesas().size()+" Sala(s) aberta(s) neste Servidor.");
                } else {
                    resposta = "Erro:Inicial:Desculpe, mas tivemos problemas para encontrar seu jogador, por favor reinicie o jogo!";
                }
                mensagemResposta = new Mensagem("String", resposta);
                this.enviaMesagem(mensagemResposta, out);
            } else if (com[0].equals("entrar")) {
                int numero = Integer.parseInt(com[1]);
                int index = buscaJogador( socket );
                if(index>=0){
                    Jogador jogador = this.getJogadores().get(index);
                    resposta = this.addJogadorMesa(numero, jogador);
                } else {
                    resposta = "Erro:Inicial:Desculpe, mas tivemos problemas para encontrar seu jogador, por favor reinicie o jogo!";
                }
                mensagemResposta = new Mensagem("String", resposta);
                this.enviaMesagem(mensagemResposta, out);
            } else {
                mensagemResposta = new Mensagem("String", "Erro:Inicial:Comando ainda não tratado!");
                this.enviaMesagem(mensagemResposta, out);
            }
        } else if(tipo.equals("Jogador")){
            Jogador jogador = (Jogador)mensagemRecebida.getObjeto();
            jogador.setSocket(socket);
            jogador.setOut(out);
            jogador.setIn(in);
            this.addJogador( jogador );
        }
    }

    void enviaMesagem(Mensagem mensagem, ObjectOutputStream out){
        try {
            out.writeObject(mensagem);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}