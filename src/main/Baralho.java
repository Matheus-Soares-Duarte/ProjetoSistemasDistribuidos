import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Baralho implements Serializable{
    private int quantidadeBaralhos;
    private List<Carta> cartas = new ArrayList<Carta>();
    private String naipes[] = {"Paus", "Copas", "Espadas", "Ouros"};

    public Baralho(){
        setQuantidade(1);
        criaBaralho(1);
    }
    public Baralho(int quantidadeBaralhos){
        setQuantidade(quantidadeBaralhos);
        criaBaralho(quantidadeBaralhos);
    }

    void setQuantidade(int quantidadeBaralhos){
        this.quantidadeBaralhos = quantidadeBaralhos;
    }

    int getQuantidadeBaralhos(){
        return this.quantidadeBaralhos;
    }
    int getQuantidadeCartas(){
        return this.cartas.size();
    }

    void addCarta(Carta carta){ this.cartas.add(carta); }

    void criaBaralho(int quantidadeBaralhos)
    {
        for(int qtd=0; qtd<quantidadeBaralhos; qtd++)
        {
            for(int i=0; i<4; i++){
                cartas.add(new Carta("ÁS",naipes[i], 1));
                for(int j=2; j<=10; j++)
                    cartas.add(new Carta(String.valueOf(j), naipes[i], j));
                cartas.add(new Carta("J",naipes[i], 10));
                cartas.add(new Carta("Q",naipes[i], 10));
                cartas.add(new Carta("K",naipes[i], 10));
            }
        }
    }

    void mostrarCartas()
    {
        for(Carta c : cartas)
            System.out.println(c.getCarta());
    }

    void embaralhar()
    {
        Collections.shuffle(cartas);
    }

    Carta entregarCarta()
    {
        Carta c = this.cartas.get(this.cartas.size()-1);
        this.cartas.remove(this.cartas.size()-1);
        return c;
    }
}
