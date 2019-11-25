import io.grpc.StatusRuntimeException;
import stubs.ComunicacaoOuterClass;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Thread.sleep;

public class Mesa implements Serializable {
    private String chaveHash;
    private List<Jogador> jogadores = new ArrayList<Jogador>();
    private Baralho baralho = new Baralho();
    private ConexaoServidor servidor;

    public Mesa(String chaveHash, ConexaoServidor servidor){
        this.setChaveHash(chaveHash);
        this.setServidor(servidor);
    }

    void setBaralho(Baralho baralho){ this.baralho = baralho; }
    void setChaveHash(String chaveHash){ this.chaveHash = chaveHash; }
    void setServidor(ConexaoServidor servidor) { this.servidor = servidor; }
    void setJogadores(List<Jogador> jogadores){ this.jogadores = jogadores; }

    synchronized Baralho getBaralho(){ return this.baralho; }
    String getChaveHash() { return this.chaveHash; }
    ConexaoServidor getServidor() { return servidor; }
    List<Jogador> getJogadores(){ return this.jogadores; }

    boolean addJogador(Jogador jogador){
        ComunicacaoOuterClass.informacoesJogoResponse.Builder resposta = ComunicacaoOuterClass.informacoesJogoResponse.newBuilder();
        if(this.getJogadores().size()<5){
            this.getJogadores().add(jogador);
            jogador.setChaveHashMesa(this.getChaveHash());
            resposta.setCodigo(5).setMensagem("Sucesso ao entrar na mesa!").build();
            this.enviarResposta(resposta.build(), jogador);

            if(this.getJogadores().size() == 1){
                resposta.setCodigo(0).setMensagem("Esperando novos jogadores para iniciar o jogo!").build();
                this.enviarResposta(resposta.build(), jogador);
            } else {
                if (this.getJogadores().size() > 1) {
                    resposta.setCodigo(0).setMensagem("O Jogador " + jogador.getNome() + " acaba de entrar na sala " + this.getChaveHash() + "!").build();
                    this.enviarRespostaTodos(resposta.build());
                }
                if (this.getJogadores().size() == 2) {
                    this.iniciarRodada();
                } else if(this.getJogadores().size()>2){
                    resposta.setCodigo(3).setMensagem("Comprar Cartas Iniciais").build();
                    this.enviarResposta(resposta.build(), jogador);
                }
            }
            return true;
        } else {
            resposta.setCodigo(0).setMensagem("A sala "+this.getChaveHash()+" ja está cheia!").build();
            this.enviarResposta(resposta.build(), jogador);
            return false;
        }
    }

    Jogador buscaJogador(String ip, String nome){
        for (int i=0; i<this.getJogadores().size(); i++ ) {
            Jogador jogador=this.getJogadores().get(i);
            if(jogador.getIp().equals(ip) && jogador.getNome().equals(nome)){
                return jogador;
            }
        }
        return null;
    }

    int buscaJogadorVez(){
        int i;
        for ( i=0; i<this.getJogadores().size(); i++ ) {
            Jogador jogador=this.getJogadores().get(i);
            if(jogador.getJogou()==false){
                break;
            }
        }
        return i;
    }

    boolean enviarResposta(ComunicacaoOuterClass.informacoesJogoResponse resposta, Jogador jogador){
        int loop = 0;
        while (true) {
            try {
                if(jogador!=null && jogador.getResponseObserver()!=null) {
                    jogador.getResponseObserver().onNext(resposta);
                }
                loop=0;
                jogador.setEmReconexão(false);
                return true;
            } catch (StatusRuntimeException e) {
                loop++;
                jogador.setEmReconexão(true);
                if(loop>3){
                   this.retirarJogador(jogador);
                   return false;
                }
                switch (e.getStatus().getCode()) {
                    case UNAVAILABLE:
                        System.err.println("ERRO DE COMUNICAÇÃO: Cliente Indisponivel no momento. "+loop+" Tentativa de reenvio.");
                        try {
                            sleep(10000);
                        } catch (InterruptedException ex) { }
                        break;
                    case CANCELLED:
                        System.err.println("ERRO DE COMUNICAÇÃO: Cliente Indisponivel no momento. "+loop+" Tentativa de reenvio.");
                        try {
                            sleep(10000);
                        } catch (InterruptedException ex) { }
                        break;
                    default:
                        System.err.println("ERRO DE COMUNICAÇÃO: Erro não tratado "+e.getStatus().getCode()+". "+loop+" Tentativa de reenvio.");
                        break;
                }
            }
        }
    }

    void enviarRespostaTodos(ComunicacaoOuterClass.informacoesJogoResponse resposta){
        int i=0;
        while (true){
            if(i == this.getJogadores().size()){
                break;
            }
            Jogador jogador = this.getJogadores().get(i);
            if(!this.enviarResposta(resposta, jogador)){
                    i--;
            }
            i++;
        }
    }

