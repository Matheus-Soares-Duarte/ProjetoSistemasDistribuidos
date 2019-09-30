public class Dealer  implements Runnable {
    private Mesa mesa;

    public Dealer(Mesa mesa) {
        setMesa(mesa);
    }

    public Mesa getMesa() {
        return mesa;
    }

    public void setMesa(Mesa mesa) {
        this.mesa = mesa;
    }

    @Override
    public void run() {
        getMesa().iniciarJogo();
    }
}
