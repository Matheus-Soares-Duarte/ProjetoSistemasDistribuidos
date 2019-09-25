import java.util.ArrayList;
import java.util.List;

public class Servidor {
    private List<Mesa> mesas = new ArrayList<Mesa>();

    void setMesas(List<Mesa> mesas){ this.mesas = mesas; }
    List<Mesa> getMesas(){ return this.mesas; }

    void addMesas(Mesa mesa){ this.mesas.add(mesa); }

    boolean addJogador(int numero, Jogador jogador){
        int index = buscaMesa(numero);
        if(index!=-1){
            this.getMesas().get(index).addJogador(jogador);
            return true;
        } else {
            System.out.println("A sala de numero "+numero+" não existe!");
            return false;
        }
    }

    int buscaMesa(int numero){
        for (Mesa mesa : this.getMesas()){
            if(mesa.getId() == numero){
                return this.getMesas().indexOf(mesa);
            }
        }
        return -1;
    }

    boolean criarMesa(int numero, Jogador jogador){
        int index = buscaMesa(numero);
        if(index==-1){
            Mesa mesa = new Mesa(numero, jogador);
            this.addMesas(mesa);
            System.out.println("A sala de numero "+numero+" foi criada com sucesso!");
            return true;
        } else {
            System.out.println("A sala de numero "+numero+" ja foi criada!");
            return false;
        }
    }

    boolean retiraJogador(int numero, Jogador jogador){
        int indexMesa = buscaMesa(numero);
        if(indexMesa!=-1){
            int indexJogador = this.getMesas().get(indexMesa).getJogadores().indexOf(jogador);
            if(indexJogador>=0) {
                this.getMesas().get(indexMesa).getJogadores().remove(indexJogador);
                return true;
            }
        } else {
            System.out.println("A sala de numero "+numero+" não existe!");
        }
        return false;
    }
}
