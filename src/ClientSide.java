
import menus.menuInicial;

import java.util.Scanner;

public class ClientSide {
    public static void main (String[] args){
        Scanner sc = new Scanner(System.in);
        System.out.println("Digite um apelido: ");
        String nome = sc.nextLine();
        Jogador p = new Jogador("1.1.1.1",nome);
        menuInicial m = new menuInicial();
        m.RunMenuInicial(p);

        sc.close();
    }
}
