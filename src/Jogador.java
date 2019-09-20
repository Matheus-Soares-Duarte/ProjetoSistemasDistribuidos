import java.util.ArrayList;
import java.util.List;

public class Jogador {
    private String ip;
    private String nome;
    private List<Carta> cartas = new ArrayList<Carta>();
    private boolean as;
    private int pontos;
    private int vitorias;
    //private int idMesa;

    public Jogador(String ip, String nome){
        setIp(ip);
        setNome(nome);
        setAs(false);
        setPontos(0);
        setVitorias(0);
    }

    //setters
    void setAs(boolean as){
        this.as = as;
    }
    void setCarta(Carta carta){
        this.cartas.add(carta);
    }
    void setIp(String ip){
        this.ip = ip;
    }
    void setNome(String nome){
        this.nome = nome;
    }
    void setPontos(int valor){
        this.pontos = valor;
    }
    void setVitorias(int valor){ this.vitorias = valor; }

    //getters
    boolean getAs(){ return this.as; }
    Carta getCarta(){ return cartas.get( cartas.size()-1 ); }
    List<Carta> getCartas(){ return cartas; }
    String getIp(){ return this.ip; }
    String getNome(){
        return this.nome;
    }
    int getPontos(){
        if(this.getAs()==true && this.pontos+10<=21)
            return this.pontos+10;
        return this.pontos;
    }
    int getVitorias(){
        return this.vitorias;
    }

    //funções proprias
    void addVitoria(){ setVitorias( getVitorias()+1 ); }

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
