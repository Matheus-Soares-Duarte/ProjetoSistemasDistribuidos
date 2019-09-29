import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Jogador implements Serializable {
    private String ip;
    private String nome;
    private List<Carta> cartas = new ArrayList<Carta>();
    private boolean as;
    private int pontos;
    private int vitorias;
    private int partidas;
    private int mesa;
    private Menu menu = new Menu();
    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;

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
    void setIn(ObjectInputStream in){ this.in = in; }
    void setIp(String ip){ this.ip = ip; }
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
    String getIp(){ return this.ip; }
    Menu getMenu(){ return this.menu; }
    int getMesa(){ return this.mesa; }
    String getNome(){ return this.nome; }
    ObjectOutputStream getOut(){ return this.out; }
    int getPontos(){
        if(this.getAs()==true && this.pontos+10<=21)
            return this.pontos+10;
        return this.pontos;
    }
    Socket getSocket(){ return this.socket; }
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

    void fazerEscolha(){
//        this.getMenu().escolha( this.getMesa(), this);
    }

//    Carta comprarCarta(){
//        Carta carta = this.getMesa().getBaralho().entregarCarta();
//        this.addCarta(carta);
//        this.setPontos(this.getPontos() + carta.getValor());
//        if (carta.getValor() == 1) {
//            this.setAs(true);
//        }
//        return carta;
//    }

    void sairDaMesa(){
//        this.getMesa().retirarJogador(this);
    }
}
