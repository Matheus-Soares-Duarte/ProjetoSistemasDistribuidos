import java.util.ArrayList;
import java.util.List;

public class Jogador {
    private String ip;
    private String nome;
    private List<Carta> cartas = new ArrayList<Carta>();
    private boolean as;
    private int valor;
    private int pontosRodada;
    //private int idMesa;

    public Jogador(String ip, String nome){
        setIp(ip);
        setNome(nome);
        setAs(false);
        setValor(0);
        setPontosRodada(0);
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
    void setValor(int valor){
        this.valor = valor;
    }
    void setPontosRodada(int pontos){ this.pontosRodada = pontos; }

    //getters
    boolean getAs(){ return this.as; }
    Carta getCarta(){ return cartas.get( cartas.size()-1 ); }
    List<Carta> getCartas(){ return cartas; }
    String getIp(){ return this.ip; }
    String getNome(){
        return this.nome;
    }
    int getValor(){
        if(this.getAs()==true && this.valor+10<=21)
            return this.valor+10;
        return this.valor;
    }
    int getPontosRodada(){
        return this.pontosRodada;
    }

    //funções proprias
    void addVitoria(){ setPontosRodada( getPontosRodada()+1 ); }

    void devolverCartas(){
        cartas.clear();
        setValor(0);
        setAs(false);
    }

    void mostrarCartas(){
        System.out.print("O jogador " + this.getNome() + " está com as cartas: ");
        for (Carta carta : cartas){
            System.out.print(carta.getCarta() + " - ");
        }
        System.out.println("Totalizando: "+getValor()+" pontos.");
    }

    void comprarCarta(){}
    void passarVez(){}
    void sairDaMesa(){}
}
