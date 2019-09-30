import java.io.*;
import java.net.*;

public class ConexaoCliente {
    byte[] inBuf = new byte[256];
    Socket socket = null;
    ObjectOutputStream out;
    ObjectInputStream in;
    Jogador jogador;

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
            Recebedor recebedor = new Recebedor(this);
            new Thread(recebedor).start();

            jogador = new Jogador(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void analisaMesagem(Mensagem mensagem){
        String tipo = mensagem.getTipo();
        switch (tipo){
            case "String" :
                String conteudo = (String) mensagem.getObjeto();
                String com[] = conteudo.split(":");
                if( com[0].equals("Erro") ){
                    if(com[1].equals("Inicial")){
                        System.out.println(com[2]);
                        jogador.getMenu().escolhaInicial(this);
                    }
                } else if( com[0].equals("Sucesso") ){
                    System.out.println("ALA DE SUCESSO");
                    System.out.println(com[1]);
                } else {
                    System.out.println(com[1]);
                }
                break;
            case "Carta":
                Carta carta = (Carta) mensagem.getObjeto();
                jogador.addCarta(carta);
                jogador.mostrarCartas();
                if (jogador.getPontos()>21){
                    System.out.println(jogador.getNome()+" ESTOUROU COM "+jogador.getPontos()+" PONTOS.");
                    Mensagem mensagemPassar = new Mensagem("String", "passar");
                    this.enviaMesagem(mensagemPassar);
                } else{
                    jogador.getMenu().escolha(jogador,this);
                }
                break;
            default:
                System.out.println("Tipo de mensagem não encontrada!");
                break;

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