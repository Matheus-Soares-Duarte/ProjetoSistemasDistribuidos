package main.java;

import java.io.IOException;

public class Recebedor implements Runnable {
    private ConexaoCliente cliente;
    private int reconexao;

    public Recebedor(ConexaoCliente cliente) {
        setReconexao(0);
        this.setCliente(cliente);
    }

    public ConexaoCliente getCliente() { return cliente; }
    public int getReconexao() { return reconexao; }

    public void setCliente(ConexaoCliente cliente) { this.cliente = cliente; }
    public void setReconexao(int reconexao) { this.reconexao = reconexao; }

    public void run() {
        while (true) {
            try {
                while (true) {
                    Mensagem mensagem = (Mensagem) this.getCliente().getIn().readObject();
                    this.getCliente().analisaMesagem(mensagem);
                }
            } catch (IOException e) {
                if (this.getReconexao() >= 3) {
                    System.err.println("Desconectando do servidor " + getCliente().getSocket().getInetAddress()+" .");
                    break;
                } else {
                    this.setReconexao(this.getReconexao() + 1);
                    System.err.println((this.getReconexao()) + "º Erro na comunicação com o servidor " + getCliente().getSocket().getInetAddress() + ". Tentando reconexão novamente em 5 segundos.");
                    if(this.getReconexao()==1){
                        this.getCliente().getJogador().setEmReconexão(true);
                    }

                    if( this.getCliente().getJogador().getEmReconexão() ){
                        try {
                            Thread.sleep(5000);
                        } catch (InterruptedException ex) {
                            ex.printStackTrace();
                        }
                    }
                    Mensagem mensagem = new Mensagem("String", "ServidorCaiu:"+this.getCliente().getSocket().getInetAddress()+":"+this.getCliente().getSocket().getPort());
                    this.getCliente().analisaMesagem(mensagem);

                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } finally {
                try {
                    this.getCliente().getSocket().close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}