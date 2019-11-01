package main.java;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

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
    public static Properties arquivoConfiguracao(){
        final String arquivo = "properties\\dados.properties";
        final String diretorio =  "properties";

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

                bw.write("Porta.TCP = 12345\n");
                bw.write("Porta.Multicast = 8888\n");
                bw.write("Diretorio.Recuperacao = ..\\Recuperacao\n");
                bw.write("Diretorio.RecuperacaoCliente = ..\\Recuperacao\\Clientes\n");
                bw.write("Diretorio.RecuperacaoServidor = ..\\Recuperacao\\Servidor\n");
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

    public static List<Mesa> leitorArquivoMesas(String caminho) {
        File file = new File(caminho);
        if(file.length()>0) {
            try {
                FileInputStream arquivo = new FileInputStream(caminho);
                ObjectInputStream objeto = new ObjectInputStream(arquivo);
                List<Mesa> listaMesas = (List<Mesa>) objeto.readObject();
                arquivo.close();
                objeto.close();
                return listaMesas;
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
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

    public static void escritorLogMesas(String caminho, List<Mesa> mesas){
        try {
            FileOutputStream arquivo = new FileOutputStream(caminho);
            ObjectOutputStream objeto = new ObjectOutputStream(arquivo);
            objeto.writeObject(mesas);
            objeto.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}