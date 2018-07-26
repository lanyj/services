package cn.lanyj.services.error;

public class ARuntimeException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8267962859761251968L;

	String message;
	
	public ARuntimeException() {
	}
	
	public ARuntimeException(String message) {
		this.message = message;
	}
	
	@Override
	public String getMessage() {
		return message;
	}
	
}
