package entidades;

public class player {
    private String nome;
    private String ip;

    public player(String nome) {
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }

    public String getIp() {
        return ip;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
}
