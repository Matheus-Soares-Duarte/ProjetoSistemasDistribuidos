import io.grpc.stub.StreamObserver;
import stubs.ComunicacaoGrpc;
import stubs.ComunicacaoOuterClass;

import java.io.Serializable;
import java.net.*;
import java.util.*;

public class ConexaoServidor extends ComunicacaoGrpc.ComunicacaoImplBase implements Serializable {
    private String chaveHash;
    private int portaTCP;
    private static List<Mesa> mesas;

    public ConexaoServidor (int porta) {
        try {
            this.setChaveHash( ChaveHash.gerarChave(IpCorreto.getIpCorreto()+" "+porta) );
        } catch (SocketException e) {
            e.printStackTrace();
        }
        this.setPortaTCP( porta );
        this.setMesas( new ArrayList<Mesa>() );
    }

    String getChaveHash() { return this.chaveHash; }
    synchronized List<Mesa> getMesas(){ return this.mesas; }
    int getPortaTCP() { return portaTCP; }

    public void setChaveHash(String chaveHash){ this.chaveHash = chaveHash; }
    public void setMesas(List<Mesa> mesas){ this.mesas = mesas; }
    public void setPortaTCP(int portaTCP) { this.portaTCP = portaTCP; }

    int buscaMesa(String chaveHashMesa){
        for (Mesa mesa : this.getMesas()){
            if( mesa.getChaveHash().equals(chaveHashMesa) ){
                return this.getMesas().indexOf(mesa);
            }
        }
        return -1;
    }

    void removeMesa(Mesa mesa) {
        if (mesa != null) {
            this.getMesas().remove(mesa);
            System.out.println("EXCLUSÃO DE SALA: A Sala " + mesa.getChaveHash() + " acaba de ser excluida, pois não existem jogadores nela! Existe(em) " + this.getMesas().size() + " Sala(s) neste Servidor.");
        }
    }

    @Override
    public void criarMesa(ComunicacaoOuterClass.criarMesaRequest request, StreamObserver<ComunicacaoOuterClass.criarMesaResponse> responseObserver) {
        String chaveHashMesa = ChaveHash.gerarChave(request.getIp()+" "+request.getNome());
        Mesa mesa = new Mesa(chaveHashMesa, this);
        this.getMesas().add(mesa);
        System.out.println("CRIAÇÃO DE SALA: Sucesso ao tentar criar a sala "+chaveHashMesa+"! Existe(em) "+this.getMesas().size()+" Sala(s) aberta(s) neste Servidor.");

        ComunicacaoOuterClass.criarMesaResponse.Builder resposta = ComunicacaoOuterClass.criarMesaResponse.newBuilder();
        String msg = "A sala de numero "+chaveHashMesa+" foi criada com sucesso!";
        resposta.setCodigo(0).setMensagem(msg).setChaveHashMesa(chaveHashMesa);
        responseObserver.onNext(resposta.build());

        responseObserver.onCompleted();
    }

    @Override
    public void entrarMesa(ComunicacaoOuterClass.entrarMesaRequest request, StreamObserver<ComunicacaoOuterClass.informacoesJogoResponse> responseObserver) {
        String chaveHashMesa = request.getChaveHashMesa();
        Jogador jogador = new Jogador();
        jogador.setIp( request.getIp() );
        jogador.setNome( request.getNome() );
        jogador.setPartidas( request.getPartidas() );
        jogador.setVitorias( request.getVitorias() );
        jogador.setResponseObserver(responseObserver);

        int index = buscaMesa( chaveHashMesa );
        ComunicacaoOuterClass.informacoesJogoResponse.Builder resposta = ComunicacaoOuterClass.informacoesJogoResponse.newBuilder();
        if(index>=0){
            if( this.getMesas().get(index).addJogador(jogador) ) {
                System.out.println("ENTRADA NA SALA: O Jogador " + jogador.getNome() + " acaba de entrar na sala " + chaveHashMesa + "! " +
                        "Existe(em) " + this.getMesas().get(index).getJogadores().size() + " Jogador(es) nessa Sala.");
            } else {
                System.err.println("ENTRADA NA SALA: Erro na tentativa do Jogador " + jogador.getNome() + " entrar na sala " + chaveHashMesa + "! " +
                        "Pois ja existe(em) " + this.getMesas().get(index).getJogadores().size() + " Jogador(es) nessa Sala.");
                resposta.setCodigo(-1).setMensagem("Erro: Sala com quantidade maxima de jogadores.");
                responseObserver.onNext(resposta.build());
            }
        } else {
            System.err.println("ENTRADA NA SALA: Erro na tentativa do Jogador "+jogador.getNome()+" entrar na sala "+chaveHashMesa+"! " +
                    "Pois essa Sala não existe.");
            resposta.setCodigo(-2).setMensagem("Erro: A Sala "+chaveHashMesa+" não existe!");
            responseObserver.onNext(resposta.build());
        }
    }

