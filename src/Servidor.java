import java.io.File;
import java.io.IOException;
import java.util.List;

public class Servidor {
    public static void main(String[] args) throws IOException {
        String diretorio = "log";
        String logJogadoresServidor = "log\\logJogadoresServidor.txt";
        String logMesasServidor = "log\\logMesasServidor.txt";

        final int PORTA = 12345;
        ConexaoServidor conexao = new ConexaoServidor(PORTA);

//        ManipuladorArquivo.criarArquivo( diretorio, logJogadoresServidor );
//        ManipuladorArquivo.criarArquivo( diretorio, logMesasServidor );
//
//        List<Jogador> jogadores = ManipuladorArquivo.leitorArquivoJogadores(logJogadoresServidor);
//        File file = new File(logJogadoresServidor);
//        if(file.length()>0){
//            conexao.setJogadores(jogadores);
//            for(Jogador jogador: jogadores){
//
//            }
//            List<Mesa> mesas = ManipuladorArquivo.leitorArquivoMesas(logMesasServidor);
//            file = new File(logMesasServidor);
//            if(file.length()>0) {
//                conexao.setMesas(mesas);
//            }
//        }

        // inicia o servidor
        new Thread(() -> { conexao.espalharServidor(); }).start();
        conexao.executa();
    }
}
