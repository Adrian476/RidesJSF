package modelo.rideExceptions;

public class IncorrectPasswordException extends Exception {
	 private static final long serialVersionUID = 1L;
	 
	 public IncorrectPasswordException()
	  {
	    super();
	  }
	  /**This exception is triggered if the question already exists 
	  *@param s String of the exception
	  */
	  public IncorrectPasswordException(String s)
	  {
	    super(s);
	  }
}
