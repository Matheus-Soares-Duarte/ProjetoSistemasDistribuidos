import java.io.Serializable;
import java.util.Scanner;

public class Menu implements Serializable {
    static transient Scanner sc = new Scanner(System.in);

    public void inicio(ConexaoCliente cliente, Jogador jogador){
        System.out.print("\nDigite seu nome: ");
        String nome = sc.nextLine();
        jogador.setNome(nome);
        System.out.println("Ola "+ jogador.getNome()+", Seja Bem Vindo!");
        Mensagem mensagem = new Mensagem("Jogador", jogador);
        cliente.enviaMesagem(mensagem);
        this.escolhaInicial(cliente);
    }

    public void escolhaInicial(ConexaoCliente cliente) {
        boolean comandoOK = false;

        while(comandoOK == false) {
            System.out.println("\n-Para criar uma sala digite 'criar <numero da sala>'");
            System.out.println("-Para entrar em uma sala existente digite 'entrar <numero da sala>'");
            System.out.print("COMANDO: ");
            String comando = sc.next().toLowerCase();
            int numero = sc.nextInt();
            Mensagem mensagem = new Mensagem("String", "Inicial:"+comando+":"+numero);

            comandoOK=true;
            switch (comando) {
                case "criar":
                    cliente.enviaMesagem(mensagem);
                    break;
                case "entrar":
                    cliente.enviaMesagem(mensagem);
                    break;
                default:
                    comandoOK=false;
                    System.out.println("Comando Invalido: "+comando+". Por favor entre com um comando valido!");
                    break;
            }
        }
    }

    public void escolhaNaVez(Jogador jogador,ConexaoCliente cliente){
        boolean terminaLoop = false;

        while(terminaLoop == false) {
            jogador.mostrarCartas();
            System.out.println("-Para comprar mais uma carta digite 'comprar'");
            System.out.println("-Para passar a vez digite 'passar'");
            System.out.println("-Para sair da sala digite 'sair'");
            System.out.print("COMANDO: ");
            String comando = sc.next().toLowerCase();
            Mensagem mensagem = new Mensagem("String","NaVez:"+comando);

            terminaLoop=true;
            switch (comando) {
                case "comprar":
                    cliente.enviaMesagem(mensagem);
                    break;
                case "passar":
                    cliente.enviaMesagem(mensagem);
                    jogador.addPartida();
                    break;
                case "sair":
                    cliente.enviaMesagem(mensagem);
                    jogador.devolverCartas();
                    break;
                default:
                    terminaLoop=false;
                    System.out.println("Comando Invalido: "+comando+". Por favor entre com um comando valido!");
                    break;
            }
        }
    }
}