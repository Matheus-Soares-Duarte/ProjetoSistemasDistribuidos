import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import stubs.ComunicacaoGrpc;
import stubs.ComunicacaoOuterClass;

import java.io.*;
import java.net.*;
import java.util.Iterator;
import java.util.Properties;

import static java.lang.Thread.sleep;

public class ConexaoCliente {
    private Jogador jogador;
    private int porta;
    private ComunicacaoGrpc.ComunicacaoBlockingStub servidor;

    public ConexaoCliente(int porta){
        this.setPorta(porta);
    }

    public Jogador getJogador() { return jogador; }
    public int getPorta() { return porta; }
    public ComunicacaoGrpc.ComunicacaoBlockingStub getServidor() { return servidor; }

    public void setJogador(Jogador jogador) { this.jogador = jogador; }
    public void setPorta(int porta) { this.porta = porta; }
    public void setServidor(ComunicacaoGrpc.ComunicacaoBlockingStub servidor) { this.servidor = servidor; }

    public void criarConexaoGRPC(String ipServidor, int porta){
        int loop = 0;
        while (loop < 3) {
            try {
                String ip = IpCorreto.getIpCorreto();
                ManagedChannel canal = ManagedChannelBuilder.forAddress(ipServidor, porta).usePlaintext().build();
                ComunicacaoGrpc.ComunicacaoBlockingStub servidor = ComunicacaoGrpc.newBlockingStub(canal);
                this.setServidor(servidor);
                System.out.println("O cliente " + ip + " se conectou ao servidor " + ipServidor + "!");

                this.setJogador(new Jogador(ip));
                this.getJogador().getMenu().inicio(this, this.getJogador());

                loop = 0;
                break;
            } catch (IOException e) {
                e.printStackTrace();
            } catch (StatusRuntimeException e) {
                loop++;
                switch (e.getStatus().getCode()) {
                    case UNAVAILABLE:
                        System.err.println("Sistema Indisponivel no momento. "+loop+" Tentativa de reenvio.");
                        try {
                            sleep(10000);
                        } catch (InterruptedException ex) {
                        }
                        break;
                    default:
                        System.err.println("Erro não tratado "+e.getStatus().getCode()+". "+loop+" Tentativa de reenvio.");
                        break;
                }
            }
        }
    }

