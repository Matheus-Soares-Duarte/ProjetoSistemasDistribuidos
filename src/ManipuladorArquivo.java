import java.io.*;
import java.util.List;

public class ManipuladorArquivo {
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

    public static List<Jogador> leitorArquivoJogadores(String caminho) {
        File file = new File(caminho);
        if(file.length()>0) {
            try {
                FileInputStream arquivo = new FileInputStream(caminho);
                ObjectInputStream objeto = new ObjectInputStream(arquivo);
                List<Jogador> listaJogadores = (List<Jogador>) objeto.readObject();
                arquivo.close();
                objeto.close();
                return listaJogadores;
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
    public static List<Jogador> leitorArquivoMesas(String caminho) {
        return null;
    }

    public static void escritorLogJogadores(String caminho, List<Jogador> jogadores){
        try {
            FileOutputStream arquivo = new FileOutputStream(caminho);
            ObjectOutputStream objeto = new ObjectOutputStream(arquivo);
            objeto.writeObject(jogadores);
            objeto.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void escritorLogMesas(String caminho, List<Jogador> jogadores){
    }

}