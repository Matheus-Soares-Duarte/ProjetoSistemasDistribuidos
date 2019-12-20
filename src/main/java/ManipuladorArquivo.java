import java.io.*;
import java.util.Properties;

public class ManipuladorArquivo {

    public static Properties arquivoConfiguracao(){
        final char barraSO = IpCorreto.getBarraSistemaOperacional();
        final File diretorioProjeto = new File(System.getProperty("user.dir").replace(barraSO+"target", ""));
        final String diretorio =  diretorioProjeto.toString()+barraSO+"properties";
        final String arquivo   = diretorio+barraSO+"dados.properties";
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

                bw.write("Ips.Servidores.Cluster = 127.0.0.1 127.0.0.1 127.0.0.1\n");
                bw.write("Portas.Servidores.Cluster = 6000 6001 6002\n");
                bw.write("Ip.Servidor = \n");
                bw.write("Porta.Servidor = 12345\n");
                bw.write("Id.Cluster = 0\n");
                bw.write("Porta.TCP = 12345\n");
                bw.write("Quantidade.BytesHash = 16\n");

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

}
