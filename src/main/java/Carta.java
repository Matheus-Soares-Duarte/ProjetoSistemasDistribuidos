import java.io.Serializable;

public class Carta implements Serializable {
    private String letra;
    private String naipe;
    private int valor;

    public Carta(String letra, String naipe, int valor)
    {
        setLetra(letra);
        setNaipe(naipe);
        setValor(valor);
    }

    //setters
    void setLetra(String letra){
        this.letra = letra;
    }
    void setNaipe(String naipe){
        this.naipe = naipe;
    }
    void setValor(int valor){
        this.valor = valor;
    }

    //getters
    String getCarta(){
        return getLetra()+" de "+getNaipe();
    }
    public String getLetra(){
        return this.letra;
    }
    public String getNaipe(){
        return this.naipe;
    }
    int getValor(){
        return this.valor;
    }
}
