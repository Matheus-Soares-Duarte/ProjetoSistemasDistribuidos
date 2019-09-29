public class Mensagem {
    private String tipo;
    private Object objeto;

    public Mensagem(String tipo, Object objeto){
        setTipo(tipo);
        setObjeto(objeto);
    }

    void setTipo(String tipo){ this.tipo = tipo; }
    void setObjeto(Object objeto){ this.objeto = objeto; }

    String getTipo(){ return this.tipo; }
    Object getObjeto(){ return this.objeto; }
}
