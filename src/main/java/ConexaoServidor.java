import commands.ComprarCarta;
import commands.CriarMesa;
import commands.EntrarMesa;
import io.atomix.catalyst.transport.Address;
import io.atomix.copycat.client.CopycatClient;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;
import stubs.ComunicacaoGrpc;
import stubs.ComunicacaoOuterClass;

import java.io.Serializable;
import java.math.BigInteger;
import java.net.SocketException;
import java.util.*;
import java.util.concurrent.CompletableFuture;

import static java.lang.Thread.sleep;

public class ConexaoServidor extends ComunicacaoGrpc.ComunicacaoImplBase implements Serializable {

    private static int bytesHash;
    private static String chaveHash;
    private static CopycatClient clienteCluster;
    private static FuncoesAtomix funcoesAtomix;
    private static String ip;
    private static String ipServidorEntrada;
    private static BigInteger maiorNumeroServidor;
    private static int portaServidorEntrada;

    private int idCluster;
    private String[] ipsCluster;
    private String[] portasCluster;
    private int porta;

    private static List<Address> addressesCluster = new LinkedList<>();
    private static List<Finger> fingerTable = null;
    private static List<Mesa> mesas = new ArrayList<>();

    public ConexaoServidor () {
        try {
            this.setIp(IpCorreto.getIpCorreto());
        } catch (SocketException e) {
            e.printStackTrace();
        }

        Properties properties = ManipuladorArquivo.arquivoConfiguracao();

        this.setPorta( Integer.parseInt(properties.getProperty("Porta.TCP")) );
        System.out.println("CONFIGURAÇÃO: Iniciando Servidor no Ip "+getIp()+" e Porta "+getPorta()+".");

        this.setIpServidorEntrada( properties.getProperty("Ip.Servidor") );
        this.setPortaServidorEntrada( Integer.parseInt(properties.getProperty("Porta.Servidor")) );

        int bytes = Integer.parseInt(properties.getProperty("Quantidade.BytesHash"));
        if(bytes>16){
            System.err.println("CONFIGURAÇÃO: Maior valor possivel de Bytes para Hash é de 16." +
                    " Usando Valor Maximo de 16 Bytes para criação de Chaves Hash.");
            this.setBytesHash(16);
        } else if(bytes<2){
            System.err.println("CONFIGURAÇÃO: Menor valor possivel de Bytes para Hash é de 2." +
                    " Usando Valor Minimo de 2 Bytes para criação de Chaves Hash.");
            this.setBytesHash(2);
        } else {
            System.out.println("CONFIGURAÇÃO: Usando Valor de "+bytes+" Bytes para criação de Chaves Hash.");
            this.setBytesHash(bytes);
        }
        this.setMaiorNumeroServidor(calculaMaiorSevidor());


        this.idCluster     = Integer.parseInt(properties.getProperty("Id.Cluster"));
        this.ipsCluster    = properties.getProperty("Ips.Servidores.Cluster").trim().split(" ");
        this.portasCluster = properties.getProperty("Portas.Servidores.Cluster").trim().split(" ");
    }

    public List<Address> getAddressesCluster() { return this.addressesCluster; }
    public int getBytesHash() { return this.bytesHash; }
    public String getChaveHash() { return this.chaveHash; }
    public CopycatClient getClienteCluster() { return this.clienteCluster; }
    public FuncoesAtomix getFuncoesAtomix() { return funcoesAtomix; }
    public List<Finger> getFingerTable() { return fingerTable; }
    public int getIdCluster() { return idCluster; }
    public String getIp() { return ip; }
    public String getIpServidorEntrada() { return ipServidorEntrada; }
    public String[] getIpsCluster() { return ipsCluster; }
    public synchronized List<Mesa> getMesas(){ return this.mesas; }
    public BigInteger getMaiorNumeroServidor() { return maiorNumeroServidor; }
    public int getPorta() { return porta; }
    public int getPortaServidorEntrada() { return portaServidorEntrada; }
    public String[] getPortasCluster() { return portasCluster; }

