package commands;

import io.atomix.copycat.Command;

public class ComprarCarta implements Command<Object> {
    public String chaveHashMesa;
    public String ipJogador;
    public String nomeJogador;

    public String letra;
    public String naipe;
    public int valor;

    public ComprarCarta(String chaveHashMesa, String ipJogador, String nomeJogador, String letra, String naipe, int valor) {
        this.chaveHashMesa = chaveHashMesa;
        this.ipJogador     = ipJogador;
        this.nomeJogador   = nomeJogador;
        this.letra = letra;
        this.naipe = naipe;
        this.valor = valor;
    }

}
