package asia.chiase.core.exception;

public class CCException extends Exception{

	/** serialVersionUID */
	private static final long	serialVersionUID	= 1L;

	private String				errmessage;

	public CCException(){
		super();
	}

	public CCException(String message){
		super(message);
		errmessage = message;
	}

	/**
	 * <strong>Constructor</strong>
	 * 
	 * @param ex
	 */
	public CCException(Exception ex){
		ex.printStackTrace();
	}

	public String getErrmessage(){
		return errmessage;
	}

	public void setErrmessage(String errmessage){
		this.errmessage = errmessage;
	}
}