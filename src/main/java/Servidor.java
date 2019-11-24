import io.grpc.Server;
import io.grpc.ServerBuilder;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

public class Servidor {
    public static void main(String[] args) {
        ConexaoServidor conexao;
        Properties properties     = ManipuladorArquivo.arquivoConfiguracao();
        ArrayList<String> ArrayList_comandos_log = null;

        String diretorio              = properties.getProperty("Diretorio.Recuperacao");
        String diretorioServidor      = properties.getProperty("Diretorio.RecuperacaoServidor");
        String extensaoLog            = properties.getProperty("extensaoLog");
        String extensaoSnapShot       = properties.getProperty("extensaoSnapShot");

        Integer  ultimoNumeroSnapShot = ManipuladorArquivo.buscaUltimoNumero(diretorio +"\\"+diretorioServidor,extensaoSnapShot);
        Integer  ultimoNumeroLog      = ManipuladorArquivo.buscaUltimoNumero(diretorio +"\\"+diretorioServidor,extensaoLog);
//        System.out.println("N° SnapShot: " + ultimoNumeroSnapShot);
//        System.out.println("N° Log: " + ultimoNumeroLog);
        if(ultimoNumeroSnapShot >= 0) {
            conexao = (ConexaoServidor) ManipuladorArquivo.recuperarObjetoDoArquivo(diretorio+"\\"+diretorioServidor+"\\"+ultimoNumeroSnapShot + extensaoSnapShot);
        } else {
            conexao = new ConexaoServidor();
        }
//        if(ultimoNumeroLog >= 0)
//        {
//          //Dentro do for realizar ações do log
//            for(int i = ultimoNumeroSnapShot + 1 ; i <= ultimoNumeroLog ; i++)
//            {
//                //ler linha por linha executar no servidor
//                ArrayList_comandos_log = ManipuladorArquivo.recuperarStringDoArquivo(diretorio + "\\" + diretorioServidor + "\\" + i + extensaoLog);
//                for (int j = 0; j < ArrayList_comandos_log.size(); j++){
//                    System.out.println(ArrayList_comandos_log.get(j));
//                }
//            }
//
//        }

        try {
            Server servidor = ServerBuilder.forPort(conexao.getPorta()).addService(conexao).build();
            servidor.start();
            System.out.println("Servidor iniciado no IP "+conexao.getIp()+" e na porta "+conexao.getPorta()+"." +
                    " Chave Hash do Servidor = "+conexao.getChaveHash()+" .");
            conexao.solicitarEntradaNaRede();
            servidor.awaitTermination();
        } catch (IOException e) {
            System.err.println("ERRO ao tentar iniciar o Servidor na porta "+conexao.getPorta()+".");
            System.err.println(e);
        } catch (InterruptedException e) {
            System.err.println("ERRO ao tentar iniciar o Servidor na porta "+conexao.getPorta()+".");
            System.err.println(e);
        }
    }
}