    public void setAddressesCluster(List<Address> addressesCluster){ this.addressesCluster = addressesCluster; }
    public void setBytesHash(int bytesHash){ this.bytesHash = bytesHash; }
    public void setChaveHash(String chaveHash){ this.chaveHash = chaveHash; }
    public void setClienteCluster(CopycatClient clienteCluster){ this.clienteCluster = clienteCluster; }
    public void setFingerTable(List<Finger> fingerTable) { this.fingerTable = fingerTable; }
    public void setFuncoesAtomix(FuncoesAtomix funcoesAtomix) { this.funcoesAtomix = funcoesAtomix; }
    public void setIp(String ip) { this.ip = ip; }
    public void setIpServidorEntrada(String ipServidorEntrada) { this.ipServidorEntrada = ipServidorEntrada; }
    public void setMaiorNumeroServidor(BigInteger maiorNumeroServidor) { this.maiorNumeroServidor = maiorNumeroServidor; }
    public void setMesas(List<Mesa> mesas){ this.mesas = mesas; }
    public void setPorta(int porta) { this.porta = porta; }
    public void setPortaServidorEntrada(int portaServidorEntrada) { this.portaServidorEntrada = portaServidorEntrada; }

    @Override
    public void atualizarFingerTable(ComunicacaoOuterClass.atualizarFingerTableRequest request, StreamObserver<ComunicacaoOuterClass.atualizarFingerTableResponse> responseObserver) {
        if(this.getFingerTable()==null){
            this.setIpServidorEntrada(request.getIpNovo());
            this.setPortaServidorEntrada(request.getPortaNovo());
            System.out.println("ENTRADA NA REDE: Tentando criar finger table com ip "+this.getIpServidorEntrada()+":"+this.getPortaServidorEntrada());
            this.criarFingerTable();
        }

        if(request.getChaveHashNovo().equals(this.getChaveHash())){
            ComunicacaoOuterClass.atualizarFingerTableResponse.Builder atualizarFingerTableResponse = ComunicacaoOuterClass.atualizarFingerTableResponse.newBuilder();
            atualizarFingerTableResponse
                    .setCodigo(0)
                    .setMensagem("ENTRADA NA REDE: Servidor iniciado na Rede do Servidor de Entrada.")
                    .build();
            responseObserver.onNext(atualizarFingerTableResponse.build());
            responseObserver.onCompleted();
        } else {
            for (int i=0; i<this.getFingerTable().size(); i++){
                if(this.getFingerTable().get(i).getChaveHash().equals(request.getChaveHashAntigo())){
                    String chaveHashLinha = this.calculaHashLinhaFingerTable(i);
                    if( (request.getChaveHashAntigo().compareTo(this.getChaveHash())>=0 && chaveHashLinha.compareTo(request.getChaveHashNovo())<=0)
                            || (request.getChaveHashAntigo().compareTo(this.getChaveHash())<=0 && request.getChaveHashNovo().compareTo(this.getChaveHash())>0 && chaveHashLinha.compareTo(this.getChaveHash())>0 && chaveHashLinha.compareTo(request.getChaveHashNovo())<=0)
                            || (request.getChaveHashAntigo().compareTo(this.getChaveHash())<=0 && request.getChaveHashNovo().compareTo(this.getChaveHash())<0 && chaveHashLinha.compareTo(this.getChaveHash())<0 && chaveHashLinha.compareTo(request.getChaveHashNovo())<=0)
                            || (request.getChaveHashAntigo().compareTo(this.getChaveHash())<=0 && request.getChaveHashNovo().compareTo(this.getChaveHash())<0 && chaveHashLinha.compareTo(this.getChaveHash())>0 && chaveHashLinha.compareTo(request.getChaveHashNovo())>0)
                    ){
                        System.out.println("CONFIGURAÇÃO: Atualizando a Linha "+(i+1)+" da Finger Table, do servidor "+request.getChaveHashAntigo()+" para o "+request.getChaveHashNovo()+" ." +
                                " Chave da Requisição da Linha = "+chaveHashLinha+" .");
                        Finger finger = new Finger(request.getChaveHashNovo(), request.getIpNovo(), request.getPortaNovo()
                                , this.criarCanalGRPC(request.getIpNovo(), request.getPortaNovo()));
                        this.getFingerTable().set(i, finger);
                    }
                }
            }

            ComunicacaoOuterClass.atualizarFingerTableResponse atualizarFingerTableResponse =
                    this.getFingerTable().get(0).getServidor().atualizarFingerTable(request);
            responseObserver.onNext(atualizarFingerTableResponse);
            responseObserver.onCompleted();
        }
    }

