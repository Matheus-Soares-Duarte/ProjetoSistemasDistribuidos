package main;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class TrataCliente implements Runnable {
    private int reconexao;
    private Socket socket;
    private ConexaoServidor servidor;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    public TrataCliente(Socket socket, ConexaoServidor servidor) {
        this.setReconexao(0);
        this.setSocket(socket);
        this.setServidor(servidor);
    }

    public void setIn(ObjectInputStream in) { this.in = in; }
    public void setOut(ObjectOutputStream out) { this.out = out; }
    public void setReconexao(int reconexao) { this.reconexao = reconexao; }
    public void setSocket(Socket socket) { this.socket = socket; }
    public void setServidor(ConexaoServidor servidor) { this.servidor = servidor; }

    public ObjectInputStream getIn() { return in; }
    public ObjectOutputStream getOut() { return out; }
    public int getReconexao() { return reconexao; }
    public Socket getSocket() { return socket; }
    public ConexaoServidor getServidor() { return servidor; }

    public void run() {
        while (true) {
            try {
                ObjectOutputStream out = new ObjectOutputStream(getSocket().getOutputStream());
                ObjectInputStream in = new ObjectInputStream(getSocket().getInputStream());
                setOut(out);
                setIn(in);
                while (true) {
                    Mensagem mensagem = (Mensagem) in.readObject();
                    this.getServidor().respondeMensagem(getSocket(), in, out, mensagem);
                }
            } catch (IOException e) {
                if( this.getServidor().buscaJogador(this.getSocket()) == -1 ){
                    break;
                }
                if (this.getReconexao()>=3) {
                    System.err.println("Desconectando o cliente " + getSocket().getInetAddress());
                    Mensagem mensagem = new Mensagem("String", "Jogo:sair");
                    this.getServidor().respondeMensagem(this.getSocket(), this.getIn(), this.getOut(), mensagem);
                    break;
                }
                else{
                    System.err.println((this.getReconexao()+1)+"º Erro na comunicação com o cliente " + getSocket().getInetAddress()+". Tentando reconexão novamente em 5 segundos.");
                    Mensagem mensagem = new Mensagem("String", "Jogo:caiu");
                    this.getServidor().respondeMensagem(this.getSocket(), this.getIn(), this.getOut(), mensagem);this.setReconexao( this.getReconexao()+1 );
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}