import commands.*;
import io.atomix.catalyst.transport.Address;
import io.atomix.catalyst.transport.netty.NettyTransport;
import io.atomix.copycat.client.CopycatClient;
import io.atomix.copycat.server.Commit;
import io.atomix.copycat.server.CopycatServer;
import io.atomix.copycat.server.StateMachine;
import io.atomix.copycat.server.storage.Storage;
import io.atomix.copycat.server.storage.StorageLevel;

import java.io.File;
import java.util.concurrent.CompletableFuture;

public class FuncoesAtomix extends StateMachine {
    private ConexaoServidor conexaoServidor;

    private FuncoesAtomix() {
        conexaoServidor = new ConexaoServidor();
    }

    public FuncoesAtomix(ConexaoServidor conexaoServidor, int idCluster, String[]ipsCluster, String[] portasCluster ) {
        this.conexaoServidor = conexaoServidor;
        iniciarCluster(idCluster, ipsCluster, portasCluster);
    }

    public void comprarCarta(Commit<ComprarCarta> commit) {
        if( commit!=null && !commit.operation().chaveHashMesa.isEmpty() ) {
            int indexMesa = conexaoServidor.buscaMesa(commit.operation().chaveHashMesa);

            if (indexMesa >= 0) {
                Mesa mesa = conexaoServidor.getMesas().get(indexMesa);
                Jogador jogador = mesa.buscaJogador(commit.operation().ipJogador, commit.operation().nomeJogador);
                if (jogador != null) {
                    Carta carta = new Carta(commit.operation().letra, commit.operation().naipe, commit.operation().valor);
                    jogador.comprarCarta(carta);
                }
            }
        }
    }

    public void criarMesa(Commit<CriarMesa> commit) {
        if (commit != null && !commit.operation().chaveHashMesa.isEmpty()) {
            Mesa mesa = new Mesa(commit.operation().chaveHashMesa, conexaoServidor);
            conexaoServidor.getMesas().add(mesa);

            System.out.println("CRIAÇÃO DE SALA: Sucesso ao tentar criar a sala " + commit.operation().chaveHashMesa + "!" +
                    " Existe(em) " + conexaoServidor.getMesas().size() + " Sala(s) aberta(s) neste Servidor.");
        }
    }

//    public void entrarMesa(Commit<EntrarMesa> commit) {
//        System.out.println("ENTRAR MESA");
//        if( commit!=null && !commit.operation().chaveHashMesa.isEmpty() ) {
//            int indexMesa = conexaoServidor.buscaMesa(commit.operation().chaveHashMesa);
//
//            if (indexMesa >= 0) {
//                Jogador jogador = new Jogador();
//                jogador.setIp( commit.operation().ipJogador );
//                jogador.setNome( commit.operation().nomeJogador );
//                jogador.setPartidas( commit.operation().partidasJogador );
//                jogador.setVitorias( commit.operation().vitoriasJogador );
//                jogador.setResponseObserver( commit.operation().responseObserver );
//
//                conexaoServidor.getMesas().get(indexMesa).addJogador(jogador);
//                System.out.println("ENTRADA NA SALA: O Jogador " + jogador.getNome() + " acaba de entrar na sala " + commit.operation().chaveHashMesa + "! " +
//                        "Existe(em) " + conexaoServidor.getMesas().get(indexMesa).getJogadores().size() + " Jogador(es) nessa Sala.");
//
//            }
//        }
//    }

    public void entrarMesa(Commit<EntrarMesa> commit) {
        int indexMesa = conexaoServidor.buscaMesa(commit.operation().chaveHashMesa);

            if (indexMesa >= 0) {
                Jogador jogador = new Jogador();
                jogador.setIp(commit.operation().ipJogador);
                jogador.setNome(commit.operation().nomeJogador);
                jogador.setPartidas(commit.operation().partidasJogador);
                jogador.setVitorias(commit.operation().vitoriasJogador);
//                jogador.setResponseObserver(commit.operation().responseObserver);

                conexaoServidor.getMesas().get(indexMesa).addJogador(jogador);
                System.out.println("ENTRADA NA SALA: O Jogador " + jogador.getNome() + " acaba de entrar na sala " + commit.operation().chaveHashMesa + "! " +
                        "Existe(em) " + conexaoServidor.getMesas().get(indexMesa).getJogadores().size() + " Jogador(es) nessa Sala.");
            }

    }

    public void iniciarCluster(int idCluster, String[] ipsCluster, String[] portasCluster) {
        for (int i = 0; i < ipsCluster.length; i ++) {
            Address address = new Address(ipsCluster[i], Integer.parseInt(portasCluster[i]));
            conexaoServidor.getAddressesCluster().add(address);
        }

        iniciarServidor(idCluster);

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        iniciarCliente(idCluster);
    }

    private void iniciarCliente(int idCluster) {
        CopycatClient.Builder builderCliente = CopycatClient.builder()
                .withTransport( NettyTransport.builder()
                        .withThreads(4)
                        .build());

        CopycatClient cliente = builderCliente.build();
        conexaoServidor.setClienteCluster(cliente);

        CompletableFuture<CopycatClient> future = cliente.connect(conexaoServidor.getAddressesCluster());
        future.join();

        if(idCluster==0){
            String chaveHash = ChaveHash.gerarChave(conexaoServidor.getIp()+" "+conexaoServidor.getPorta(), conexaoServidor.getBytesHash());
            conexaoServidor.setChaveHash( chaveHash );
        }

    }

    private void iniciarServidor(int idCluster) {
        CopycatServer.Builder builderServidor = CopycatServer.builder(conexaoServidor.getAddressesCluster().get(idCluster));

        builderServidor.withStateMachine(FuncoesAtomix::new);
        builderServidor.withTransport(NettyTransport.builder()
                .withThreads(4)
                .build());
        builderServidor.withStorage(Storage.builder()
                .withDirectory(new File("logs_" + idCluster))
                .withStorageLevel(StorageLevel.DISK)
                .build());

        CopycatServer server = builderServidor.build();

        if (idCluster == 0) {
            server.bootstrap().join();
        } else {
            server.join(conexaoServidor.getAddressesCluster()).join();
        }
    }

}