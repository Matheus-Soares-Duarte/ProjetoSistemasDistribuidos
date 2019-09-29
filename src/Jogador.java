import java.util.ArrayList;
import java.util.List;

public class Jogador {
    private String ip;
    private String nome;
    private List<Carta> cartas = new ArrayList<Carta>();
    private boolean as;
    private int pontos;
    private int vitorias;
    private int partidas;
    private Mesa mesa;
    private Menu menu = new Menu();

    public Jogador(ConexaoCliente cliente){
        this.getMenu().inicio(cliente, this);
        setAs(false);
        setPontos(0);
        setVitorias(0);
        setPartidas(0);
    }

    //setters
    void setAs(boolean as){ this.as = as; }
    void setCartas(List<Carta> cartas){ this.cartas = cartas; }
    void setIp(String ip){ this.ip = ip; }
    void setMesa(Mesa mesa){ this.mesa = mesa; }
    void setNome(String nome){ this.nome = nome; }
    void setPontos(int pontos){ this.pontos = pontos; }
    void setVitorias(int vitorias){ this.vitorias = vitorias; }
    void setPartidas(int partidas) { this.partidas = partidas; }

    //getters
    boolean getAs(){ return this.as; }
    List<Carta> getCartas(){ return cartas; }
    String getIp(){ return this.ip; }
    Menu getMenu(){ return this.menu; }
    Mesa getMesa(){ return this.mesa; }
    String getNome(){ return this.nome; }
    int getPontos(){
        if(this.getAs()==true && this.pontos+10<=21)
            return this.pontos+10;
        return this.pontos;
    }
    int getVitorias(){ return this.vitorias; }
    int getPartidas(){ return this.partidas; }

    //funções proprias
    void addVitoria(){ setVitorias( getVitorias()+1 ); }
    void addPartida(){ setPartidas( getPartidas()+1 ); }
    void addCarta(Carta carta){ this.cartas.add(carta); }

    void devolverCartas(){
        cartas.clear();
        setPontos(0);
        setAs(false);
    }

    void mostrarCartas(){
        System.out.print("CARTAS: ");
        for (Carta carta : cartas){
            System.out.print(carta.getCarta());
            if(carta!=this.getCartas().get(this.getCartas().size()-1)){
                System.out.print(" - ");
            } else {
                System.out.print(".");
            }

        }
        System.out.println("\nSOMA DE PONTOS: "+getPontos()+" Pontos.");
    }

    void fazerEscolha(){ this.getMenu().escolha( this.getMesa(), this); }

    Carta comprarCarta(){
        Carta carta = this.getMesa().getBaralho().entregarCarta();
        this.addCarta(carta);
        this.setPontos(this.getPontos() + carta.getValor());
        if (carta.getValor() == 1) {
            this.setAs(true);
        }
        return carta;
    }

    void sairDaMesa(){ this.getMesa().retirarJogador(this); }
}
