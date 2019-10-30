package main.java;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class PropertiesSalvos
{

    public Properties retornoProp() throws IOException {
        ManipuladorArquivo.criarProperties();
        Properties p = new Properties();
        String caminho_properties = "..\\properties\\dados.properties";
        FileInputStream file = new FileInputStream(caminho_properties);
        p.load(file);
        return p;
    }

}
