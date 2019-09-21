package menus;
import java.util.Scanner;

import entidades.player;


public class menuInicial {
    public void RunMenuInicial(Jogador p){
        System.out.println("Ola: "+ p.getNome());
        System.out.println("criar uma sala '-criar <numero da sala>'");
        System.out.println("entrar em uma sala exixtente '-entrar <numero da sala>'");
        Scanner sc = new Scanner(System.in);
        String comando = sc.next();
        int numero = sc.nextInt();

        switch (comando){
            case "-criar":
                System.out.println("Criando sala");//chama método no servidor
                break;
            case "-entrar" : System.out.println("entrando na sala");//chama método no servidor
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + comando);
        }
        sc.close();
    }

}
