import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Jogador implements Serializable {
    private boolean jogou;
    private boolean as;
    private List<Carta> cartas = new ArrayList<Carta>();
    private String nome;
    private int pontos;
    private int vitorias;
    private int partidas;
    private int mesa;
    private Menu menu = new Menu();
    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    public Jogador(ConexaoCliente cliente){
        setAs(false);
        setPartidas(0);
        setPontos(0);
        setJogou(false);
        setVitorias(0);
        this.setSocket(cliente.getSocket());
        this.setOut(cliente.getOut());
        this.setIn(cliente.getIn());
        this.getMenu().inicio(cliente, this);
    }

    //setters
    void setAs(boolean as){ this.as = as; }
    void setCartas(List<Carta> cartas){ this.cartas = cartas; }
    void setIn(ObjectInputStream in){ this.in = in; }
    public void setJogou(boolean jogou) { this.jogou = jogou; }
    void setMesa(int mesa){ this.mesa = mesa; }
    void setNome(String nome){ this.nome = nome; }
    void setOut(ObjectOutputStream out){ this.out = out; }
    void setPartidas(int partidas) { this.partidas = partidas; }
    void setPontos(int pontos){ this.pontos = pontos; }
    void setSocket(Socket socket) { this.socket = socket; }
    void setVitorias(int vitorias){ this.vitorias = vitorias; }

    //getters
    boolean getAs(){ return this.as; }
    List<Carta> getCartas(){ return cartas; }
    ObjectInputStream getIn(){ return this.in; }
    public boolean getJogou() { return jogou; }
    Menu getMenu(){ return this.menu; }
    int getMesa(){ return this.mesa; }
    String getNome(){ return this.nome; }
    ObjectOutputStream getOut(){ return this.out; }
    int getPartidas(){ return this.partidas; }
    int getPontos(){
        if(this.getAs()==true && this.pontos+10<=21)
            return this.pontos+10;
        return this.pontos;
    }
    Socket getSocket(){ return this.socket; }
    int getVitorias(){ return this.vitorias; }

    //funções proprias
    void addVitoria(){ setVitorias( getVitorias()+1 ); }
    void addPartida(){ setPartidas( getPartidas()+1 ); }
    void addCarta(Carta carta){ this.cartas.add(carta); }

    void comprarCarta(Carta carta){
        this.addCarta(carta);
        this.setPontos(this.getPontos() + carta.getValor());
        this.setJogou(false);
        if (carta.getValor() == 1) {
            this.setAs(true);
        }
    }

    void devolverCartas(){
        cartas.clear();
        setPontos(0);
        setAs(false);
        setJogou(false);
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
}