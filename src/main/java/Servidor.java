package main.java;

import java.io.IOException;

public class Servidor {
    public static void main(String[] args) throws IOException {
        final int PORTA = 12345;
        ConexaoServidor conexao = new ConexaoServidor(PORTA);

        // inicia o servidor
        new Thread(() -> { conexao.espalharServidor(); }).start();
        conexao.executa();
    }
}
