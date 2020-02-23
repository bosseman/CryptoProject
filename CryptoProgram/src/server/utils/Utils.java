package server.utils;

import server.security.ServerConstants;

public class Utils {
	private final ServerConstants serverConstants = new ServerConstants();
	private StringBuilder responseBuilder; 
	public Utils() {
		
	}
	public String[] parseString(String parseThis) {
		String[] returnThis = parseThis.split(serverConstants.getDelim());
		
		return returnThis;
	}
	public String publicKeyTransfer() {
		responseBuilder = new StringBuilder();
		responseBuilder.append("KEY");
		responseBuilder.append(serverConstants.getDelim());
		responseBuilder.append(serverConstants.getPublickey());
		
		return responseBuilder.toString();
	}
}
