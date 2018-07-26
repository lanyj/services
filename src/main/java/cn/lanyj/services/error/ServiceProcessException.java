package cn.lanyj.services.error;

public class ServiceProcessException extends ARuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7853104780110103807L;

	String message;
	
	public ServiceProcessException(String message) {
		super(message);
	}
	
}
