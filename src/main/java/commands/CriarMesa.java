package commands;

import io.atomix.copycat.Command;

public class CriarMesa implements Command<Object> {
    public String chaveHashMesa;

    public CriarMesa(String chaveHashMesa) {
        this.chaveHashMesa = chaveHashMesa;
    }

}