    int buscaMesa(String chaveHashMesa){
        for (Mesa mesa : this.getMesas()){
            if( mesa.getChaveHash().equals(chaveHashMesa) ){
                return this.getMesas().indexOf(mesa);
            }
        }
        return -1;
    }

    public Finger buscaProximoServidor(String chaveHashRequisicao){
        if(this.getFingerTable()!=null){
            int indexProximo=0;
            for(int i=0; i<this.getFingerTable().size(); i++){
                if( chaveHashRequisicao.compareTo(this.getFingerTable().get(i).getChaveHash())>0
                        && this.getFingerTable().get(i).getChaveHash().compareTo( this.getFingerTable().get(indexProximo).getChaveHash() )>0 ){
                    indexProximo=i;
                }
            }
            return this.getFingerTable().get(indexProximo);
        }
        return null;
    }

    public String calculaHashLinhaFingerTable(int posicao){
        BigInteger bigInteger      = BigInteger.valueOf(2);
        BigInteger bigIntegerPow   = bigInteger.pow(posicao);
        BigInteger bigIntegerChave = new BigInteger(this.getChaveHash(), 16);
        BigInteger bigIntegerSoma  = bigIntegerChave.add(bigIntegerPow);
        if(bigIntegerSoma.compareTo(this.getMaiorNumeroServidor())>=0){
            bigIntegerSoma = bigIntegerSoma.subtract(this.getMaiorNumeroServidor());
        }
        byte[] bytes = bigIntegerSoma.toByteArray();
        int diferenca = bytes.length-this.getBytesHash();
        byte[] bytesCorretos = new byte[this.getBytesHash()];
        for(int i=diferenca, j=0; i<bytes.length; i++, j++){
            bytesCorretos[j]=bytes[i];
        }
        return ChaveHash.gerarHexa(bytesCorretos, this.getBytesHash());
    }

    public BigInteger calculaMaiorSevidor(){
        BigInteger bigInteger      = BigInteger.valueOf(2);
        BigInteger bigIntegerMaiorServidor   = bigInteger.pow(this.getBytesHash()*8);
        return bigIntegerMaiorServidor;
    }

    @Override
    public void comprarCarta(ComunicacaoOuterClass.requisicaoNaVezRequest request, StreamObserver<ComunicacaoOuterClass.comprarCartaResponse> responseObserver) {
        String chaveHashMesa = request.getChaveHashMesa();
        int indexMesa = buscaMesa( chaveHashMesa );

        if(indexMesa>=0 || this.getFingerTable()==null || request.getRealiza()){
            ComunicacaoOuterClass.comprarCartaResponse.Builder comprarCartaResponse = ComunicacaoOuterClass.comprarCartaResponse.newBuilder();
            if(indexMesa<0){
                comprarCartaResponse
                        .setCodigo(-1)
                        .setLetra("")
                        .setNaipe("")
                        .setValor(0)
                        .build();
            } else {
                Mesa mesa = this.getMesas().get(indexMesa);
                Jogador jogador = mesa.buscaJogador(request.getIp(), request.getNome());
                if (jogador != null) {
                    Carta carta = mesa.getBaralho().entregarCarta();

                    CompletableFuture future = this.getClienteCluster().submit(new ComprarCarta(chaveHashMesa, jogador.getIp(), jogador.getNome(),
                            carta.getLetra(), carta.getNaipe(), carta.getValor()));

                    comprarCartaResponse
                            .setCodigo(0)
                            .setLetra(carta.getLetra())
                            .setNaipe(carta.getNaipe())
                            .setValor(carta.getValor())
                            .build();
                } else {
                    comprarCartaResponse
                            .setCodigo(-2)
                            .setLetra("")
                            .setNaipe("")
                            .setValor(0)
                            .build();
                }
            }
            responseObserver.onNext(comprarCartaResponse.build());
        } else {
            Repasse repasse = this.descobrirRepasse(request.getChaveHashMesa());
            ComunicacaoOuterClass.requisicaoNaVezRequest.Builder requisicaoNaVezRequest =
                    ComunicacaoOuterClass.requisicaoNaVezRequest.newBuilder();
            requisicaoNaVezRequest
                    .setChaveHashMesa(request.getChaveHashMesa())
                    .setIp(request.getIp())
                    .setNome(request.getNome())
                    .setRealiza(repasse.getRealiza())
                    .build();
            System.out.println("REPASSE: Requisição de Compra de Carta da Sala "+requisicaoNaVezRequest.getChaveHashMesa()+" para o Servidor "+repasse.getProximoServidor().getChaveHash()+" .");
            ComunicacaoOuterClass.comprarCartaResponse comprarCartaResponse =
                    repasse.getProximoServidor().getServidor().comprarCarta(requisicaoNaVezRequest.build());

            responseObserver.onNext(comprarCartaResponse);
        }
        responseObserver.onCompleted();
    }

