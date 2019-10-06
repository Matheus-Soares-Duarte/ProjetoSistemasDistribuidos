import java.io.*;
import java.net.*;

public class ConexaoCliente {
    private byte[] inBuf = new byte[256];
    private Socket socket = null;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private Jogador jogador;

    public ObjectInputStream getIn() { return in; }
    public byte[] getInBuf() { return inBuf; }
    public Jogador getJogador() { return jogador; }
    public ObjectOutputStream getOut() { return out; }
    public Socket getSocket() { return socket; }

    public void setIn(ObjectInputStream in) { this.in = in; }
    public void setInBuf(byte[] inBuf) { this.inBuf = inBuf; }
    public void setJogador(Jogador jogador) { this.jogador = jogador; }
    public void setOut(ObjectOutputStream out) { this.out = out; }
    public void setSocket(Socket socket) { this.socket = socket; }

    void analisaMesagem(Mensagem mensagem){
        String tipo = mensagem.getTipo();
        Carta carta;
        switch (tipo){
            case "String" :
                String conteudo = (String) mensagem.getObjeto();
                String conteudoSeparado[] = conteudo.split(":");
                if( conteudoSeparado[0].equals("Erro") ){
                    if(conteudoSeparado[1].equals("Inicial")){
                        System.out.println(conteudoSeparado[2]);
                        this.getJogador().getMenu().escolhaInicial(this);
                    } else {
                        System.out.println(conteudoSeparado[1]);
                    }
                } else if( conteudoSeparado[0].equals("Sucesso") ){
                    if(conteudoSeparado[1].equals("Inicial")){
                        System.out.println(conteudoSeparado[2]);
                        this.getJogador().setMesa(Integer.parseInt(conteudoSeparado[3]));
                    } else {
                        System.out.println(conteudoSeparado[1]);
                    }
                } else if( conteudoSeparado[0].equals("SuaVez") ){
                    this.getJogador().getMenu().escolhaNaVez(this.getJogador(), this);
                } else if( conteudoSeparado[0].equals("Vitoria") ){
                    this.getJogador().addVitoria();
                } else if( conteudoSeparado[0].equals("ReiniciarRodada") ){
                    this.getJogador().devolverCartas();
                }else {
                    System.out.println(conteudo);
                }
                break;
            case "Carta":
                carta = (Carta) mensagem.getObjeto();
                this.getJogador().comprarCarta(carta);
                if (this.getJogador().getPontos()>21){
                    this.getJogador().setJogou(true);
                    this.getJogador().mostrarCartas();
                    System.out.println(this.getJogador().getNome()+" ESTOUROU COM "+this.getJogador().getPontos()+" PONTOS.");
                    Mensagem mensagemPassar = new Mensagem("String", "NaVez:passar");
                    this.enviaMesagem(mensagemPassar);
                } else{
                    this.getJogador().getMenu().escolhaNaVez(this.getJogador(),this);
                }
                break;
            case "CartaInicial":
                carta = (Carta) mensagem.getObjeto();
                this.getJogador().comprarCarta(carta);
                break;
            default:
                System.out.println("Tipo de mensagem não encontrada!");
                break;
        }
    }

    String buscaServidor(){
        String ipServidor="";
        try {
            MulticastSocket socket = new MulticastSocket(8888);
            InetAddress address = InetAddress.getByName("224.2.2.3");
            socket.joinGroup(address);
            System.out.println("Meu IP = "+InetAddress.getLocalHost().getHostAddress() );
            System.out.println("Tentando se Conectar ao Servidor." );
            DatagramPacket inPacket = new DatagramPacket(this.getInBuf(), this.getInBuf().length);
            while(true){
                socket.receive(inPacket);
                String mensagem = new String(this.getInBuf(), 0, inPacket.getLength());
                String textoResposta[] = mensagem.split(":");
                if(textoResposta[0].equals("IpDoServidor21")){
                    ipServidor = textoResposta[1];
                    socket.leaveGroup(address);
                    break;
                }
            }
        }catch (IOException ioe) {
            System.out.println(ioe);
        }
        return ipServidor;
    }

    void criarSocketTCP(String ipServidor, int porta){
        try {
            Socket socket = new Socket(ipServidor, porta);
            this.setSocket(socket);
            this.setOut( new ObjectOutputStream(socket.getOutputStream()) );
            this.setIn( new ObjectInputStream(socket.getInputStream()) );

            System.out.println("O cliente "+ InetAddress.getLocalHost().getHostAddress()+" se conectou ao servidor "+ipServidor+"!");
            Recebedor recebedor = new Recebedor(this);
            new Thread(recebedor).start();

            this.setJogador( new Jogador(this) );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void enviaMesagem(Mensagem mensagem){
        try {
            this.getOut().writeObject(mensagem);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}