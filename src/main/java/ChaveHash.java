import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

public class ChaveHash {
    public static String gerarChave(String stringBase, int qtdBytes) {
        Date hoje = new Date();
        stringBase += " "+hoje.toString();
        byte[] hash = gerarHash(stringBase,"MD5");
        String chave = gerarHexa(hash, qtdBytes);
        return chave;
    }

    public static byte[] gerarHash(String stringBase, String algoritmo) {
        try {
            MessageDigest md = MessageDigest.getInstance(algoritmo);
            md.update(stringBase.getBytes());
            return md.digest();
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }

    public static String gerarHexa(byte[] bytes, int qtdBytes) {
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < qtdBytes; i++) {
            int parteAlta = ((bytes[i] >> 4) & 0xf) << 4;
            int parteBaixa = bytes[i] & 0xf;
            if (parteAlta == 0) s.append('0');
            s.append(Integer.toHexString(parteAlta | parteBaixa));
        }
        return s.toString();
    }
}