    public ComunicacaoGrpc.ComunicacaoBlockingStub criarCanalGRPC(String ipServidor, int portaServidor){
        if(!ipServidor.equals("") && portaServidor>0 && portaServidor<65365) {
            int loop = 0;
            while (loop < 3) {
                try {
                    ManagedChannel canal = ManagedChannelBuilder.forAddress(ipServidor, portaServidor).usePlaintext().build();
                    ComunicacaoGrpc.ComunicacaoBlockingStub servidor = ComunicacaoGrpc.newBlockingStub(canal);
                    return servidor;
                } catch (StatusRuntimeException e) {
                    loop++;
                    switch (e.getStatus().getCode()) {
                        case UNAVAILABLE:
                            System.err.println("Sistema Indisponivel no momento. " + loop + " Tentativa de reenvio.");
                            try {
                                sleep(10000);
                            } catch (InterruptedException ex) {
                            }
                            break;
                        default:
                            System.err.println("Erro não tratado " + e.getStatus().getCode() + ". " + loop + " Tentativa de reenvio.");
                            break;
                    }
                }
            }
        }
        return null;
    }

    public void criarFingerTable(){
        if(!this.getIpServidorEntrada().equals("") && this.getPortaServidorEntrada()>0 && this.getPortaServidorEntrada()<65365) {
            List<Finger> fingerTable = new ArrayList<>();
            ComunicacaoGrpc.ComunicacaoBlockingStub servidor = criarCanalGRPC(this.getIpServidorEntrada(), this.getPortaServidorEntrada());
            if (servidor != null) {
                System.out.println("CONFIGURAÇÃO: Criando Finger Table.");
                for (int i = 0; i < (this.getBytesHash() * 8); i++) {
                    String chaveRequisicao = this.calculaHashLinhaFingerTable(i);

                    stubs.ComunicacaoOuterClass.informarResponsavelHashRequest informarResponsavelHashRequest =
                            stubs.ComunicacaoOuterClass.informarResponsavelHashRequest.newBuilder()
                                    .setChaveHashRequisicao(chaveRequisicao)
                                    .setChaveHashNovo(this.getChaveHash())
                                    .build();
                    stubs.ComunicacaoOuterClass.informarResponsavelHashResponse informarResponsavelHashResponse =
                            servidor.informarResponsavelHash(informarResponsavelHashRequest);

                    String chaveResponsavel = informarResponsavelHashResponse.getChaveHash();
                    String ip;
                    int porta;
                    ComunicacaoGrpc.ComunicacaoBlockingStub canalServidorResponsavel;
                    if (chaveResponsavel.equals(this.getChaveHash())) {
                        ip = this.getIp();
                        porta = this.getPorta();
                        canalServidorResponsavel = null;
                    } else {
                        ip = informarResponsavelHashResponse.getIp();
                        porta = informarResponsavelHashResponse.getPorta();
                        canalServidorResponsavel = this.criarCanalGRPC(ip, porta);
                    }

                    Finger finger = new Finger(chaveResponsavel, ip, porta, canalServidorResponsavel);
                    System.out.println("CONFIGURAÇÃO: Finger Table Linha "+(i+1)+": Servidor "+finger.getChaveHash()+" ."+
                            " Chave da Requisição da Linha = "+chaveRequisicao+" .");
                    fingerTable.add(finger);
                }
                this.setFingerTable(fingerTable);
                System.out.println("CONFIGURAÇÃO: Finger Table Criada com Sucesso. Possui " + this.getFingerTable().size() + " linhas.");
            } else {
                System.err.println("ENTRADA NA REDE: Erro ao tentar concetar com o servidor." +
                        " Por favor verifique os dados do Servidor de Entrada.");
                System.exit(0);
            }
        }
    }

