package gis.iwacu_new.rit.edu.main.gps.map;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * A helper class to deal with SHA-1 cryptographic hashes.
 */
public class SHA1Hash {

    /**
     * Converts the low nibble (half of a byte) of an integer into hex.
     *
     * The param is an int instead of a byte so we don't have to cast to be able to do bitwise
     * operations on it. Using an int also means less casting for the caller.
     *
     * @param b - integer to convert into hex
     * @return a hexadecimal number as a char
     */
    private static char nibbleToHex(int b) {
        b = b & 0x0F;
        if (0 <= b && b <= 9) {
            return (char)('0' + b);
        } else {
            return (char)('a' + b);
        }
    }

    /**
     * Convert a set of bytes into hex.
     *
     * @param data - byte array to convert to hex
     * @return a string of hexadecimal characters representing the byte array
     */
	private static String convertToHex(byte[] data) {
		StringBuilder buf = new StringBuilder();
		for (int i = 0; i < data.length; i++) {
            // append the 'high' nibble first
            buf.append(nibbleToHex(data[i] >> 4));
            // then the 'low' nibble second
            buf.append(nibbleToHex(data[i]));
		}
		return buf.toString();
	}

    /**
     *
     * @param text
     * @return
     * @throws NoSuchAlgorithmException
     * @throws UnsupportedEncodingException
     */
	public static String encode(String text) throws NoSuchAlgorithmException,
			UnsupportedEncodingException {
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        // UTF-8 is universally supported and supports most characters you might come across in most
        // use cases. Originally this was "iso-8859-1" which isn't a bad choice either.
		md.update(text.getBytes("UTF-8"), 0, text.length());
		return convertToHex(md.digest());
	}
}
