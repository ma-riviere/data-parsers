package com.parser.commons.utils.properties.transformers;

import java.lang.reflect.Field;

import com.parser.commons.utils.properties.PropertyTransformer;
import com.parser.commons.utils.properties.TransformationException;

/**
 * Transforms String to Byte. String can be in decimal or hex format.
 * {@link com.parser.commons.utils.properties.TransformationException} will be thrown if something goes wrong
 * 
 * @author SoulKeeper
 */
public class ByteTransformer implements PropertyTransformer<Byte> {

	/**
	 * Shared instance of this transformer. It's thread-safe so no need of multiple instances
	 */
	public static final ByteTransformer SHARED_INSTANCE = new ByteTransformer();

	/**
	 * Tansforms string to byte
	 * 
	 * @param value
	 *          value that will be transformed
	 * @param field
	 *          value will be assigned to this field
	 * @return Byte object that represents value
	 * @throws TransformationException
	 *           if something went wrong
	 */
	@Override
	public Byte transform(String value, Field field) throws TransformationException {
		try {
			return Byte.decode(value);
		}
		catch (Exception e) {
			throw new TransformationException(e);
		}
	}
}
