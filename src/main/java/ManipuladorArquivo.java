package main.java;

import java.io.*;
import java.util.ArrayList;
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
    public static void criarProperties() throws IOException {
        String arquivo = new String();
        String diretorio =  new String();
        arquivo = "..\\properties\\dados.properties";
        diretorio = "..\\properties";

        File file = new File(diretorio);

        List<String> lista = new ArrayList<String>();
        lista.add("PORTACLIENTE = 12345");
        lista.add("PORTASERVIDOR = 8888");
        lista.add("portaMulticast = 8888");
        lista.add("log = log");
        lista.add("Jogador = ..\\log\\logJogadoresServidor.txt");
        lista.add("Mesas = ..\\log\\logMesasServidor.txt");

        if(file.exists())
        {
            file = new File(diretorio);
            if(!file.exists()){
                file = new File(arquivo);
                file.createNewFile();

                FileWriter fw = new FileWriter(arquivo ,true);
                BufferedWriter bw = new BufferedWriter(fw);

                for(int i = 0;  i < lista.size(); i++)
                {
                    System.out.println(lista.get(i));

                    bw.write(lista.get(i));
                    bw.newLine();
                }

                bw.close();
            }
        }else
        {
            new File(diretorio).mkdir();
            new File(arquivo).createNewFile();

            FileWriter fw = new FileWriter(arquivo ,true);
            BufferedWriter bw = new BufferedWriter(fw);

            for(int i = 0;  i < lista.size(); i++)
            {
                bw.write(lista.get(i));
                bw.newLine();
            }

            bw.close();
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