    @Override
    public void criarMesa(ComunicacaoOuterClass.criarMesaRequest request, StreamObserver<ComunicacaoOuterClass.criarMesaResponse> responseObserver) {
        String chaveHashMesa = request.getChaveHashMesa();
        if(chaveHashMesa.equals("")) {
            chaveHashMesa = ChaveHash.gerarChave(request.getIp() + " " + request.getNome(), this.getBytesHash());
        }

        if(this.getFingerTable()==null || request.getRealiza()) {
            ComunicacaoOuterClass.criarMesaResponse.Builder resposta = ComunicacaoOuterClass.criarMesaResponse.newBuilder();
            int indexMesa = this.buscaMesa(chaveHashMesa);
            if(indexMesa<0) {

                CompletableFuture future = this.getClienteCluster().submit(new CriarMesa(chaveHashMesa));
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                String msg = "A sala " + chaveHashMesa + " foi criada com sucesso!";
                resposta.setCodigo(0).setMensagem(msg).setChaveHashMesa(chaveHashMesa);
            } else {
                String msg = "Erro ao criar a sala " + chaveHashMesa + "!";
                resposta.setCodigo(-1).setMensagem(msg).setChaveHashMesa(chaveHashMesa);
            }
            responseObserver.onNext(resposta.build());
        }  else {
            Repasse repasse = this.descobrirRepasse(chaveHashMesa);
            ComunicacaoOuterClass.criarMesaRequest.Builder criarMesaRequest =
                    ComunicacaoOuterClass.criarMesaRequest.newBuilder();
            criarMesaRequest
                    .setChaveHashMesa(chaveHashMesa)
                    .setIp(request.getIp())
                    .setNome(request.getNome())
                    .setRealiza(repasse.getRealiza())
                    .build();
            System.out.println("REPASSE: Requisição de Criar Sala "+criarMesaRequest.getChaveHashMesa()+" para o Servidor "+repasse.getProximoServidor().getChaveHash()+" .");
            ComunicacaoOuterClass.criarMesaResponse criarMesaResponse =
                    repasse.getProximoServidor().getServidor().criarMesa(criarMesaRequest.build());

            responseObserver.onNext(criarMesaResponse);
        }

        responseObserver.onCompleted();
    }

    public Repasse descobrirRepasse(String chaveHashRequisicao){
        Repasse repasse = new Repasse();
        if(this.getFingerTable()!=null) {
            //ultimo servidor do anel
            if (this.getFingerTable().get(0).getChaveHash().compareTo(this.getChaveHash()) < 0) {
                repasse.setAtualUltimo(true);
                if (chaveHashRequisicao.compareTo(this.getChaveHash()) > 0
                        && chaveHashRequisicao.compareTo(this.getFingerTable().get(0).getChaveHash()) > 0) {
                    repasse.setRealiza(true);
                    repasse.setProximoServidor(this.getFingerTable().get(0));
                } else if (chaveHashRequisicao.compareTo(this.getChaveHash()) < 0
                        && chaveHashRequisicao.compareTo(this.getFingerTable().get(0).getChaveHash()) < 0) {
                    repasse.setRealiza(true);
                    repasse.setProximoServidor(this.buscaProximoServidor(chaveHashRequisicao));
                } else {
                    repasse.setRealiza(false);
                    repasse.setProximoServidor(this.buscaProximoServidor(chaveHashRequisicao));
                }
            } else if (chaveHashRequisicao.compareTo(this.getChaveHash()) > 0
                    && chaveHashRequisicao.compareTo(this.getFingerTable().get(0).getChaveHash()) < 0) {
                repasse.setAtualUltimo(false);
                repasse.setRealiza(true);
                repasse.setProximoServidor(this.getFingerTable().get(0));
            } else {
                repasse.setAtualUltimo(false);
                repasse.setRealiza(false);
                repasse.setProximoServidor(this.buscaProximoServidor(chaveHashRequisicao));
            }
        } else {
            Finger finger = new Finger(this.getChaveHash(), this.getIp(), this.getPorta(), null);
            repasse.setAtualUltimo(true);
            repasse.setRealiza(true);
            repasse.setProximoServidor(finger);
        }

        return repasse;
    }

