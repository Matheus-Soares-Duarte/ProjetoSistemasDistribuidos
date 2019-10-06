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
    private transient Socket socketCliente;
    private transient ObjectOutputStream outCliente;
    private transient ObjectInputStream inCliente;
    private transient Socket socketServidor;
    private transient ObjectOutputStream outServidor;
    private transient ObjectInputStream inServidor;

    public Jogador(ConexaoCliente cliente){
        setAs(false);
        setPartidas(0);
        setPontos(0);
        setJogou(false);
        setVitorias(0);
        //Inserir os setters para escrever so a classe jogador no arquivo
        setSocketCliente(cliente.getSocket());
        setOutCliente(cliente.getOut());
        setInCliente(cliente.getIn());
        this.getMenu().inicio(cliente, this);
    }

    //setters
    void setAs(boolean as){ this.as = as; }
    void setCartas(List<Carta> cartas){ this.cartas = cartas; }
    void setInCliente(ObjectInputStream in){ this.inCliente = in; }
    void setInServidor(ObjectInputStream in){ this.inServidor = in; }
    public void setJogou(boolean jogou) { this.jogou = jogou; }
    void setMesa(int mesa){ this.mesa = mesa; }
    void setNome(String nome){ this.nome = nome; }
    void setOutCliente(ObjectOutputStream out){ this.outCliente = out; }
    void setOutServidor(ObjectOutputStream out){ this.outServidor = out; }
    void setPartidas(int partidas) { this.partidas = partidas; }
    void setPontos(int pontos){ this.pontos = pontos; }
    void setSocketCliente(Socket socket) { this.socketCliente = socket; }
    void setSocketServidor(Socket socket) { this.socketServidor = socket; }
    void setVitorias(int vitorias){ this.vitorias = vitorias; }

    //getters
    boolean getAs(){ return this.as; }
    List<Carta> getCartas(){ return cartas; }
    ObjectInputStream getInCliente(){ return this.inCliente; }
    ObjectInputStream getInServidor(){ return this.inServidor; }
    public boolean getJogou() { return jogou; }
    Menu getMenu(){ return this.menu; }
    int getMesa(){ return this.mesa; }
    String getNome(){ return this.nome; }
    ObjectOutputStream getOutCliente(){ return this.outCliente; }
    ObjectOutputStream getOutServidor(){ return this.outServidor; }
    int getPartidas(){ return this.partidas; }
    int getPontos(){
        if(this.getAs()==true && this.pontos+10<=21)
            return this.pontos+10;
        return this.pontos;
    }
    Socket getSocketCliente(){ return this.socketCliente; }
    Socket getSocketServidor(){ return this.socketServidor; }
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