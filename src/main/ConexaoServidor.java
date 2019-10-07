package main;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class ConexaoServidor implements Serializable {
    private int portaTCP;
    private static List<Jogador> jogadores;
    private static List<Mesa> mesas;

    public ConexaoServidor (int porta) {
        this.setPortaTCP( porta );
        this.setJogadores( new ArrayList<Jogador>() );
        this.setMesas( new ArrayList<Mesa>() );
    }

    List<Jogador> getJogadores(){ return this.jogadores; }
    List<Mesa> getMesas(){ return this.mesas; }
    int getPortaTCP() { return portaTCP; }

    public void setJogadores(List<Jogador> jogadores) { this.jogadores = jogadores; }
    public void setMesas(List<Mesa> mesas){ this.mesas = mesas; }
    public void setPortaTCP(int portaTCP) { this.portaTCP = portaTCP; }

    void addJogador(Jogador jogador){ this.getJogadores().add(jogador); }

    void addMesas(Mesa mesa){ this.getMesas().add(mesa); }

    String addJogadorMesa(int numero, Jogador jogador){
        int index = buscaMesa(numero);
        if(index>=0){
            if( this.getMesas().get(index).addJogador(jogador) ) {
                System.out.println("ENTRADA NA SALA: O Jogador " + jogador.getNome() + " acaba de entrar na sala " + numero + "! Existe(em) " + this.getMesas().get(index).getJogadores().size() + " Jogador(es) nessa Sala.");
            } else {
                System.out.println("ENTRADA NA SALA: Erro na tentativa do Jogador " + jogador.getNome() + " entrar na sala " + numero + "! Pois existe(em) " + this.getMesas().get(index).getJogadores().size() + " Jogador(es) nessa Sala.");
            }
            return "Sucesso:Inicial:Bem vindo a Sala "+numero+"!:"+numero;
        } else {
            return "Erro:Inicial:A Sala "+numero+" não existe!";
        }
    }

    int buscaJogador(Socket socket){
        for (Jogador jogador : this.getJogadores() ){
            if(jogador.getSocketServidor().equals(socket)){
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
            System.out.println("CRIAÇÃO DE SALA: Sucesso ao tentar criar a sala "+numero+"! Existe(em) "+this.getMesas().size()+" Sala(s) aberta(s) neste Servidor.");
            System.out.println("ENTRADA NA SALA: O Jogador "+jogador.getNome()+" acaba de entrar na sala "+numero+"! Existe(em) "+mesa.getJogadores().size()+" Jogador(es) nessa Sala.");
            return "Sucesso:Inicial:A sala de numero "+numero+" foi criada com sucesso!\nEsperando novos jogadores para iniciar o jogo!:"+numero;
        } else {
            System.out.println("CRIAÇÃO DE SALA: Erro ao tentar criar a sala "+numero+"! Existe(em) "+this.getMesas().size()+" Sala(s) aberta(s) neste Servidor.");
            return "Erro:Inicial:A sala de numero "+numero+" ja foi criada!";
        }
    }

    public void espalharServidor(){
        byte[] outBuf;
        final int portaMulticast = 8888;
        try {
            DatagramSocket socket = new DatagramSocket();
            String ipServidor = "IpDoServidor21:"+IpCorreto.getIpCorreto();
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
        ServerSocket servidor = new ServerSocket(this.getPortaTCP(),30,InetAddress.getByName(IpCorreto.getIpCorreto()));
        System.out.println("Servidor Iniciado no IP "+IpCorreto.getIpCorreto()+" e Porta "+this.getPortaTCP()+".\nEsperando Conexões!");

        while (true) {
            // aceita um cliente
            Socket cliente = servidor.accept();
            System.out.println("Nova conexão com o cliente " + cliente.getInetAddress().getHostAddress() );

            // cria tratador de cliente numa nova thread
            TrataCliente tc = new TrataCliente(cliente, this);
            new Thread(tc).start();
        }
    }

    public synchronized void respondeMensagem(Socket socket, ObjectInputStream in, ObjectOutputStream out, Mensagem mensagemRecebida) {
        Mensagem mensagemResposta;

        String tipo = mensagemRecebida.getTipo();
        if(tipo.equals("String")) {
            String conteudo = (String) mensagemRecebida.getObjeto();
            String conteudoSeparado[] = conteudo.split(":");
            String resposta="";

            int index = buscaJogador( socket );
            if(index>=0) {
                Jogador jogador = this.getJogadores().get(index);
                int indexMesa = buscaMesa(jogador.getMesa());

                if (conteudoSeparado[0].equals("Inicial")) {
                    if (conteudoSeparado[1].equals("criar")) {
                        int numero = Integer.parseInt(conteudoSeparado[2]);
                        resposta = this.criarMesa(numero, jogador);
                    } else if (conteudoSeparado[1].equals("entrar")) {
                        int numero = Integer.parseInt(conteudoSeparado[2]);
                        resposta = this.addJogadorMesa(numero, jogador);
                    }

                    mensagemResposta = new Mensagem("String", resposta);
                    this.enviaMesagem(mensagemResposta, out);
                } else if ( conteudoSeparado[0].equals("NaVez") ) {
                    if ( conteudoSeparado[1].equals("comprar") ) {
                        this.getMesas().get(indexMesa).comprarCarta(jogador, "Carta");
                    } else if ( conteudoSeparado[1].equals("passar") ) {
                        jogador.setJogou(true);
                        this.getMesas().get(indexMesa).acorda();
                    } else if ( conteudoSeparado[1].equals("sair") ) {
                        if (indexMesa >= 0) {
                            Mesa mesa = this.getMesas().get(indexMesa);
                            mesa.retirarJogador(jogador);
                            System.out.println("SAIDA DA SALA: " + jogador.getNome() + " saiu da sala " + mesa.getId() + "! Existe(em) " + mesa.getJogadores().size() + " Jogador(es) nessa Sala.");
                            if (mesa.getJogadores().size() < 1) {
                                this.getMesas().remove(mesa);
                                System.out.println("EXCLUSÃO DE SALA: A Sala " + mesa.getId() + " acaba de ser excluida, pois não existem jogadores nela! Existe(em) " + this.getMesas().size() + " Sala(s) neste Servidor.");
                            }
                            mensagemResposta = new Mensagem("String", "Erro:Inicial:Você saiu da sala " + mesa.getId() + "!");
                            this.enviaMesagem(mensagemResposta, out);
                        }
                    }
                } else if( conteudoSeparado[0].equals("Jogo") ){
                    if ( conteudoSeparado[1].equals("sair") ) {
                        if (indexMesa >= 0) {
                            Mesa mesa = this.getMesas().get(indexMesa);
                            mesa.retirarJogador(jogador);
                            System.out.println("SAIDA DA SALA: " + jogador.getNome() + " saiu da sala " + mesa.getId() + "! Existe(em) " + mesa.getJogadores().size() + " Jogador(es) nessa Sala.");
                            if (mesa.getJogadores().size() == 0) {
                                this.getMesas().remove(mesa);
                                System.out.println("EXCLUSÃO DE SALA: A Sala " + mesa.getId() + " acaba de ser excluida, pois não existem jogadores nela! Existe(em) " + this.getMesas().size() + " Sala(s) neste Servidor.");
                            }
                        }
                        System.out.println("EXCLUSÃO DE JOGADOR: O Jogador de nome " + jogador.getNome() + " e IP " + socket.getInetAddress() + " acaba de sair deste Servidor.");
                        this.getJogadores().remove(index);
                    } else {
                        mensagemResposta = new Mensagem("String", "Erro:Comando de Jogo ainda não tratado!");
                        this.enviaMesagem(mensagemResposta, out);
                    }
                } else {
                    mensagemResposta = new Mensagem("String", "Erro:Comando ainda não tratado!");
                    this.enviaMesagem(mensagemResposta, out);
                }
            }
        } else if(tipo.equals("Jogador")){
            Jogador jogador = (Jogador)mensagemRecebida.getObjeto();
            jogador.setSocketServidor(socket);
            jogador.setOutServidor(out);
            jogador.setInServidor(in);
            this.addJogador( jogador );
        } else {
            mensagemResposta = new Mensagem("String", "Erro:Tipo de Mensagem ainda não tratada!");
            this.enviaMesagem(mensagemResposta, out);
        }

//        try {
//            Thread.sleep(1000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        String logJogadoresServidor = "log\\logJogadoresServidor.txt";
//        String logMesasServidor = "log\\logMesasServidor.txt";
//        ManipuladorArquivo.escritorLogJogadores(logJogadoresServidor, this.getJogadores());
//        List<Jogador> testeJogadores = ManipuladorArquivo.leitorArquivoJogadores(logJogadoresServidor);
//        System.out.println(testeJogadores.size());
//        for(Jogador j: testeJogadores){
//            System.out.println("Jogador "+j.getNome()+" socket>"+j.getSocketServidor()+" in>"+j.getInServidor()+" out>"+j.getOutServidor());
//            j.mostrarCartas();
//        }
//        ManipuladorArquivo.escritorLogMesas(logMesasServidor, this.getMesas());
    }

    void enviaMesagem(Mensagem mensagem, ObjectOutputStream out){
        try {
            out.writeObject(mensagem);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}