    @Override
    public void entrarMesa(ComunicacaoOuterClass.entrarMesaRequest request, StreamObserver<ComunicacaoOuterClass.informacoesJogoResponse> responseObserver) {
        String chaveHashMesa = request.getChaveHashMesa();
        Jogador jogador = new Jogador();
        jogador.setIp( request.getIp() );
        jogador.setNome( request.getNome() );
//        jogador.setResponseObserver( responseObserver );

        int index = buscaMesa( chaveHashMesa );
        ComunicacaoOuterClass.informacoesJogoResponse.Builder resposta = ComunicacaoOuterClass.informacoesJogoResponse.newBuilder();
        if(index>=0 || this.getFingerTable()==null || request.getRealiza()){
            if(index<0){
                System.err.println("ENTRADA NA SALA: Erro na tentativa do Jogador "+request.getNome()+" entrar na sala "+chaveHashMesa+"! " +
                        "Pois essa Sala não existe.");
                resposta.setCodigo(-1).setMensagem("Erro: A Sala "+chaveHashMesa+" não existe!");
                responseObserver.onNext(resposta.build());
            } else {

                if (this.getMesas().get(index).getJogadores().size() < 5) {
//                if (this.getMesas().get(index).addJogador(jogador)) {

//                    CompletableFuture future = this.getClienteCluster().submit(new EntrarMesa(chaveHashMesa, request.getIp(), request.getNome(), request.getPartidas(), request.getVitorias(), responseObserver));
                    CompletableFuture future = this.getClienteCluster().submit(new EntrarMesa(chaveHashMesa, request.getIp(), request.getNome(), request.getPartidas(), request.getVitorias()));
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                } else {
                    System.err.println("ENTRADA NA SALA: Erro na tentativa do Jogador " + request.getNome() + " entrar na sala " + chaveHashMesa + "! " +
                            "Pois ja existe(em) " + this.getMesas().get(index).getJogadores().size() + " Jogador(es) nessa Sala.");
                    resposta.setCodigo(-2).setMensagem("Erro: Sala com quantidade maxima de jogadores.");
                    responseObserver.onNext(resposta.build());
                }
            }
        } else {
            Repasse repasse = this.descobrirRepasse(request.getChaveHashMesa());
            ComunicacaoOuterClass.entrarMesaRequest.Builder entrarMesaRequest =
                    ComunicacaoOuterClass.entrarMesaRequest.newBuilder();
            entrarMesaRequest
                    .setChaveHashMesa(request.getChaveHashMesa())
                    .setIp(request.getIp())
                    .setNome(request.getNome())
                    .setPartidas(request.getPartidas())
                    .setRealiza(repasse.getRealiza())
                    .setVitorias(request.getVitorias())
                    .build();
            System.out.println("REPASSE: Requisição de Entrada na Sala "+entrarMesaRequest.getChaveHashMesa()+" para o Servidor "+repasse.getProximoServidor().getChaveHash()+" .");

            try {
                Iterator<ComunicacaoOuterClass.informacoesJogoResponse> informacoesJogoResponse =
                        repasse.getProximoServidor().getServidor().entrarMesa(entrarMesaRequest.build());

                for (Iterator<ComunicacaoOuterClass.informacoesJogoResponse> it = informacoesJogoResponse; it.hasNext(); ) {
                    ComunicacaoOuterClass.informacoesJogoResponse respostaOutroServidor = it.next();
                    responseObserver.onNext(respostaOutroServidor);
                }
            } catch (StatusRuntimeException e){  }
        }
    }

