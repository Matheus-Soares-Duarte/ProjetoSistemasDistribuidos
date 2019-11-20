public class TemporizadorSnapshot implements Runnable {
    private ConexaoServidor servidor;
    private int tempoEspera;

    public TemporizadorSnapshot(ConexaoServidor servidor, int tempoEspera) {
        this.setServidor(servidor);
        this.setTempoEspera(tempoEspera);
    }

    public ConexaoServidor getServidor() { return servidor; }
    public int getTempoEspera() { return tempoEspera; }

    public void setServidor(ConexaoServidor servidor) { this.servidor = servidor; }
    public void setTempoEspera(int tempoEspera) { this.tempoEspera = tempoEspera; }

    @Override
    public void run() {
        while (this.getServidor()!=null) {
            try {
                Thread.sleep(this.getTempoEspera());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if(this.getServidor()!=null) {
                this.getServidor().setEscreverSnapshot(true);
                this.getServidor().dorme();
            }
        }
    }
}
