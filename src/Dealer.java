public class Dealer  implements Runnable {
    private Mesa mesa;

    public Dealer(Mesa mesa) {
        setMesa(mesa);
    }

    public Mesa getMesa() {
        return mesa;
    }

    public void setMesa(Mesa mesa) {
        this.mesa = mesa;
    }

    @Override
    public void run() {
        Mensagem mensagem;
        mensagem = new Mensagem("String", "\n--------------INICIANDO JOGO--------------");
        this.getMesa().enviarMensagemTodos(mensagem);

        while(this.getMesa().getJogadores().size()>1){
            mensagem = new Mensagem("String", "\n--------INICIANDO RODADA--------");
            this.getMesa().enviarMensagemTodos(mensagem);

            this.getMesa().reiniciarRodada();
            for ( int i=0; i<getMesa().getJogadores().size(); i++ ) {
                Jogador jogador=this.getMesa().getJogadores().get(i);
                for (int j=0; j<2; j++) {
                    this.getMesa().comprarCarta(jogador,"CartaInicial");
                }
            }

            for ( int i=0; i<getMesa().getJogadores().size(); i++ ) {
                Jogador jogador=this.getMesa().getJogadores().get(i);
                mensagem = new Mensagem("String", "Vez do Jogador "+jogador.getNome()+".");
                this.getMesa().enviarMensagemTodos(mensagem);
                mensagem = new Mensagem("String", "SuaVez");
                this.getMesa().getServidor().enviaMesagem(mensagem, jogador.getOut());
                this.getMesa().dorme();
            }
            this.getMesa().verificarVitoria();
            this.getMesa().score();
        }

        if(this.getMesa().getJogadores().size()<2){
            mensagem = new Mensagem("String", "Erro:Não existem jogadores suficientes na mesa para iniciar a rodada.");
            this.getMesa().getServidor().enviaMesagem(mensagem, this.getMesa().getJogadores().get(0).getOut());
        }
    }
}
