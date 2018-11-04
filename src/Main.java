import javax.crypto.Cipher;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Random;

public class Main {

    public static void main(String[] args)
    {
        int bitLength = 64;
        Random r = new SecureRandom();
        BigInteger p = BigInteger.probablePrime(bitLength, r);
        BigInteger g = BigInteger.valueOf(2);
        BigInteger x = new BigInteger(32, r).mod(p.subtract(BigInteger.valueOf(2))).add(BigInteger.valueOf(2));
        BigInteger y = g.modPow(x, p);

        String message = "Hello world";
        BigInteger[][] coddedMessage = encode(message, y, g, p, new BigInteger(32, r).mod(p.subtract(BigInteger.valueOf(2))).add(BigInteger.valueOf(2)));
        String decodedMessage = decode(coddedMessage, p, x);

        System.out.println(decodedMessage);
    }

    public static BigInteger[][] encode(String message, BigInteger y, BigInteger g, BigInteger p, BigInteger sessionKey)
    {
        BigInteger[] msg = divideMessage(message);
        BigInteger[][] ans = new BigInteger[msg.length][2];
        for(int i = 0; i < msg.length; i++)
        {
            ans[i] = encodePartOfMessage(msg[i], y, g, p, sessionKey);
        }
        return ans;
    }

    public static String decode(BigInteger[][] codedMessage, BigInteger p, BigInteger x)
    {
        BigInteger[] msg = new BigInteger[codedMessage.length];
        for(int i = 0; i < codedMessage.length; i++)
        {
            msg[i] = decodePartOfMessage(codedMessage[i][0], codedMessage[i][1], p, x);
        }
        return combineMessage(msg);
    }

    public static BigInteger decodePartOfMessage(BigInteger a, BigInteger b, BigInteger p, BigInteger x)
    {
        BigInteger T = a.modPow(p.subtract(x).subtract(BigInteger.ONE), p).multiply(b).mod(p);
        return T;
    }


    public static BigInteger[] encodePartOfMessage(BigInteger partOfMessage,  BigInteger y, BigInteger g, BigInteger p, BigInteger sessionKey)
    {
        BigInteger a = g.modPow(sessionKey, p);
        BigInteger b = y.modPow(sessionKey, p).multiply(partOfMessage).mod(p);
        return new BigInteger[]{a, b};
    }


    public static BigInteger[] divideMessage(String message)
    {
        BigInteger[] ans = new BigInteger[message.length()];
        char[] msg = message.toCharArray();
        for(int i = 0; i < message.length(); i++)
        {
            ans[i] = BigInteger.valueOf(msg[i]);
        }
        return ans;
    }

    public static String combineMessage(BigInteger[] message)
    {
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < message.length; i++)
            sb.append((char)Integer.parseInt(message[i].toString()));
        return sb.toString();
    }

}
