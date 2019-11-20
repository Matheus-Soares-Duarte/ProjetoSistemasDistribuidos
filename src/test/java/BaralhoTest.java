import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BaralhoTest {

    @Test
    void criaBaralho() {

        Baralho baralho = new Baralho();
        assertEquals(baralho.getList().size(), 52);
        assertEquals(baralho.getList().get(0).getLetra(), "ÁS");
        assertEquals(baralho.getList().get(0).getNaipe(),"Paus");
        assertEquals(baralho.getList().get(12).getLetra(), "K");

        assertEquals(baralho.getList().get(13).getLetra(), "ÁS");
        assertEquals(baralho.getList().get(13).getNaipe(),"Copas");

        assertEquals(baralho.getList().get(25).getLetra(), "K");

        assertEquals(baralho.getList().get(26).getLetra(), "ÁS");
        assertEquals(baralho.getList().get(26).getNaipe(),"Espadas");

        assertEquals(baralho.getList().get(38).getLetra(), "K");

        assertEquals(baralho.getList().get(39).getLetra(), "ÁS");
        assertEquals(baralho.getList().get(39).getNaipe(),"Ouros");

        assertEquals(baralho.getList().get(51).getLetra(),"K");

    }

    @Test
    void embaralhar() {
        Baralho b1 = new Baralho();
        Baralho b2 = new Baralho();

        b2.embaralhar();

        int verdade = 0;
        if(b1 == b2){verdade = 1;}

        assertEquals(verdade,0);
    }

    @Test
    void entregarCarta() {
        Baralho baralho = new Baralho();
        Carta c1 = baralho.getList().get(baralho.getList().size() -1);
        Carta c2 = baralho.entregarCarta();

        assertEquals(c1,c2);
        assertEquals(baralho.getList().size(),51);
    }
}