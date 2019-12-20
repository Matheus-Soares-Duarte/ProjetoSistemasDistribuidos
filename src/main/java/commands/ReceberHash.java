package commands;

import io.atomix.copycat.Command;

public class ReceberHash implements Command<Object> {
    public String chaveHashServidor;

    public ReceberHash(String chaveHashServidor) {
        this.chaveHashServidor = chaveHashServidor;
    }

}