    void realizaRequisicao(String tipo, Object request){
        int loop = 0;
        while (loop < 3) {
            try {
                switch (tipo){
                    case "criar":
                        ComunicacaoOuterClass.criarMesaResponse criarMesaResponse = this.getServidor().criarMesa((ComunicacaoOuterClass.criarMesaRequest) request);
                        System.out.println(criarMesaResponse.getMensagem());
                        if (criarMesaResponse.getCodigo()==0){
                            stubs.ComunicacaoOuterClass.entrarMesaRequest entrarMesaRequest = stubs.ComunicacaoOuterClass.entrarMesaRequest.newBuilder()
                                    .setChaveHashMesa(criarMesaResponse.getChaveHashMesa())
                                    .setIp(((ComunicacaoOuterClass.criarMesaRequest) request).getIp())
                                    .setNome(((ComunicacaoOuterClass.criarMesaRequest) request).getNome())
                                    .setVitorias(this.getJogador().getVitorias())
                                    .setPartidas(this.getJogador().getPartidas())
                                    .build();
                            this.realizaRequisicao("entrar", entrarMesaRequest);
                        } else {
                            this.getJogador().getMenu().escolhaInicial(this);
                        }
                        break;
                    case "entrar":
                        Iterator<ComunicacaoOuterClass.informacoesJogoResponse> informacoesJogoResponse = this.getServidor().entrarMesa((ComunicacaoOuterClass.entrarMesaRequest) request);
                        this.getJogador().setChaveHashMesa(((ComunicacaoOuterClass.entrarMesaRequest) request).getChaveHashMesa());

                        stubs.ComunicacaoOuterClass.requisicaoNaVezRequest requisicaoNaVezRequest = stubs.ComunicacaoOuterClass.requisicaoNaVezRequest.newBuilder()
                                .setChaveHashMesa(((ComunicacaoOuterClass.entrarMesaRequest) request).getChaveHashMesa())
                                .setIp(((ComunicacaoOuterClass.entrarMesaRequest) request).getIp())
                                .setNome(((ComunicacaoOuterClass.entrarMesaRequest) request).getNome())
                                .build();

                        for (Iterator<ComunicacaoOuterClass.informacoesJogoResponse> it = informacoesJogoResponse; it.hasNext(); ) {
                            ComunicacaoOuterClass.informacoesJogoResponse resposta = it.next();
                            if(resposta.getCodigo()==4){ //reiniciar rodada
                                this.getJogador().devolverCartas();
                            } else if(resposta.getCodigo()==3){ //comprar cartas iniciais
                                for(int i=0; i<2; i++){
                                    this.realizaRequisicao("comprar", requisicaoNaVezRequest);
                                }
                                this.getJogador().mostrarCartas();
                            } else if(resposta.getCodigo()==2){ //vez do jogador
                                System.out.println("Sua Vez de Jogar.");
                                this.getJogador().getMenu().escolhaNaVez(this.getJogador(), this);
                            } else if(resposta.getCodigo()==1) { //vitoria
                                this.getJogador().addVitoria();
                            } else if(resposta.getCodigo()==0) { //mensagem de mudança na mesa
                                System.out.println(resposta.getMensagem());
                            } else {
                                System.err.println(resposta.getMensagem());
                                this.getJogador().setChaveHashMesa("");
                                this.getJogador().getMenu().escolhaInicial(this);
                            }
                        }
                        break;
                    case "sair do jogo":
                        System.out.println("Obrigado por jogar. Saindo do jogo.");
                        System.exit(0);
                        break;
                    case "comprar":
                        ComunicacaoOuterClass.comprarCartaResponse comprarCartaResponse = this.getServidor().comprarCarta((ComunicacaoOuterClass.requisicaoNaVezRequest) request);
                        if (comprarCartaResponse.getCodigo()==0){
                            Carta carta = new Carta(comprarCartaResponse.getLetra(), comprarCartaResponse.getNaipe(), comprarCartaResponse.getValor());
                            this.getJogador().comprarCarta(carta);

                            if(this.getJogador().getCartas().size() > 2){
                                this.getJogador().mostrarCartas();

                                if (this.getJogador().getPontos()>21){
                                    this.getJogador().setJogou(true);

                                    System.out.println(this.getJogador().getNome()+" ESTOUROU COM "+this.getJogador().getPontos()+" PONTOS.");
                                    request = stubs.ComunicacaoOuterClass.requisicaoNaVezRequest.newBuilder()
                                            .setIp(this.getJogador().getIp())
                                            .setNome(this.getJogador().getNome())
                                            .setChaveHashMesa(this.getJogador().getChaveHashMesa())
                                            .build();
                                    this.realizaRequisicao("passar", request);
                                } else {
                                    this.getJogador().getMenu().escolhaNaVez(this.getJogador(),this);
                                }
                            }
                        } else {
                            this.getJogador().getMenu().escolhaNaVez(this.getJogador(), this);
                        }
                        break;
                    case "passar":
                        ComunicacaoOuterClass.passarVezResponse passarVezResponse = this.getServidor().passarVez((ComunicacaoOuterClass.requisicaoNaVezRequest) request);
                        if (passarVezResponse.getCodigo()==0){
                            this.getJogador().setJogou(true);
                            this.getJogador().addPartida();
                        } else {
                            System.err.println(passarVezResponse.getMensagem());
                            this.getJogador().getMenu().escolhaNaVez(this.getJogador(), this);
                        }
                        break;
                    case "sair":
                        ComunicacaoOuterClass.sairMesaResponse sairMesaResponse = this.getServidor().sairMesa((ComunicacaoOuterClass.requisicaoNaVezRequest) request);
                        if (sairMesaResponse.getCodigo()==0){
                            this.getJogador().devolverCartas();
                            this.getJogador().setChaveHashMesa("");
                            System.out.println(sairMesaResponse.getMensagem());
                            this.getJogador().getMenu().escolhaInicial(this);
                        } else {
                            this.getJogador().getMenu().escolhaNaVez(this.getJogador(), this);
                        }
                        break;
                    default:
                        System.err.println("Mensagem gRPC não esperada.");
                        break;
                }

                loop = 0;
                break;
            } catch (StatusRuntimeException e) {
                loop++;
                switch (e.getStatus().getCode()) {
                    case UNAVAILABLE:
                        System.err.println("Sistema Indisponivel no momento. "+loop+" Tentativa de reenvio.");
                        try {
                            sleep(10000);
                        } catch (InterruptedException ex) {
                        }
                        break;
                    default:
                        System.err.println("Erro não tratado "+e.getStatus().getCode()+". "+loop+" Tentativa de reenvio.");
                        break;
                }
            }
        }
    }
}