import io.grpc.stub.StreamObserver;
import stubs.ComunicacaoOuterClass;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Jogador implements Serializable {
    private boolean jogou;
    private boolean as;
    private List<Carta> cartas = new ArrayList<Carta>();
    private String nome;
    private int pontos;
    private boolean emReconexão;
    private int vitorias;
    private int partidas;
    private String chaveHashMesa;
    private Menu menu = new Menu();
    private String ip;
    private StreamObserver<ComunicacaoOuterClass.informacoesJogoResponse> responseObserver;

    public Jogador(String ip){
        setAs(false);
        setPartidas(0);
        setPontos(0);
        setEmReconexão(false);
        setJogou(false);
        setIp(ip);
        setVitorias(0);
    }
    public Jogador(){
        setAs(false);
        setPartidas(0);
        setPontos(0);
        setEmReconexão(false);
        setJogou(false);
        setVitorias(0);
    }

    //setters
    void setAs(boolean as){ this.as = as; }
    void setCartas(List<Carta> cartas){ this.cartas = cartas; }
    void setEmReconexão(boolean emReconexão) { this.emReconexão = emReconexão; }
    void setIp(String ip){ this.ip = ip; }
    public void setJogou(boolean jogou) { this.jogou = jogou; }
    void setChaveHashMesa(String chaveHashMesa){ this.chaveHashMesa = chaveHashMesa; }
    void setNome(String nome){ this.nome = nome; }
    public void setPartidas(int partidas) { this.partidas = partidas; }
    void setPontos(int pontos){ this.pontos = pontos; }
    void setResponseObserver(StreamObserver<ComunicacaoOuterClass.informacoesJogoResponse> responseObserver) { this.responseObserver = responseObserver; }
    public void setVitorias(int vitorias){ this.vitorias = vitorias; }


    //getters
    public boolean getAs(){ return this.as; }
    public List<Carta> getCartas(){ return cartas; }
    boolean getEmReconexão() { return emReconexão; }
    String getIp(){ return this.ip; }
    public boolean getJogou() { return jogou; }
    Menu getMenu(){ return this.menu; }
    String getChaveHashMesa(){ return this.chaveHashMesa; }
    String getNome(){ return this.nome; }
    public int getPartidas(){ return this.partidas; }
    public int getPontos(){
        if(this.getAs()==true && this.pontos+10<=21)
            return this.pontos+10;
        return this.pontos;
    }

    StreamObserver<ComunicacaoOuterClass.informacoesJogoResponse> getResponseObserver() { return this.responseObserver; }
    public int getVitorias(){ return this.vitorias; }

    //funções proprias
    public void addVitoria(){ setVitorias( getVitorias()+1 ); }
    public void addPartida(){ setPartidas( getPartidas()+1 ); }
    public void addCarta(Carta carta){ this.cartas.add(carta); }

    public void comprarCarta(Carta carta){
        this.addCarta(carta);
        this.setPontos(this.getPontos() + carta.getValor());
        this.setJogou(false);
        if (carta.getValor() == 1) {
            this.setAs(true);
        }
    }

    public void devolverCartas(){
        cartas.clear();
        setPontos(0);
        setAs(false);
        setJogou(false);
    }

    void mostrarCartas(){
        System.out.println("--------CARTAS--------");
        System.out.print("> ");
        for (Carta carta : this.getCartas()){
            System.out.print(carta.getCarta());
            if(carta!=this.getCartas().get(this.getCartas().size()-1)){
                System.out.print(" - ");
            } else {
                System.out.print(".");
            }
        }
        System.out.println("\n> SOMA DE PONTOS: "+getPontos()+" Pontos.");
        System.out.println("---------------------");
    }
}