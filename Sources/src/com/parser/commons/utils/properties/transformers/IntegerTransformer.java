package com.parser.commons.utils.properties.transformers;

import java.lang.reflect.Field;

import com.parser.commons.utils.properties.PropertyTransformer;
import com.parser.commons.utils.properties.TransformationException;

/**
 * Transfomrs string to integer. Integer can be represented both as decimal or hex value.
 * 
 * @author SoulKeeper
 */
public class IntegerTransformer implements PropertyTransformer<Integer> {

	/**
	 * Shared instance of this transformer. It's thread-safe so no need of multiple instances
	 */
	public static final IntegerTransformer SHARED_INSTANCE = new IntegerTransformer();

	/**
	 * Transforms value to integer
	 * 
	 * @param value
	 *          value that will be transformed
	 * @param field
	 *          value will be assigned to this field
	 * @return Integer object that represents value
	 * @throws TransformationException
	 *           if something went wrong
	 */
	@Override
	public Integer transform(String value, Field field) throws TransformationException {
		try {
			return Integer.decode(value);
		}
		catch (Exception e) {
			throw new TransformationException(e);
		}
	}
}
