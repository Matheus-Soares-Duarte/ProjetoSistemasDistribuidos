import stubs.ComunicacaoGrpc;

import java.io.Serializable;

public class Finger implements Serializable {
    private String chaveHash;
    private String ip;
    private int porta;
    private transient ComunicacaoGrpc.ComunicacaoBlockingStub servidor;

    public Finger(String chaveHash, String ip, int porta, ComunicacaoGrpc.ComunicacaoBlockingStub servidor) {
        this.chaveHash = chaveHash;
        this.ip = ip;
        this.porta = porta;
        this.servidor = servidor;
    }

    public String getChaveHash() { return chaveHash; }
    public String getIp() { return ip; }
    public int getPorta() { return porta; }
    public ComunicacaoGrpc.ComunicacaoBlockingStub getServidor() { return servidor; }

    public void setChaveHash(String chaveHash) { this.chaveHash = chaveHash; }
    public void setIp(String ip) { this.ip = ip; }
    public void setPorta(int porta) { this.porta = porta; }
    public void setServidor(ComunicacaoGrpc.ComunicacaoBlockingStub servidor) { this.servidor = servidor; }
}
