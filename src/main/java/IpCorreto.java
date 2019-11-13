import java.io.IOException;
import java.net.*;
import java.util.Enumeration;

public class IpCorreto {
    public static String getIpCorreto() throws SocketException {
        String addrs = null;
        if(System.getProperty("os.name").contains("Linux")){
            Enumeration<NetworkInterface> em = NetworkInterface.getNetworkInterfaces();
            while (em.hasMoreElements()) {
                NetworkInterface i = (NetworkInterface) em.nextElement();
                for (Enumeration en2 = i.getInetAddresses(); en2.hasMoreElements();) {
                    InetAddress addr = (InetAddress) en2.nextElement();
                    if (!addr.isLoopbackAddress()) {
                        if (addr instanceof Inet4Address) {
                            addrs = addr.getHostAddress();
                        }
                        if (addr instanceof Inet6Address) {
                            if (true) {
                                continue;
                            }
                            addrs = addr.getHostAddress();
                        }
                    }
                }
            }
        }else{
            try {
                addrs = InetAddress.getLocalHost().getHostAddress();
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
        }
        return addrs;
    }

    public static String buscaServidor(){
        String ipServidor="";
        byte[] inBuf = new byte[256];
        try {
            final int portaMulticast = Integer.parseInt( ManipuladorArquivo.arquivoConfiguracao().getProperty("Porta.Multicast") );
            MulticastSocket socket = new MulticastSocket(portaMulticast);

            InetAddress address = InetAddress.getByName("224.2.2.3");
            socket.joinGroup(address);
            System.out.println("Meu IP = "+InetAddress.getLocalHost().getHostAddress() );
            System.out.println("Tentando se Conectar ao Servidor.");
            DatagramPacket inPacket = new DatagramPacket(inBuf, inBuf.length);
            while(true){
                socket.receive(inPacket);
                String mensagem = new String(inBuf, 0, inPacket.getLength());
                String textoResposta[] = mensagem.split(":");
                if(textoResposta[0].equals("IpDoServidor21")){
                    ipServidor = textoResposta[1];
                    socket.leaveGroup(address);
                    break;
                }
            }
        }catch (IOException ioe) {
            System.out.println(ioe);
        }
        return ipServidor;
    }

    public static void espalharServidor(int portaMulticast) {
        byte[] outBuf;
        try {
            DatagramSocket socket = new DatagramSocket();
            String ipServidor = "IpDoServidor21:"+IpCorreto.getIpCorreto();
            outBuf = (ipServidor).getBytes();
            InetAddress address = InetAddress.getByName("224.2.2.3");
            while (true) {
                DatagramPacket outPacket = new DatagramPacket(outBuf, outBuf.length, address, portaMulticast);
                socket.send(outPacket);
                try { Thread.sleep(2000); }catch (InterruptedException ie) {}
            }
        } catch (IOException ioe) { System.out.println(ioe); }
    }
}
