package main;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class Servidor {
    public static void main(String[] args) throws IOException {
        String diretorio = "log";
        String logJogadoresServidor = "log\\logJogadoresServidor.txt";
        String logMesasServidor = "log\\logMesasServidor.txt";
        final int PORTA = 12345;

        ManipuladorArquivo.criarArquivo( diretorio, logJogadoresServidor );
        ManipuladorArquivo.criarArquivo( diretorio, logMesasServidor );

        ConexaoServidor conexao = new ConexaoServidor(PORTA);

        List<Jogador> jogadores = ManipuladorArquivo.leitorArquivoJogadores(logJogadoresServidor);
        File file = new File(logJogadoresServidor);
        if(file.length()>0){
            System.out.println("RECUPERANDO ESTADO DO SERVIDOR.");
            for(Jogador jogador: jogadores){
                System.out.println("Jogador "+jogador.getNome()+" socket "+jogador.getSocketServidor());
                jogador.setEmReconexão(true);
            }
            conexao.setJogadores(jogadores);
            List<Mesa> mesas = ManipuladorArquivo.leitorArquivoMesas(logMesasServidor);
            file = new File(logMesasServidor);
            if(file.length()>0) {
                conexao.setMesas(mesas);
                for(Mesa mesa: mesas){
                    Dealer dealer = new Dealer(mesa);
                    new Thread(dealer).start();
                }
            }
            try {
                System.out.println("Esperando por reconexões.");
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            for(int i=0; i<conexao.getJogadores().size(); i++){
                Jogador jogador = conexao.getJogadores().get(i);
                if(jogador.getEmReconexão()){
                    conexao.getJogadores().remove(i);
                    i--;
                }
            }
            System.out.println("ESTADO DO SERVIDOR RECUPERADO!");
        }

        // inicia o servidor
        new Thread(() -> { conexao.espalharServidor(); }).start();
        conexao.executa();
    }
}
