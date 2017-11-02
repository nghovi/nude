package nguyenhoangviet.vpcorp.android.exception;

/**
 * CAException
 */
public class CAException extends Exception{

	/** serialVersionUID */
	private static final long	serialVersionUID	= 1L;

	private String				errmessage;

	/**
	 * Instantiates a new CA exception.
	 */
	public CAException(){
		super();
	}

	/**
	 * Instantiates a new CA exception.
	 *
	 * @param message the message
	 */
	public CAException(String message){
		super(message);
		errmessage = message;
	}

	/**
	 * <strong>Constructor</strong>
	 *
	 * @param ex the ex
	 */
	public CAException(Exception ex){
		ex.printStackTrace();
	}

	/**
	 * Get errmessage.
	 *
	 * @return the string
	 */
	public String getErrmessage(){
		return errmessage;
	}

	/**
	 * Set errmessage.
	 *
	 * @param errmessage the errmessage
	 */
	public void setErrmessage(String errmessage){
		this.errmessage = errmessage;
	}
}
