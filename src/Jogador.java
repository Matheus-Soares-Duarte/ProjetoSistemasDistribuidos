import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Jogador {
    private final String comandoComprar = "-comprar" ;
    private final String comandoPassar = "-passar" ;
    private final String comandoSair = "-sair" ;

    private String ip;
    private String nome;
    private List<Carta> cartas = new ArrayList<Carta>();
    private boolean as;
    private int pontos;
    private int vitorias;
    private int partidas;
    private Mesa mesa;
    Scanner sc = new Scanner(System.in);

    public Jogador(String ip, String nome){
        setIp(ip);
        setNome(nome);
        setAs(false);
        setPontos(0);
        setVitorias(0);
        setPartidas(0);
    }

    //setters
    void setAs(boolean as){ this.as = as; }
    void setCarta(Carta carta){ this.cartas.add(carta); }
    void setIp(String ip){ this.ip = ip; }
    void setMesa(Mesa mesa){ this.mesa = mesa; }
    void setNome(String nome){ this.nome = nome; }
    void setPontos(int pontos){ this.pontos = pontos; }
    void setVitorias(int vitorias){ this.vitorias = vitorias; }
    void setPartidas(int partidas) { this.partidas = partidas; }

    //getters
    boolean getAs(){ return this.as; }
    Carta getCarta(){ return cartas.get( cartas.size()-1 ); }
    List<Carta> getCartas(){ return cartas; }
    String getIp(){ return this.ip; }
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

    void devolverCartas(){
        cartas.clear();
        setPontos(0);
        setAs(false);
    }

    void mostrarCartas(){
        System.out.print("O jogador " + this.getNome() + " está com as cartas: ");
        for (Carta carta : cartas){
            System.out.print(carta.getCarta() + " - ");
        }
        System.out.println("Totalizando: "+getPontos()+" pontos.");
    }

    void comprarCarta(){}
    void passarVez(){}
    void sairDaMesa(){}
}
