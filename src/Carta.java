public class Carta
{
    private String letra;
    private String naipe;
    private int valor;

    public Carta(String letra, String naipe, int valor)
    {
        setLetra(letra);
        setNaipe(naipe);
        setValor(valor);
    }

    void setLetra(String l){
        this.letra = l;
    }
    void setNaipe(String n){
        this.naipe = n;
    }
    void setValor(int v){
        this.valor = v;
    }

    String getLetra(){
        return this.letra;
    }
    String getNaipe(){
        return this.naipe;
    }
    int getValor(){
        return this.valor;
    }

    String getCarta(){
        return getLetra()+" de "+getNaipe();
    }
}
