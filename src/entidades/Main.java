package entidades;
import menus.menuInicial;

import java.util.Scanner;

public class Main {
    public static void main (String[] args){
        Scanner sc = new Scanner(System.in);
        System.out.println("Digite um apelido: ");
        String nome = sc.nextLine();
        player p = new player(nome);
        menuInicial m = new menuInicial();
        m.RunMenuInicial(p);

        sc.close();
    }
}
