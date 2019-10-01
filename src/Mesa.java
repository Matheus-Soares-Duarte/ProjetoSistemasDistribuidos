import java.util.ArrayList;
import java.util.List;

public class Mesa {
    private int id;
    private List<Jogador> jogadores = new ArrayList<Jogador>();
    private Baralho baralho = new Baralho();
    private ConexaoServidor servidor;

    public Mesa(int id, Jogador jogador, ConexaoServidor servidor){
        setId(id);
        setServidor(servidor);
        addJogador(jogador);
    }

    void setBaralho(Baralho baralho){ this.baralho = baralho; }
    void setServidor(ConexaoServidor servidor) {
        this.servidor = servidor;
    }
    void setId(int id){ this.id = id; }
    void setJogadores(List<Jogador> jogadores){ this.jogadores = jogadores; }

    Baralho getBaralho(){ return this.baralho; }
    ConexaoServidor getServidor() {
        return servidor;
    }
    int getId(){ return this.id; }
    List<Jogador> getJogadores(){ return this.jogadores; }

    synchronized void acorda(){
        notify();
    }

    void addJogador(Jogador jogador){
        Mensagem mensagem;
        if(this.getJogadores().size()<5){
            this.getJogadores().add(jogador);
            jogador.setMesa(this.getId());
            if(this.getJogadores().size() != 1){
                mensagem = new Mensagem("String", "O Jogador "+jogador.getNome()+" acaba de entrar na sala "+this.getId()+"!");
                this.enviarMensagemTodos(mensagem);
                if(this.getJogadores().size() == 2){
                    Dealer dealer = new Dealer(this);
                    new Thread(dealer).start();
                } else if(this.getJogadores().size()>2) {
                    for (int i=0; i<2; i++) {
                        this.comprarCarta(jogador,"CartaInicial");
                    }
                }
            }
        } else {
            mensagem = new Mensagem("String", "Erro:Inicial:A sala "+this.getId()+" ja está cheia!");
            this.getServidor().enviaMesagem(mensagem, jogador.getOut());
        }
    }

    synchronized void dorme(){
        try {
            this.wait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    void enviarMensagemTodos(Mensagem mensagem){
        for (Jogador jogador : this.getJogadores() ){
            getServidor().enviaMesagem(mensagem, jogador.getOut());
        }
    }

    void reiniciarRodada(){
        Mensagem mensagem = new Mensagem("String", "ReiniciarRodada");
        this.enviarMensagemTodos(mensagem);
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
        Mensagem mensagem = new Mensagem("String", "--------SCORE--------");
        this.enviarMensagemTodos(mensagem);
        for ( Jogador jogador : this.getJogadores() ){
            mensagem = new Mensagem("String", "-Jogador nº "+i+" ("+jogador.getNome()+"): "+jogador.getVitorias()+" Vitorias em "+jogador.getPartidas()+" Partidas.");
            this.enviarMensagemTodos(mensagem);
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
                Mensagem mensagem = new Mensagem("String", "Vitoria");
                this.getServidor().enviaMesagem(mensagem, jogador.getOut());
                mensagem = new Mensagem("String", "VITORIA DE "+jogador.getNome()+" COM "+jogador.getPontos()+" PONTOS!");
                this.enviarMensagemTodos(mensagem);
                jogador.addVitoria();
            }
        }
    }

    void comprarCarta(Jogador jogador, String string){
        Carta carta = this.getBaralho().entregarCarta();
        Mensagem mensagem = new Mensagem(string, carta);
        this.getServidor().enviaMesagem(mensagem, jogador.getOut());
        jogador.comprarCarta(carta);
    }
}
