package skyglass.demo.service;

public class ServiceException extends Exception {

	private static final long serialVersionUID = -5997887084858164385L;
	
	private Error error;
	
	public ServiceException(Error error) {
		this.error = error;
	}
	
	public ServiceException(String reason, String message) {
		this.error = new Error(reason, message);
	}
	
	public Error getError() {
		return error;
	}

}
