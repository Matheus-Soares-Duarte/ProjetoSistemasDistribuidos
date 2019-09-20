import java.util.ArrayList;
import java.util.List;

public class Mesa {
    private int id;
    private List<Jogador> jogadores = new ArrayList<Jogador>();
    private Baralho baralho = new Baralho();

    public Mesa(int id, Jogador jogador){
        setId(id);
        setJogador(jogador);
    }

    void setId(int id){ this.id = id; }
    void setJogador(Jogador jogador){ this.jogadores.add(jogador); }

    void iniciarJogo(){
        System.out.println("\n--------------INICIANDO JOGO--------------");

        //while(jogadores.size()>1){
        if(jogadores.size()>1) {
            for (int r = 0; r < 3; r++) {
                System.out.println("\n--------INICIANDO RODADA--------");
                this.reiniciarRodada();

                for (Jogador jogador : jogadores) {
                    for (int i = 0; i < 2; i++) {
                        jogador.setCarta(baralho.entregarCarta());
                        jogador.setPontos(jogador.getPontos() + jogador.getCarta().getValor());
                        if (jogador.getCarta().getValor() == 1) {
                            jogador.setAs(true);
                        }
                    }
                    jogador.mostrarCartas();
                }
                this.verificarVitoria();
                this.score();
            }
        }

        if(jogadores.size()<2){
            System.out.println("Não existem jogadores suficientes na mesa para iniciar a rodada.");
        }
    }

    void reiniciarRodada(){
        for (Jogador jogador : jogadores){
            for (Carta carta : jogador.getCartas()) {
                baralho.addCarta(carta);
            }
            jogador.devolverCartas();
        }
        baralho.embaralhar();
    }

    void score(){
        int i=1;
        System.out.println("--------SCORE--------");
        for (Jogador jogador : jogadores){
            System.out.println("-Jogador nº "+i+" ("+jogador.getNome()+"): "+jogador.getVitorias()+" Vitorias.");
            i++;
        }
    }

    void verificarVitoria(){
        int maiorValor = 0;
        for (Jogador jogador : jogadores){
            if (jogador.getPontos()>maiorValor)
                maiorValor = jogador.getPontos();
        }
        for (Jogador jogador : jogadores){
            if (jogador.getPontos()==maiorValor)
                jogador.addVitoria();
        }
    }

}
