import java.io.Serializable;
import java.util.Scanner;

public class Menu implements Serializable {
    static transient Scanner sc = new Scanner(System.in);

    public void inicio(ConexaoCliente cliente, Jogador jogador){
        System.out.print("\nDigite seu nome: ");
        String nome = sc.nextLine();
        jogador.setNome(nome);
        System.out.println("Ola "+ jogador.getNome()+", Seja Bem Vindo!");
        this.escolhaInicial(cliente);
    }

    public void escolhaInicial(ConexaoCliente cliente) {
        while(true) {
            System.out.println("\n-Para criar uma sala digite 'criar sala'");
            System.out.println("-Para entrar em uma sala existente digite 'entrar <identificador da sala>'");
            System.out.println("-Para sair do jogo digite 'sair do jogo'");
            System.out.print("COMANDO: ");
            String linha = sc.nextLine().toLowerCase();
            String conteudoSeparado[] = linha.split(" ");

            if(conteudoSeparado.length<=3) {
                if(linha.equals("criar sala")){
                    stubs.ComunicacaoOuterClass.criarMesaRequest criarMesaRequest = stubs.ComunicacaoOuterClass.criarMesaRequest.newBuilder()
                            .setIp(cliente.getJogador().getIp())
                            .setNome(cliente.getJogador().getNome())
                            .build();
                    cliente.realizaRequisicao(conteudoSeparado[0], criarMesaRequest);
                    break;
                } else if(conteudoSeparado.length==2 && conteudoSeparado[0].equals("entrar")){
                    stubs.ComunicacaoOuterClass.entrarMesaRequest entrarMesaRequest = stubs.ComunicacaoOuterClass.entrarMesaRequest.newBuilder()
                            .setChaveHashMesa(conteudoSeparado[1])
                            .setIp(cliente.getJogador().getIp())
                            .setNome(cliente.getJogador().getNome())
                            .setVitorias(cliente.getJogador().getVitorias())
                            .setPartidas(cliente.getJogador().getPartidas())
                            .build();
                    cliente.realizaRequisicao(conteudoSeparado[0], entrarMesaRequest);
                    break;
                } else if(linha.equals("sair do jogo")){
                    cliente.realizaRequisicao(linha, "");
                    break;
                }
            }
            System.err.println("Comando Invalido: " + linha + ". Por favor entre com um comando valido!");
        }
    }

    public void escolhaNaVez(Jogador jogador,ConexaoCliente cliente){
        String comando = "";

        while(true) {
            System.out.println("-Para comprar mais uma carta digite 'comprar'");
            System.out.println("-Para passar a vez digite 'passar'");
            System.out.println("-Para sair da sala digite 'sair'");
            System.out.print("COMANDO: ");
            comando = sc.next().toLowerCase();

            if( comando.equals("comprar") || comando.equals("passar") || comando.equals("sair") ) {
                break;
            } else {
                System.err.println("Comando Invalido: " + comando + ". Por favor entre com um comando valido!");
            }
        }
        stubs.ComunicacaoOuterClass.requisicaoNaVezRequest requisicaoNaVezRequest = stubs.ComunicacaoOuterClass.requisicaoNaVezRequest.newBuilder()
                .setChaveHashMesa(jogador.getChaveHashMesa())
                .setIp(cliente.getJogador().getIp())
                .setNome(cliente.getJogador().getNome())
                .build();
        cliente.realizaRequisicao(comando, requisicaoNaVezRequest);
    }
}