    @Override
    public void comprarCarta(ComunicacaoOuterClass.requisicaoNaVezRequest request, StreamObserver<ComunicacaoOuterClass.comprarCartaResponse> responseObserver) {
        String chaveHashMesa = request.getChaveHashMesa();
        int indexMesa = buscaMesa( chaveHashMesa );
        ComunicacaoOuterClass.comprarCartaResponse.Builder comprarCartaResponse = ComunicacaoOuterClass.comprarCartaResponse.newBuilder();

        if(indexMesa>=0){
            Mesa mesa = this.getMesas().get(indexMesa);
            Jogador jogador = mesa.buscaJogador(request.getIp(), request.getNome());
            if(jogador!=null){
                Carta carta = mesa.getBaralho().entregarCarta();
                jogador.comprarCarta(carta);
                comprarCartaResponse
                        .setCodigo(0)
                        .setLetra(carta.getLetra())
                        .setNaipe(carta.getNaipe())
                        .setValor(carta.getValor())
                        .build();
            } else {
                comprarCartaResponse
                        .setCodigo(1)
                        .setLetra("")
                        .setNaipe("")
                        .setValor(0)
                        .build();
            }
        }
        responseObserver.onNext(comprarCartaResponse.build());
        responseObserver.onCompleted();
    }

    @Override
    public void passarVez(ComunicacaoOuterClass.requisicaoNaVezRequest request, StreamObserver<ComunicacaoOuterClass.passarVezResponse> responseObserver) {
        String chaveHashMesa = request.getChaveHashMesa();
        int indexMesa = buscaMesa( chaveHashMesa );
        ComunicacaoOuterClass.passarVezResponse.Builder passarVezResponse = ComunicacaoOuterClass.passarVezResponse.newBuilder();

        if(indexMesa>=0){
            Mesa mesa = this.getMesas().get(indexMesa);
            Jogador jogador = mesa.buscaJogador(request.getIp(), request.getNome());
            if(jogador!=null){
                jogador.setJogou(true);
                mesa.acorda();
                passarVezResponse
                        .setCodigo(0)
                        .setMensagem("Sucesso ao passar a vez.")
                        .build();
            } else {
                passarVezResponse
                        .setCodigo(1)
                        .setMensagem("Erro ao encontrar seu jogador.")
                        .build();
            }
        } else {
            passarVezResponse
                    .setCodigo(2)
                    .setMensagem("Erro ao encontrar a Sala "+chaveHashMesa+".")
                    .build();
        }
        responseObserver.onNext(passarVezResponse.build());
        responseObserver.onCompleted();
    }

    @Override
    public void sairMesa(ComunicacaoOuterClass.requisicaoNaVezRequest request, StreamObserver<ComunicacaoOuterClass.sairMesaResponse> responseObserver) {
        String chaveHashMesa = request.getChaveHashMesa();
        int indexMesa = buscaMesa( chaveHashMesa );
        ComunicacaoOuterClass.sairMesaResponse.Builder sairMesaResponse = ComunicacaoOuterClass.sairMesaResponse.newBuilder();

        if(indexMesa>=0){
            Mesa mesa = this.getMesas().get(indexMesa);
            Jogador jogador = mesa.buscaJogador(request.getIp(), request.getNome());
            if(jogador!=null && mesa.retirarJogador(jogador)){
                System.err.println("SAIDA DA SALA: " + jogador.getNome() + " saiu da sala " + mesa.getChaveHash() + "! Existe(em) " + mesa.getJogadores().size() + " Jogador(es) nessa Sala.");
                sairMesaResponse
                        .setCodigo(0)
                        .setMensagem("Sucesso ao sair da mesa.")
                        .build();
            } else {
                sairMesaResponse
                        .setCodigo(1)
                        .setMensagem("Erro ao encontrar seu jogador.")
                        .build();
            }
        } else {
            sairMesaResponse
                    .setCodigo(2)
                    .setMensagem("Erro ao encontrar a Sala "+chaveHashMesa+".")
                    .build();
        }
        responseObserver.onNext(sairMesaResponse.build());
        responseObserver.onCompleted();
    }
}