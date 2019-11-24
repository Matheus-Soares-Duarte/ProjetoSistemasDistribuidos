public class Repasse {
    boolean atualUltimo;
    boolean realiza;
    Finger proximoServidor;

    public boolean getAtualUltimo() { return atualUltimo; }
    public Finger getProximoServidor() { return proximoServidor; }
    public boolean getRealiza() { return realiza; }

    public void setAtualUltimo(boolean atualUltimo) { this.atualUltimo = atualUltimo; }
    public void setProximoServidor(Finger proximoServidor) { this.proximoServidor = proximoServidor; }
    public void setRealiza(boolean realiza) { this.realiza = realiza; }
}
