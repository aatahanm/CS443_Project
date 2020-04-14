package URLShortener;
import java.util.*;
import java.lang.*;
import java.io.*;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class TinyURL {
    // Function to generate a short url from integer ID
    static String idToShortURL(int n) {
        // Map to store 62 possible characters
        char map[] = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".toCharArray();

        StringBuffer shorturl = new StringBuffer();

        while (n > 0) {
            shorturl.append(map[n % 62]);
            n = n / 62;
        }
        String input = shorturl.reverse().toString();
        try {
            // Static getInstance method is called with hashing MD5
            MessageDigest md = MessageDigest.getInstance("MD5");

            // digest() method is called to calculate message digest
            //  of an input digest() return array of byte
            byte[] messageDigest = md.digest(input.getBytes());

            // Convert byte array into signum representation
            BigInteger no = new BigInteger(1, messageDigest);

            // Convert message digest into hex value
            String hashtext = no.toString(16);
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            return hashtext;
        }
        // For specifying wrong message digest algorithms
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

/*    public static void main (String[] args) throws IOException {
        int n = 12345;
        String shorturl = idToShortURL(n);
        System.out.println("Generated short url in MD5: " + shorturl);
    }*/
}
