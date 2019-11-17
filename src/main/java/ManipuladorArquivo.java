import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class ManipuladorArquivo
{

    public static boolean gravarStringLog(String comando, String caminho, String diretorio)  {
        criarArquivo(diretorio,caminho);
        FileWriter fw = null;
        try {
            fw = new FileWriter(caminho,true);
            BufferedWriter bw = new BufferedWriter(fw);

            bw.write(comando);
            bw.newLine();

            bw.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

    }

    public static boolean gravarObjetoSnapShot(Object o, String caminho, String diretorio) throws IOException {
        criarArquivo(diretorio,caminho);
        FileOutputStream fos = null;
        ObjectOutputStream obj = null;
        try
        {
            fos = new FileOutputStream(caminho,true);
            obj = new ObjectOutputStream(fos);

            obj.writeObject(o);
            obj.flush();

            fos.close();
            obj.close();
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
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

                bw.write("Diretorio.Recuperacao = "+diretorioProjeto+"\\Recuperacao\n");
                bw.write("Diretorio.RecuperacaoCliente = "+diretorioProjeto+"\\Recuperacao\\Clientes\n");
                bw.write("Diretorio.RecuperacaoServidor = "+diretorioProjeto+"\\Recuperacao\\Servidor\n");
                bw.write("Ip.Servidor = \n");
                bw.write("Porta.TCP = 12345\n");
                bw.write("Porta.Multicast = 8888\n");
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

    public static void criarArquivo(String diretorio, String caminho){
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

    public static void escreverObjetoNoArquivo(String caminho, Object objeto){
        try {
            FileOutputStream arquivo = new FileOutputStream(caminho);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(arquivo);
            objectOutputStream.writeObject(objeto);
            objectOutputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}