    @Override
    public void informarResponsavelHash(ComunicacaoOuterClass.informarResponsavelHashRequest request, StreamObserver<ComunicacaoOuterClass.informarResponsavelHashResponse> responseObserver) {
        Repasse repasse = this.descobrirRepasse(request.getChaveHashRequisicao());

        if(this.getFingerTable()==null || request.getChaveHashNovo().equals(this.getFingerTable().get(0).getChaveHash())) {
            ComunicacaoOuterClass.informarResponsavelHashResponse.Builder resposta =
                    ComunicacaoOuterClass.informarResponsavelHashResponse.newBuilder();

            if( (request.getChaveHashNovo().compareTo(this.getChaveHash())<0 && request.getChaveHashRequisicao().compareTo(this.getChaveHash())<=0 && request.getChaveHashRequisicao().compareTo(request.getChaveHashNovo())>0)
                    || (request.getChaveHashNovo().compareTo(this.getChaveHash())>0 && request.getChaveHashRequisicao().compareTo(this.getChaveHash())>0 && request.getChaveHashRequisicao().compareTo(request.getChaveHashNovo())>0)
                    || (request.getChaveHashNovo().compareTo(this.getChaveHash())>0 && request.getChaveHashRequisicao().compareTo(this.getChaveHash())<0 && request.getChaveHashRequisicao().compareTo(request.getChaveHashNovo())<0)
            ){
                resposta.setChaveHash(this.getChaveHash())
                        .setIp(this.getIp())
                        .setPorta(this.getPorta())
                        .build();
            } else {
                resposta.setChaveHash(request.getChaveHashNovo())
                        .setIp("")
                        .setPorta(0)
                        .build();
            }
            responseObserver.onNext(resposta.build());
        } else {
            ComunicacaoOuterClass.informarResponsavelHashResponse.Builder resposta =
                    ComunicacaoOuterClass.informarResponsavelHashResponse.newBuilder();

            if (repasse.getRealiza()) {
                if ( (!repasse.getAtualUltimo() && request.getChaveHashRequisicao().compareTo(request.getChaveHashNovo()) <= 0 && request.getChaveHashNovo().compareTo(this.getChaveHash()) > 0 && request.getChaveHashNovo().compareTo(repasse.getProximoServidor().getChaveHash()) < 0)
                        || (repasse.getAtualUltimo() && request.getChaveHashRequisicao().compareTo(request.getChaveHashNovo()) <= 0 && request.getChaveHashNovo().compareTo(this.getChaveHash()) > 0 && request.getChaveHashNovo().compareTo(repasse.getProximoServidor().getChaveHash()) > 0)
                        || (repasse.getAtualUltimo() && request.getChaveHashRequisicao().compareTo(request.getChaveHashNovo()) <= 0 && request.getChaveHashNovo().compareTo(this.getChaveHash()) < 0 && request.getChaveHashNovo().compareTo(repasse.getProximoServidor().getChaveHash()) < 0)
                ) {
                    resposta.setChaveHash(request.getChaveHashNovo())
                            .setIp("")
                            .setPorta(0)
                            .build();
                } else {
                    resposta.setChaveHash(repasse.getProximoServidor().getChaveHash())
                            .setIp(repasse.getProximoServidor().getIp())
                            .setPorta(repasse.getProximoServidor().getPorta())
                            .build();
                }
                responseObserver.onNext(resposta.build());
            } else {
                ComunicacaoOuterClass.informarResponsavelHashRequest.Builder informarResponsavelHashRequest =
                        ComunicacaoOuterClass.informarResponsavelHashRequest.newBuilder();
                informarResponsavelHashRequest
                        .setChaveHashRequisicao(request.getChaveHashRequisicao())
                        .setChaveHashNovo(request.getChaveHashNovo())
                        .build();

                ComunicacaoOuterClass.informarResponsavelHashResponse respostaRepasse =
                        repasse.getProximoServidor().getServidor().informarResponsavelHash(informarResponsavelHashRequest.build());
                responseObserver.onNext(respostaRepasse);
            }
        }
        responseObserver.onCompleted();
    }

