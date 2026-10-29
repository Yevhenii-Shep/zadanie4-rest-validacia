package sk.ukf.vd.exception;

public class EmailAlreadyExistsException extends RuntimeException {
	public EmailAlreadyExistsException(String email) {
		super("Email " + email + " už existuje");
	}
}
