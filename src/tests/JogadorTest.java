package tests;

import main.Carta;
import main.Jogador;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JogadorTest {
    Jogador jogador = new Jogador();
    @Test
    void addVitoria() {
        int esperado = 1;
        this.jogador.setVitorias(0);
        this.jogador.addVitoria();
        int real = this.jogador.getVitorias();

        assertEquals(esperado,real);
    }

    @Test
    void addPartida() {
        int esperado = 1;
        this.jogador.setPartidas(0);
        this.jogador.addPartida();
        int real = this.jogador.getPartidas();

        assertEquals(esperado,real);
    }

    @Test
    void addCarta() {
        String esperado = "Ás de Copas";
        Carta carta = new Carta("Ás","Copas",1);
        String real = carta.getLetra() +" de "+ carta.getNaipe();
        int esperado1 = 1;
        this.jogador.addCarta(carta);
        int real1 = this.jogador.getCartas().size();

        assertEquals(esperado,real);
        assertEquals(esperado1,real1);
    }

    @Test
    void comprarCarta() {
        Carta carta = new Carta("Ás","Copas",1);
        this.jogador.comprarCarta(carta);
        int valorEsperado = 11;
        int valorReal = this.jogador.getPontos();

        assertEquals(valorEsperado,valorReal);
    }

    @Test
    void devolverCartas() {
        this.jogador.devolverCartas();
        int tamanhoEsperado = 0;
        int pontosEsperados = 0;
        boolean jogouEsperado = false;
        boolean AsEsperado = false;
        int tamanhoReal = this.jogador.getCartas().size();
        int pontosReal = this.jogador.getPontos();
        boolean jogouReal = this.jogador.getJogou();
        boolean AsReal = this.jogador.getAs();

        assertEquals(tamanhoEsperado,tamanhoReal);
        assertEquals(jogouEsperado,jogouReal);
        assertEquals(pontosEsperados,pontosReal);
        assertEquals(AsEsperado,AsReal);

    }

}