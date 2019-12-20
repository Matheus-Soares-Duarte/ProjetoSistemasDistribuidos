package commands;

import io.atomix.copycat.Command;
import io.grpc.stub.StreamObserver;
import stubs.ComunicacaoOuterClass;

public class EntrarMesa implements Command<Object> {
    public String chaveHashMesa;
    public String ipJogador;
    public String nomeJogador;
    public int partidasJogador;
    public int vitoriasJogador;
//    public StreamObserver<ComunicacaoOuterClass.informacoesJogoResponse> responseObserver;

//    public EntrarMesa(String chaveHashMesa, String ipJogador, String nomeJogador, int partidasJogador, int vitoriasJogador, StreamObserver<ComunicacaoOuterClass.informacoesJogoResponse> responseObserver) {
    public EntrarMesa(String chaveHashMesa, String ipJogador, String nomeJogador, int partidasJogador, int vitoriasJogador) {
        this.chaveHashMesa = chaveHashMesa;
        this.ipJogador = ipJogador;
        this.nomeJogador = nomeJogador;
        this.partidasJogador = partidasJogador;
        this.vitoriasJogador = vitoriasJogador;
//        this.responseObserver = responseObserver;
    }

}
