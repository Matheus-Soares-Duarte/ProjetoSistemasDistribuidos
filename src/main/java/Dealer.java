import stubs.ComunicacaoOuterClass;

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
        while (true) {
            while (true) {
                if(this.getMesa().getJogadores().size() > 1){
                    break;
                }
                this.getMesa().dorme();
            }
            ComunicacaoOuterClass.informacoesJogoResponse.Builder resposta = ComunicacaoOuterClass.informacoesJogoResponse.newBuilder();

            resposta.setCodigo(0).setMensagem("\n--------------INICIANDO JOGO--------------").build();
            this.getMesa().enviarRespostaTodos(resposta.build());

            this.getMesa().reiniciarRodada();
            resposta.setCodigo(3).setMensagem("Comprar Cartas Iniciais").build();
            this.getMesa().enviarRespostaTodos(resposta.build());

            while (true) {
                int i = this.getMesa().buscaJogadorVez();
                if (i == this.getMesa().getJogadores().size()) {
                    break;
                }
                Jogador jogador = this.getMesa().getJogadores().get(i);

                resposta.setCodigo(0).setMensagem("Vez do Jogador " + jogador.getNome() + ".").build();
                this.getMesa().enviarRespostaTodos(resposta.build());

                if (!jogador.getEmReconexão()) {
                    resposta.setCodigo(2).setMensagem("Sua Vez").build();
                    this.getMesa().enviarResposta(resposta.build(), jogador);
                    this.getMesa().dorme();
                } else {
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            if(this.getMesa().getJogadores().size() == 0) {
                this.getMesa().getServidor().removeMesa(this.getMesa());
                break;
            }

            this.getMesa().verificarVitoria();
            this.getMesa().score();

            if(this.getMesa().getJogadores().size() == 1) {
                resposta.setCodigo(0).setMensagem("Erro:Não existem jogadores suficientes na mesa para iniciar a rodada.").build();
                this.getMesa().enviarResposta(resposta.build(), this.getMesa().getJogadores().get(0));
            }
        }
    }
}
