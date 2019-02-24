package uk.co.bigsoft.apps.cfgen4j.exceptions;

public class CfGen4jException extends RuntimeException {

	private static final long serialVersionUID = -4496982273919710977L;

	public CfGen4jException(Throwable cause) {
		super(cause);
	}

	public CfGen4jException(String message) {
		super(message);
	}
}
