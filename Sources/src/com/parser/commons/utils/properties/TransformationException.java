package com.parser.commons.utils.properties;

/**
 * This exception is internal for configuration process. Thrown by
 * {@link com.parser.commons.utils.properties.PropertyTransformer} when transformaton error occurs and is catched by
 * {@link com.parser.commons.utils.properties.ConfigurableProcessor}
 * 
 * @author SoulKeeper
 */
public class TransformationException extends RuntimeException {

	/**
	 * SerialID
	 */
	private static final long serialVersionUID = -6641235751743285902L;

	/**
	 * Creates new instance of exception
	 */
	public TransformationException() {
	}

	/**
	 * Creates new instance of exception
	 * 
	 * @param message
	 *          exception message
	 */
	public TransformationException(String message) {
		super(message);
	}

	/**
	 * Creates new instance of exception
	 * 
	 * @param message
	 *          exception message
	 * @param cause
	 *          exception that is the reason of this exception
	 */
	public TransformationException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Creates new instance of exception
	 * 
	 * @param cause
	 *          exception that is the reason of this exception
	 */
	public TransformationException(Throwable cause) {
		super(cause);
	}
}
