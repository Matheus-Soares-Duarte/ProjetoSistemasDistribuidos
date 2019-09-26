import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Cliente {
//    public static void main (String[] args){
//        Scanner sc = new Scanner(System.in);
//        System.out.print("Digite um nome: ");
//        String nome = sc.nextLine();
//        Jogador p = new Jogador("1.1.1.1",nome);
//        menuInicial m = new menuInicial();
//        m.RunMenuInicial(p);
//
//        sc.close();
//    }

    public Cliente (Servidor servidor){
        try {
            Scanner sc = new Scanner(System.in);

            System.out.print("Digite um nome: ");
            String nome = sc.nextLine();
            Jogador jogador = new Jogador(InetAddress.getLocalHost().getHostAddress(), nome);

            MenuInicial m = new MenuInicial();
            m.RunMenuInicial(servidor, jogador);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }
}
