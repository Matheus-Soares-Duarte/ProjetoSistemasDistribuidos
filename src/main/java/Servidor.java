import io.grpc.Server;
import io.grpc.ServerBuilder;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

public class Servidor {
    public static void main(String[] args) {
        ConexaoServidor conexao   = null;
        Properties properties     = ManipuladorArquivo.arquivoConfiguracao();
        ArrayList<String> ArrayList_comandos_log = null;
        ArrayList<String[]> conteudo_comandos_log = new ArrayList();

        String diretorio              = properties.getProperty("Diretorio.Recuperacao");
        String diretorioServidor      = properties.getProperty("Diretorio.RecuperacaoServidor");
        String extensaoLog            = properties.getProperty("extensaoLog");
        String extensaoSnapShot       = properties.getProperty("extensaoSnapShot");


        Integer  ultimoNumeroSnapShot = ManipuladorArquivo.buscaUltimoNumero(diretorio +"\\"+diretorioServidor,extensaoSnapShot);
        Integer  ultimoNumeroLog      = ManipuladorArquivo.buscaUltimoNumero(diretorio +"\\"+diretorioServidor,extensaoLog);

        if(ultimoNumeroSnapShot >= 0)
        {
            conexao = (ConexaoServidor) ManipuladorArquivo.recuperarObjetoDoArquivo(diretorio+"\\"+diretorioServidor+"\\"+ultimoNumeroSnapShot + extensaoSnapShot);

        }else
        {
            conexao = new ConexaoServidor();
        }
        if(ultimoNumeroLog >= 0)
        {
            //Dentro do for realizar ações do log
            for(int i = ultimoNumeroSnapShot + 1 ; i <= ultimoNumeroLog ; i++)
            {
                //Ler todas as linhas do log e salva na variavel ArrayList_comandos_log
                ArrayList_comandos_log = ManipuladorArquivo.recuperarStringDoArquivo(diretorio + "\\" + diretorioServidor + "\\" + i + extensaoLog);

                if(ArrayList_comandos_log == null)
                {
                    System.out.println("Arquivo de log em branco!");;
                }else
                {
                    for (int contadorLinhasComandos = 0; contadorLinhasComandos < ArrayList_comandos_log.size(); contadorLinhasComandos++)
                    {
                        //Separa os comandos por : de cada linha e salva como uma lista de String
                        conteudo_comandos_log.add(ArrayList_comandos_log.get(contadorLinhasComandos).split(":"));
                        for(int contadorComandos = 0; contadorComandos < conteudo_comandos_log.get(contadorLinhasComandos).length; contadorComandos++)
                        {

                            switch (conteudo_comandos_log.get(contadorLinhasComandos)[contadorComandos])
                            {
                                case "criarMesa":
                                    //hashServidor   comando   HashMesa  IP nome

                                    String chaveHashMesa    = conteudo_comandos_log.get(contadorLinhasComandos)[2];
                                    String IpJogador        = conteudo_comandos_log.get(contadorLinhasComandos)[3];
                                    String NomeJogador      = conteudo_comandos_log.get(contadorLinhasComandos)[4];

                                    conexao.reconexaoCriarMesa(conexao,chaveHashMesa,IpJogador,NomeJogador);
                                    break;
                                case "comprarCarta":
                                    //hashServidor   comando   HashMesa  IP nome
                                    //comando = "comprarCarta"+":"+carta.getLetra()+":"+carta.getNaipe()+":"+carta.getValor()

                                    String cartaLetra       = conteudo_comandos_log.get(contadorLinhasComandos)[2];
                                    String cartaNaipe       = conteudo_comandos_log.get(contadorLinhasComandos)[3];
                                    int cartaValor          = Integer.parseInt(conteudo_comandos_log.get(contadorLinhasComandos)[4]);
                                    chaveHashMesa           = conteudo_comandos_log.get(contadorLinhasComandos)[5];
                                    IpJogador               = conteudo_comandos_log.get(contadorLinhasComandos)[6];
                                    NomeJogador             = conteudo_comandos_log.get(contadorLinhasComandos)[7];

                                    conexao.reconexaoComprarCarta(cartaLetra, cartaNaipe, cartaValor,chaveHashMesa, IpJogador, NomeJogador, conexao);

                                    break;
                                case "entrarMesa":

                                    int NumeroPartidas     = Integer.parseInt(conteudo_comandos_log.get(contadorLinhasComandos)[2]);
                                    int NumeroVitorias     = Integer.parseInt(conteudo_comandos_log.get(contadorLinhasComandos)[3]);
                                    chaveHashMesa          = conteudo_comandos_log.get(contadorLinhasComandos)[4];
                                    IpJogador              = conteudo_comandos_log.get(contadorLinhasComandos)[5];
                                    NomeJogador            =  conteudo_comandos_log.get(contadorLinhasComandos)[6];

                                    conexao.reconexaoEntrarMesa(chaveHashMesa,IpJogador,NomeJogador, NumeroPartidas,NumeroVitorias, conexao);
                                    break;
                                case "passarVez":
                                    //hashServidor   comando   HashMesa  IP nome

                                    chaveHashMesa           = conteudo_comandos_log.get(contadorLinhasComandos)[2];
                                    IpJogador               = conteudo_comandos_log.get(contadorLinhasComandos)[3];
                                    NomeJogador             = conteudo_comandos_log.get(contadorLinhasComandos)[4];

                                    conexao.reconexaoPassarVez(chaveHashMesa,IpJogador, NomeJogador,conexao);
                                    break;
                                case "sairMesa":
                                    //hashServidor   comando   HashMesa  IP nome

                                    chaveHashMesa           = conteudo_comandos_log.get(contadorLinhasComandos)[2];
                                    IpJogador               = conteudo_comandos_log.get(contadorLinhasComandos)[3];
                                    NomeJogador             = conteudo_comandos_log.get(contadorLinhasComandos)[4];

                                    conexao.reconexaoSairMesa(chaveHashMesa,IpJogador, NomeJogador,conexao);
                                    break;
                            }
                        }

                    }
                }

            }
        }

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