    public void iniciarRodada(){
        ComunicacaoOuterClass.informacoesJogoResponse.Builder resposta = ComunicacaoOuterClass.informacoesJogoResponse.newBuilder();

        this.reiniciarRodada();
        resposta.setCodigo(4).setMensagem("ReiniciarRodada").build();
        this.enviarRespostaTodos(resposta.build());

        if(this.getJogadores().size() == 1) {
            resposta.setCodigo(0).setMensagem("Erro: Não existem jogadores suficientes na mesa para iniciar a rodada.").build();
            this.enviarResposta(resposta.build(), this.getJogadores().get(0));
        } else {
            resposta.setCodigo(0).setMensagem("\n--------------INICIANDO JOGO--------------").build();
            this.enviarRespostaTodos(resposta.build());

            resposta.setCodigo(3).setMensagem("Comprar Cartas Iniciais").build();
            this.enviarRespostaTodos(resposta.build());
            resposta.setCodigo(0).setMensagem("Vez do Jogador " + this.getJogadores().get(0).getNome() + ".").build();
            this.enviarRespostaTodos(resposta.build());

            resposta.setCodigo(2).setMensagem("Sua Vez").build();
            this.enviarResposta(resposta.build(), this.getJogadores().get(0));
        }
    }

    public void passarVez() {
        int indexJogadorComVez = this.buscaJogadorVez();
        if (indexJogadorComVez == this.getJogadores().size() || indexJogadorComVez+1 == this.getJogadores().size()) {
            this.verificarVitoria();
            this.score();
            this.iniciarRodada();
        } else {
            Jogador jogador = this.getJogadores().get(indexJogadorComVez);
            jogador.setJogou(true);

            Jogador proxJogadorComVez = this.getJogadores().get(indexJogadorComVez+1);
            ComunicacaoOuterClass.informacoesJogoResponse.Builder resposta = ComunicacaoOuterClass.informacoesJogoResponse.newBuilder();
            resposta.setCodigo(0).setMensagem("Vez do Jogador " + proxJogadorComVez.getNome() + ".").build();
            this.enviarRespostaTodos(resposta.build());

            resposta.setCodigo(2).setMensagem("Sua Vez").build();
            this.enviarResposta(resposta.build(), proxJogadorComVez);
        }
    }

    void reiniciarRodada(){
        for (Jogador jogador : this.getJogadores() ){
            for (Carta carta : jogador.getCartas()) {
                this.getBaralho().addCarta(carta);
            }
            jogador.devolverCartas();
            jogador.setJogou(false);
        }
        this.getBaralho().embaralhar();
    }

    boolean retirarJogador(Jogador jogador){
        int index = this.getJogadores().indexOf(jogador);
        if(index>=0){
            int indexVez = this.buscaJogadorVez();
            System.err.println("SAIDA DA SALA: " + jogador.getNome() + " saiu da sala " + this.getChaveHash() + "! Existe(em) " + this.getJogadores().size() + " Jogador(es) nessa Sala.");
            ComunicacaoOuterClass.informacoesJogoResponse.Builder resposta = ComunicacaoOuterClass.informacoesJogoResponse.newBuilder();
            resposta.setCodigo(0).setMensagem("O Jogador "+jogador.getNome()+ " acaba de abandonar a partida.").build();
            this.enviarRespostaTodos(resposta.build());

            if(index==indexVez){
                this.passarVez();
            }

            for (Carta carta : jogador.getCartas()) {
                this.getBaralho().addCarta(carta);
            }
            jogador.devolverCartas();
            jogador.setChaveHashMesa("");
            this.getJogadores().remove(jogador);
            this.getBaralho().embaralhar();

            return true;
        }
        return false;
    }

    void score(){
        int contador=1;
        String mensagem = "--------SCORE--------\n";
        for ( Jogador jogador : this.getJogadores() ){
            mensagem += "-Jogador nº "+contador+" ("+jogador.getNome()+"): "+jogador.getVitorias()+" Vitorias em "+jogador.getPartidas()+" Partidas.\n";
            contador++;
        }
        ComunicacaoOuterClass.informacoesJogoResponse.Builder resposta = ComunicacaoOuterClass.informacoesJogoResponse.newBuilder();
        resposta.setCodigo(0).setMensagem(mensagem).build();
        this.enviarRespostaTodos(resposta.build());
    }

    void verificarVitoria(){
        int maiorValor = 0;
        String mensagem = "\n------VENCEDORES------\n";
        ComunicacaoOuterClass.informacoesJogoResponse.Builder resposta = ComunicacaoOuterClass.informacoesJogoResponse.newBuilder();

        for ( Jogador jogador : this.getJogadores() ){
            if (jogador.getPontos()<=21 && jogador.getPontos()>maiorValor)
                maiorValor = jogador.getPontos();
        }

        if(maiorValor==0){
            mensagem += "NÃO HOUVERAM VENCEDORES NESTA RODADA.\n";
        } else {
            for (Jogador jogador : this.getJogadores()) {
                jogador.addPartida();
                if (jogador.getPontos() == maiorValor) {
                    resposta.setCodigo(1).setMensagem("Vitoria").build();
                    this.enviarResposta(resposta.build(), jogador);

                    mensagem += "VITORIA DE " + jogador.getNome() + " COM " + jogador.getPontos() + " PONTOS!\n";
                    jogador.addVitoria();
                }
            }
        }
        resposta.setCodigo(0).setMensagem(mensagem).build();
        this.enviarRespostaTodos(resposta.build());
    }
}