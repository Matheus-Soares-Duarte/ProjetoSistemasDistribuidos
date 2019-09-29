import java.util.ArrayList;
import java.util.List;

public class Mesa {
    private int id;
    private List<Jogador> jogadores = new ArrayList<Jogador>();
    private Baralho baralho = new Baralho();

    public Mesa(int id, Jogador jogador){
        setId(id);
        addJogador(jogador);
    }

    void setBaralho(Baralho baralho){ this.baralho = baralho; }
    void setId(int id){ this.id = id; }
    void setJogadores(List<Jogador> jogadores){ this.jogadores = jogadores; }

    Baralho getBaralho(){ return this.baralho; }
    int getId(){ return this.id; }
    List<Jogador> getJogadores(){ return this.jogadores; }

    void addJogador(Jogador jogador){
        this.getJogadores().add(jogador);
        jogador.setMesa(this.getId());
        if(this.getJogadores().size()!=1) {
            System.out.println("O Jogador " + jogador.getNome() + " acaba de entrar nesta partida!");
        }
        if(this.getJogadores().size() == 2){
            this.iniciarJogo();
        }
    }

    void iniciarJogo(){
        System.out.println("\n--------------INICIANDO JOGO--------------");

        while(this.getJogadores().size()>1){
            System.out.println("\n--------INICIANDO RODADA--------");
            this.reiniciarRodada();

//            for (Jogador jogador : this.getJogadores() ) {
//                for (int i=0; i<2; i++) { jogador.comprarCarta(); }
//            }

//            for ( Jogador jogador : this.getJogadores() ) { jogador.fazerEscolha(); }
            this.verificarVitoria();
            this.score();
        }

        if(this.getJogadores().size()<2){
            System.out.println("Não existem jogadores suficientes na mesa para iniciar a rodada.");
        }
    }

    void reiniciarRodada(){
        for (Jogador jogador : this.getJogadores() ){
            for (Carta carta : jogador.getCartas()) {
                this.getBaralho().addCarta(carta);
            }
            jogador.devolverCartas();
        }
        this.getBaralho().embaralhar();
    }

    void retirarJogador(Jogador jogador){
        int index = this.getJogadores().indexOf(jogador);
        if(index>=0){
            System.out.println("O Jogador "+jogador.getNome()+" acaba de abandonar a partida.");
            this.getJogadores().remove(index);
//            jogador.setMesa(null);
        }
    }

    void score(){
        int i=1;
        System.out.println("--------SCORE--------");
        for ( Jogador jogador : this.getJogadores() ){
            System.out.println("-Jogador nº "+i+" ("+jogador.getNome()+"): "+jogador.getVitorias()+" Vitorias em "+jogador.getPartidas()+" Partidas.");
            i++;
        }
    }

    void verificarVitoria(){
        int maiorValor = 0;
        for ( Jogador jogador : this.getJogadores() ){
            if (jogador.getPontos()<=21 && jogador.getPontos()>maiorValor)
                maiorValor = jogador.getPontos();
        }
        for ( Jogador jogador : this.getJogadores() ){
            jogador.addPartida();
            if (jogador.getPontos()==maiorValor) {
                System.out.println("VITORIA DE "+jogador.getNome()+" COM "+jogador.getPontos()+" PONTOS!");
                jogador.addVitoria();
            }
        }
    }
}
