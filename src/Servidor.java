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
            conexao.setJogadores(jogadores);
            for(Jogador jogador: jogadores){
                System.out.println("Jogador "+jogador.getNome()+" socket "+jogador.getSocketServidor());
                TrataCliente tc = new TrataCliente(jogador.getSocketCliente(), conexao);
                new Thread(tc).start();
            }
            List<Mesa> mesas = ManipuladorArquivo.leitorArquivoMesas(logMesasServidor);
            file = new File(logMesasServidor);
            if(file.length()>0) {
                conexao.setMesas(mesas);
                for(Mesa mesa: mesas){
                    Dealer dealer = new Dealer(mesa);
                    new Thread(dealer).start();
                }
            }
            System.out.println("ESTADO DO SERVIDOR RECUPERADO!");
        }

        // inicia o servidor
        new Thread(() -> { conexao.espalharServidor(); }).start();
        conexao.executa();
    }
}
