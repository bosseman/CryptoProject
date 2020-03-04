package serverImpl;

import java.security.SecureRandom;
import java.util.Random;

public class GenerateRandomStuff {

	private final static Random RANDOM = new SecureRandom();
	private final static String ALPHABET = "0123456789QWERTYUIOPLKJHGFDSAZXCVBNMmnbvcxzasdfghjklpoiuytrewq";

	public static String generateKey(int length) {
		return generateRandomString(length);
	}

	private  static String generateRandomString(int length) {
		StringBuilder returnValue = new StringBuilder(length);

		for (int i = 0; i < length; i++) {
			returnValue.append(ALPHABET.charAt(RANDOM.nextInt(ALPHABET.length())));
		}

		return new String(returnValue);
	}
}
