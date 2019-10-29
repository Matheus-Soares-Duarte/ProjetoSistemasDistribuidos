package main.java;

import java.io.IOException;

public class Recebedor implements Runnable {
    private ConexaoCliente cliente;
    private int reconexao;

    public Recebedor(ConexaoCliente cliente) {
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
                    if (this.getReconexao() > 0) {
                        this.setReconexao(0);
                    }
                    this.getCliente().analisaMesagem(mensagem);
                }
            } catch (IOException e) {
                if (this.getReconexao() >= 3) {
                    System.err.println("Desconectando do servidor " + getCliente().getSocket().getInetAddress()+" .");
                    break;
                } else {
                    this.setReconexao(this.getReconexao() + 1);
                    System.err.println((this.getReconexao()) + "º Erro na comunicação com o servidor " + getCliente().getSocket().getInetAddress() + ". Tentando reconexão novamente em 5 segundos.");
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
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