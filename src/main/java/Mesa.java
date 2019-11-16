import stubs.ComunicacaoOuterClass;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Mesa implements Serializable {
    private String chaveHash;
    private List<Jogador> jogadores = new ArrayList<Jogador>();
    private Baralho baralho = new Baralho();
    private ConexaoServidor servidor;
    private boolean iniciada;

    public Mesa(String chaveHash, ConexaoServidor servidor){
        this.setChaveHash(chaveHash);
        this.setServidor(servidor);
        this.setIniciada(false);

        Dealer dealer = new Dealer(this);
        new Thread(dealer).start();
    }

    void setBaralho(Baralho baralho){ this.baralho = baralho; }
    void setChaveHash(String chaveHash){ this.chaveHash = chaveHash; }
    void setIniciada(boolean iniciada){ this.iniciada = iniciada; }
    void setServidor(ConexaoServidor servidor) { this.servidor = servidor; }
    void setJogadores(List<Jogador> jogadores){ this.jogadores = jogadores; }

    Baralho getBaralho(){ return this.baralho; }
    String getChaveHash() { return this.chaveHash; }
    Boolean getIniciada() { return this.iniciada; }
    ConexaoServidor getServidor() { return servidor; }
    List<Jogador> getJogadores(){ return this.jogadores; }

    synchronized void acorda(){
        notifyAll();
    }

    boolean addJogador(Jogador jogador){
        ComunicacaoOuterClass.informacoesJogoResponse.Builder resposta = ComunicacaoOuterClass.informacoesJogoResponse.newBuilder();
        if(this.getJogadores().size()<5){
            this.getJogadores().add(jogador);
            jogador.setChaveHashMesa(this.getChaveHash());
            if(this.getJogadores().size() == 1){
                resposta.setCodigo(0).setMensagem("Esperando novos jogadores para iniciar o jogo!").build();
                this.enviarResposta(resposta.build(), jogador);
            } else {
                if (this.getIniciada()) {
                    resposta.setCodigo(3).setMensagem("Comprar Cartas Iniciais").build();
                    this.enviarResposta(resposta.build(), jogador);
                }

                if (this.getJogadores().size() > 1) {
                    resposta.setCodigo(0).setMensagem("O Jogador " + jogador.getNome() + " acaba de entrar na sala " + this.getChaveHash() + "!").build();
                    this.enviarRespostaTodos(resposta.build());
                }
                if (this.getJogadores().size() == 2 && !this.getIniciada()) {
                    this.acorda();
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

    synchronized void dorme(){
        try {
            this.wait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    void enviarResposta(ComunicacaoOuterClass.informacoesJogoResponse resposta, Jogador jogador){
        if(jogador!=null && !jogador.getEmReconexão() && jogador.getResponseObserver()!=null) {
            jogador.getResponseObserver().onNext(resposta);
        }
    }

    void enviarRespostaTodos(ComunicacaoOuterClass.informacoesJogoResponse resposta){
        for (Jogador jogador : this.getJogadores() ){
            if(!jogador.getEmReconexão()) {
                this.enviarResposta(resposta, jogador);
            }
        }
    }

    void reiniciarRodada(){
        ComunicacaoOuterClass.informacoesJogoResponse.Builder resposta = ComunicacaoOuterClass.informacoesJogoResponse.newBuilder();
        resposta.setCodigo(4).setMensagem("ReiniciarRodada").build();
        this.enviarRespostaTodos(resposta.build());

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
            for (Carta carta : jogador.getCartas()) {
                this.getBaralho().addCarta(carta);
            }
            jogador.devolverCartas();
            jogador.setChaveHashMesa("");
            jogador.getResponseObserver().onCompleted();
            this.getJogadores().remove(jogador);
            this.getBaralho().embaralhar();

            ComunicacaoOuterClass.informacoesJogoResponse.Builder resposta = ComunicacaoOuterClass.informacoesJogoResponse.newBuilder();
            resposta.setCodigo(0).setMensagem("O Jogador "+jogador.getNome()+ " acaba de abandonar a partida.").build();
            this.enviarRespostaTodos(resposta.build());

            if(index==indexVez){
                this.acorda();
            }
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