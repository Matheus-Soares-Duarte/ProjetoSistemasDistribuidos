import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Menu {
    Scanner sc = new Scanner(System.in);

    public void inicio(ConexaoCliente cliente, Jogador jogador){
        try {
            System.out.print("\nDigite seu nome: ");
            String nome = sc.nextLine();
            jogador.setNome(nome);
            jogador.setIp(InetAddress.getLocalHost().getHostAddress());
            System.out.println("Ola "+ jogador.getNome()+", Seja Bem Vindo!");
            boolean comandoOK = false;

            while(comandoOK == false) {
                System.out.println("\n-Para criar uma sala digite 'criar <numero da sala>'");
                System.out.println("-Para entrar em uma sala existente digite 'entrar <numero da sala>'");
                System.out.print("COMANDO: ");
                String comando = sc.next().toLowerCase();
                int numero = sc.nextInt();
                Mensagem mensagem = new Mensagem("String", comando+":"+numero);

                switch (comando) {
                    case "criar":
                        cliente.enviaMesagem(mensagem);
                        comandoOK=true;
                        break;
                    case "entrar":
                        cliente.enviaMesagem(mensagem);
                        comandoOK=true;
                        break;
                    default:
                        //throw new IllegalStateException("Unexpected value: " + comando);
                        System.out.println("Unexpected value: " + comando);
                        System.out.println("Por favor entre com um comando valido!");
                }
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    public void escolha(Mesa mesa, Jogador jogador){
        boolean terminaLoop = false;

        while(terminaLoop == false) {
            System.out.println("\nVEZ DE ESCOLHA DO JOGADOR "+(mesa.getJogadores().indexOf(jogador)+1)+" ("+jogador.getNome()+"):");
            jogador.mostrarCartas();
            System.out.println("-Para comprar mais uma carta digite 'comprar'");
            System.out.println("-Para passar a vez digite 'passar'");
            System.out.println("-Para sair da sala digite 'sair'");
            System.out.print("COMANDO: ");
            String comando = sc.next().toLowerCase();

            switch (comando) {
                case "comprar":
                    Carta carta = jogador.comprarCarta();
                    System.out.println("A Carta comprada foi: "+carta.getCarta()+".");
                    jogador.mostrarCartas();
                    if (jogador.getPontos()>21){
                        System.out.println(jogador.getNome()+" ESTOUROU COM "+jogador.getPontos()+" PONTOS.");
                        terminaLoop =  true;
                    } else{
                        terminaLoop = false;
                    }
                    break;
                case "passar":
                    System.out.println(jogador.getNome()+" PASSOU A VEZ.");
                    terminaLoop=true;
                    break;
                case "sair":
                    jogador.sairDaMesa();
                    terminaLoop=true;
                    break;
                default:
                    //throw new IllegalStateException("Unexpected value: " + comando);
                    System.out.println("Unexpected value: " + comando);
                    System.out.println("Por favor entre com um comando valido!");
            }
        }
    }
}
