import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
//        System.out.println("CRIANDO BARALHO:");
//        Baralho b = new Baralho();
//        b.mostraCartas();
//
//        System.out.println("\nEMBARALHANDO BARALHO:");
//        b.embaralhar();
//        b.mostraCartas();
//        System.out.println("Quantidade de cartas: " + b.getQuantidadeCartas());
//
//        System.out.println("\nENTREGANDO CARTAS:");
//        List<Carta> cartas = new ArrayList<Carta>();
//        cartas.add(b.entregarCarta());
//        System.out.println("Carta Entregue: " + cartas.get(cartas.size()-1).getCarta() );
//        System.out.println("Quantidade de cartas: " + b.getQuantidadeCartas());
//        cartas.add(b.entregarCarta());
//        System.out.println("Carta Entregue: " + cartas.get(cartas.size()-1).getCarta() );
//        System.out.println("Quantidade de cartas: " + b.getQuantidadeCartas());
//
//        System.out.println("\nVOLTANDO CARTAS AO BARALHO:");
//        while (cartas.size()>0){
//            System.out.println("Ultima Carta Entregue: " + cartas.get(cartas.size()-1).getCarta() );
//            b.addCarta(cartas.get(cartas.size()-1));
//            System.out.println("Quantidade de cartas: " + b.getQuantidadeCartas());
//            cartas.remove(cartas.size()-1);
//        }

//        Jogador j1 = new Jogador("1.1.1.1","Fulano");
//        Jogador j2 = new Jogador("2.2.2.2","Ciclano");
//        Mesa m1 = new Mesa(1, j1);
//        m1.iniciarJogo();
//        m1.setJogador(j2);
//        m1.iniciarJogo();

        Servidor servidor = new Servidor();
        ClientSide c1 = new ClientSide(servidor);
        ClientSide c2 = new ClientSide(servidor);

    }
}
