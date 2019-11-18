import java.io.*;
import java.util.Properties;

public class ManipuladorArquivo {
    public synchronized static void apagarMenores(String diretorio, int numero){
        File diretorioDeRecuperacao = new File(diretorio);

        System.out.println(diretorio);

        if(diretorioDeRecuperacao!=null) {
            File[] arquivos = diretorioDeRecuperacao.listFiles();
            if (arquivos!=null){
                for(int i=arquivos.length-1; i>=0; i--){
                    File arquivo = arquivos[i];
                    int numeroArquivo = Integer.parseInt(arquivo.getName().replace(".log", "").replace(".snapshot", ""));
//                    System.out.println(numero +" - "+numeroArquivo);
                    if(numeroArquivo<=numero) {
//                        System.out.println("DELETANDO "+arquivos[i].getName());
                        arquivos[i].delete();
                    }
                }
            }
        }
    }

    public static Properties arquivoConfiguracao(){
        final File diretorioProjeto = new File(System.getProperty("user.dir").replace("\\target", ""));
        final String diretorio =  diretorioProjeto+"\\properties";
        final String arquivo   = diretorio+"\\dados.properties";
        Properties properties = new Properties();
        File file;

        try {
            file = new File(diretorio);
            if(!file.exists())
            {
                file.mkdir();
            }

            file = new File(arquivo);
            if(!file.exists()){
                file.createNewFile();
                FileWriter fw = new FileWriter(arquivo);
                BufferedWriter bw = new BufferedWriter(fw);

                bw.write("Diretorio.Recuperacao = recuperacao\n");
                bw.write("Diretorio.RecuperacaoCliente = Cliente\n");
                bw.write("Diretorio.RecuperacaoServidor = Servidor\n");
                bw.write("Ip.Servidor = \n");
                bw.write("Porta.TCP = 12345\n");
                bw.write("Porta.Multicast = 8888\n");
                bw.write("Tempo.Snapshot = 30\n");
                bw.close();
                fw.close();
            }
            FileInputStream fileInputStream = new FileInputStream(arquivo);
            properties.load(fileInputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return properties;
    }

    public static int buscaUltimoNumero(String diretorio, String extensao){
        int numero=-1;
        File diretorioDeRecuperacao = new File(diretorio);
        if(diretorioDeRecuperacao!=null) {
            File[] arquivos = diretorioDeRecuperacao.listFiles();
            if (arquivos!=null && arquivos.length!=0){
                for(int i=0; i<arquivos.length; i++){
                    if(arquivos[i].getName().endsWith(extensao)){
                        int numeroLog = Integer.parseInt(arquivos[i].getName().replace(extensao, ""));
                        if(numeroLog > numero){
                            numero=numeroLog;
                        }
                    }
                }
            }
        }
        return numero;
    }

    public static int contadorAparicoes(String diretorio, String extensao){
        int numero=0;
        File diretorioDeRecuperacao = new File(diretorio);
        if(diretorioDeRecuperacao!=null) {
            File[] arquivos = diretorioDeRecuperacao.listFiles();
            if (arquivos!=null && arquivos.length!=0){
                for(int i=0; i<arquivos.length; i++){
                    if(arquivos[i].getName().endsWith(extensao)){
                        numero++;
                    }
                }
            }
        }
        return numero;
    }

    public static void criarArquivo(String diretorio, String nomeArquivo){
        String caminho = diretorio+"\\"+nomeArquivo;
        File file = new File(diretorio);
        if (file.exists()){
            file = new File(caminho);
            if (!file.exists()){
                try {
                    new FileOutputStream(caminho);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        } else {
            new File(diretorio).mkdir();
            try {
                new FileOutputStream(caminho);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public static void criarDiretorio(String diretorio){
        final File diretorioProjeto = new File(System.getProperty("user.dir").replace("\\target", ""));
        File file = new File(diretorioProjeto+"\\"+diretorio);
        if (!file.exists()){
            new File(diretorio).mkdir();
        }
    }

    public static void escreverObjetoNoArquivo(String caminho, Object objeto){
        try {
            FileOutputStream arquivo = new FileOutputStream(caminho, true);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(arquivo);
            objectOutputStream.writeObject(objeto);
            objectOutputStream.writeObject("\n");
            objectOutputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized static void escreverLog(Object objeto, String diretorio, String mensagem, boolean escreverSnapshot)  {
        int numero = ManipuladorArquivo.buscaUltimoNumero(diretorio, ".log");
        if(numero==-1){
            numero++;
            ManipuladorArquivo.criarArquivo(diretorio, numero+".log");
        }

        String caminho = diretorio+"\\"+numero+".log";
        ManipuladorArquivo.escreverObjetoNoArquivo(caminho, mensagem);

        if(escreverSnapshot) {
            ManipuladorArquivo.escreverSnapshot(objeto, diretorio, numero);
        }
    }

    public synchronized static void escreverSnapshot(Object objeto, String diretorio, int numero) {
        String nomeArquivo = numero+".snapshot";
        String caminho = diretorio+"\\"+nomeArquivo;
        ManipuladorArquivo.criarArquivo(diretorio, nomeArquivo);
        ManipuladorArquivo.escreverObjetoNoArquivo(caminho, objeto);
        int qtdLog = ManipuladorArquivo.contadorAparicoes(diretorio, ".log");
        int qtdSnapshot = ManipuladorArquivo.contadorAparicoes(diretorio, ".snapshot");
        if (qtdLog > 3 || qtdSnapshot > 3) {
            ManipuladorArquivo.apagarMenores(diretorio, numero - 3);
        }
        numero++;
        ManipuladorArquivo.criarArquivo(diretorio, numero+".log");
    }

    public static Object recuperarObjetoDoArquivo(String caminho) {
        File file = new File(caminho);
        if(file.length()>0) {
            try {
                FileInputStream arquivo = new FileInputStream(caminho);
                ObjectInputStream objectInputStream = new ObjectInputStream(arquivo);
                Object objeto = objectInputStream.readObject();
                arquivo.close();
                objectInputStream.close();
                return objeto;
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}