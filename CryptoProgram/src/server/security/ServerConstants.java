package server.security;

public class ServerConstants {

	private final static String publicKey = ""; // Need to figure this out
	private final static String privateKey = "";
	private final static int timeOut = 1000000; // 1000 seconds
	private final static String delim = "~";

	public static String getPublickey() {
		return publicKey;
	}

	public static String getPrivatekey() {
		return privateKey;
	}

	public static int getTimeout() {
		return timeOut;
	}

	public static String getDelim() {
		return delim;
	}

}
