import java.util.Scanner;

public class menuInicial {
//    private final String comandoCriar = "-criar" ;
//    private final String comandoEntrar = "-entrar" ;
    Scanner sc = new Scanner(System.in);

    public void RunMenuInicial(Servidor servidor, Jogador jogador){
        System.out.println("Ola: "+ jogador.getNome());
        boolean comandoOK = false;

        while(comandoOK == false) {
            System.out.println("Para criar uma sala digite '-criar <numero da sala>'");
            System.out.println("Para entrar em uma sala existente digite '-entrar <numero da sala>'");
            System.out.print("COMANDO: ");
            String comando = sc.next().toLowerCase();
            int numero = sc.nextInt();

            switch (comando) {
                case "-criar":
                    System.out.println("Criando sala");//chama método no servidor
                    comandoOK=servidor.criarMesa(numero, jogador);
                    break;
                case "-entrar":
                    System.out.println("entrando na sala");//chama método no servidor
                    comandoOK=servidor.addJogador(numero, jogador);
                    break;
                default:
                    //throw new IllegalStateException("Unexpected value: " + comando);
                    System.out.println("Unexpected value: " + comando);
                    System.out.println("Por favor entre com um comando valido!");
            }
        }
    }
}