    @Override
    public void passarVez(ComunicacaoOuterClass.requisicaoNaVezRequest request, StreamObserver<ComunicacaoOuterClass.passarVezResponse> responseObserver) {
        String chaveHashMesa = request.getChaveHashMesa();
        int indexMesa = buscaMesa( chaveHashMesa );

        if(indexMesa>=0 || this.getFingerTable()==null || request.getRealiza()){
            ComunicacaoOuterClass.passarVezResponse.Builder passarVezResponse = ComunicacaoOuterClass.passarVezResponse.newBuilder();
            if(indexMesa<0){
                passarVezResponse
                        .setCodigo(2)
                        .setMensagem("Erro ao encontrar a Sala "+chaveHashMesa+".")
                        .build();
            } else {
                Mesa mesa = this.getMesas().get(indexMesa);
                Jogador jogador = mesa.buscaJogador(request.getIp(), request.getNome());
                if (jogador != null) {
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
            }
            responseObserver.onNext(passarVezResponse.build());
        } else {
            Repasse repasse = this.descobrirRepasse(request.getChaveHashMesa());
            ComunicacaoOuterClass.requisicaoNaVezRequest.Builder requisicaoNaVezRequest =
                    ComunicacaoOuterClass.requisicaoNaVezRequest.newBuilder();
            requisicaoNaVezRequest
                    .setChaveHashMesa(request.getChaveHashMesa())
                    .setIp(request.getIp())
                    .setNome(request.getNome())
                    .setRealiza(repasse.getRealiza())
                    .build();
            System.out.println("REPASSE: Requisição de Passar Vez da Sala "+requisicaoNaVezRequest.getChaveHashMesa()+" para o Servidor "+repasse.getProximoServidor().getChaveHash()+" .");
            ComunicacaoOuterClass.passarVezResponse passarVezResponse =
                    repasse.getProximoServidor().getServidor().passarVez(requisicaoNaVezRequest.build());

            responseObserver.onNext(passarVezResponse);
        }
        responseObserver.onCompleted();
    }

    void removerMesa(Mesa mesa) {
        if (mesa != null) {
            this.getMesas().remove(mesa);
            System.err.println("EXCLUSÃO DE SALA: A Sala " + mesa.getChaveHash() + " acaba de ser excluida, pois não existem jogadores nela! Existe(em) " + this.getMesas().size() + " Sala(s) neste Servidor.");
        }
    }

    @Override
    public void sairMesa(ComunicacaoOuterClass.requisicaoNaVezRequest request, StreamObserver<ComunicacaoOuterClass.sairMesaResponse> responseObserver) {
        String chaveHashMesa = request.getChaveHashMesa();
        int indexMesa = buscaMesa( chaveHashMesa );

        if(indexMesa>=0 || this.getFingerTable()==null || request.getRealiza()){
            ComunicacaoOuterClass.sairMesaResponse.Builder sairMesaResponse = ComunicacaoOuterClass.sairMesaResponse.newBuilder();
            if(indexMesa<0){
                sairMesaResponse
                        .setCodigo(2)
                        .setMensagem("Erro ao encontrar a Sala "+chaveHashMesa+".")
                        .build();
            } else {
                Mesa mesa = this.getMesas().get(indexMesa);
                Jogador jogador = mesa.buscaJogador(request.getIp(), request.getNome());
                if (jogador != null && mesa.retirarJogador(jogador)) {
                    jogador.getResponseObserver().onCompleted();
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
            }
            responseObserver.onNext(sairMesaResponse.build());
        } else {
            Repasse repasse = this.descobrirRepasse(request.getChaveHashMesa());
            ComunicacaoOuterClass.requisicaoNaVezRequest.Builder requisicaoNaVezRequest =
                    ComunicacaoOuterClass.requisicaoNaVezRequest.newBuilder();
            requisicaoNaVezRequest
                    .setChaveHashMesa(request.getChaveHashMesa())
                    .setIp(request.getIp())
                    .setNome(request.getNome())
                    .setRealiza(repasse.getRealiza())
                    .build();
            System.out.println("REPASSE: Requisição de Sair da Sala "+requisicaoNaVezRequest.getChaveHashMesa()+" para o Servidor "+repasse.getProximoServidor().getChaveHash()+" .");

            ComunicacaoOuterClass.sairMesaResponse sairMesaResponse =
                    repasse.getProximoServidor().getServidor().sairMesa(requisicaoNaVezRequest.build());

            responseObserver.onNext(sairMesaResponse);
        }
        responseObserver.onCompleted();
    }

    public void solicitarEntradaNaRede(){
        if(this.getFingerTable()!=null){
            stubs.ComunicacaoOuterClass.atualizarFingerTableRequest atualizarFingerTableRequest =
                    stubs.ComunicacaoOuterClass.atualizarFingerTableRequest.newBuilder()
                            .setChaveHashAntigo(this.getFingerTable().get(0).getChaveHash())
                            .setChaveHashNovo(this.getChaveHash())
                            .setIpNovo(this.getIp())
                            .setPortaNovo(this.getPorta())
                            .build();

            ComunicacaoOuterClass.atualizarFingerTableResponse atualizarFingerTableResponse =
                    this.getFingerTable().get(0).getServidor().atualizarFingerTable(atualizarFingerTableRequest);
            if(atualizarFingerTableResponse.getCodigo()==0){
                System.out.println("ENTRADA NA REDE: Servidor iniciado na Rede do Servidor "+
                        this.getIpServidorEntrada()+":"+this.getPortaServidorEntrada()+".");
            }
        } else {
            System.out.println("ENTRADA NA REDE: Unico Servidor iniciado nesta Rede.");
        }
    }
}