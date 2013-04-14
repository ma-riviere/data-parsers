package com.parser.commons.utils.properties;

import java.io.File;
import java.util.regex.Pattern;

import com.parser.commons.utils.properties.transformers.BooleanTransformer;
import com.parser.commons.utils.properties.transformers.ByteTransformer;
import com.parser.commons.utils.properties.transformers.CharTransformer;
import com.parser.commons.utils.properties.transformers.ClassTransformer;
import com.parser.commons.utils.properties.transformers.DoubleTransformer;
import com.parser.commons.utils.properties.transformers.EnumTransformer;
import com.parser.commons.utils.properties.transformers.FileTransformer;
import com.parser.commons.utils.properties.transformers.FloatTransformer;
import com.parser.commons.utils.properties.transformers.IntegerTransformer;
import com.parser.commons.utils.properties.transformers.LongTransformer;
import com.parser.commons.utils.properties.transformers.PatternTransformer;
import com.parser.commons.utils.properties.transformers.ShortTransformer;
import com.parser.commons.utils.properties.transformers.StringTransformer;

/**
 * This class is responsible for creating property transformers. Each time it creates new instance of custom property
 * transformer, but for build-in it uses shared instances to avoid overhead
 * 
 * @author SoulKeeper
 */
public class PropertyTransformerFactory {

	/**
	 * Returns property transformer or throws {@link com.parser.commons.utils.properties.TransformationException} if can't
	 * create new one.
	 * 
	 * @param clazzToTransform
	 *          Class that will is going to be transformed
	 * @param tc
	 *          {@link com.parser.commons.utils.properties.PropertyTransformer} class that will be instantiated
	 * @return instance of PropertyTransformer
	 * @throws TransformationException
	 *           if can't instantiate {@link com.parser.commons.utils.properties.PropertyTransformer}
	 */
	@SuppressWarnings("rawtypes")
	public static PropertyTransformer newTransformer(Class clazzToTransform, Class<? extends PropertyTransformer> tc)
		throws TransformationException {

		// Just a hack, we can't set null to annotation value
		if (tc == PropertyTransformer.class) {
			tc = null;
		}

		if (tc != null) {
			try {
				return tc.newInstance();
			}
			catch (Exception e) {
				throw new TransformationException("Can't instantiate property transfromer", e);
			}
		}
		if (clazzToTransform == Boolean.class || clazzToTransform == Boolean.TYPE) {
			return BooleanTransformer.SHARED_INSTANCE;
		}
		else if (clazzToTransform == Byte.class || clazzToTransform == Byte.TYPE) {
			return ByteTransformer.SHARED_INSTANCE;
		}
		else if (clazzToTransform == Character.class || clazzToTransform == Character.TYPE) {
			return CharTransformer.SHARED_INSTANCE;
		}
		else if (clazzToTransform == Double.class || clazzToTransform == Double.TYPE) {
			return DoubleTransformer.SHARED_INSTANCE;
		}
		else if (clazzToTransform == Float.class || clazzToTransform == Float.TYPE) {
			return FloatTransformer.SHARED_INSTANCE;
		}
		else if (clazzToTransform == Integer.class || clazzToTransform == Integer.TYPE) {
			return IntegerTransformer.SHARED_INSTANCE;
		}
		else if (clazzToTransform == Long.class || clazzToTransform == Long.TYPE) {
			return LongTransformer.SHARED_INSTANCE;
		}
		else if (clazzToTransform == Short.class || clazzToTransform == Short.TYPE) {
			return ShortTransformer.SHARED_INSTANCE;
		}
		else if (clazzToTransform == String.class) {
			return StringTransformer.SHARED_INSTANCE;
		}
		else if (clazzToTransform.isEnum()) {
			return EnumTransformer.SHARED_INSTANCE;
		}
		else if (clazzToTransform == File.class) {
			return FileTransformer.SHARED_INSTANCE;
		}
		else if (clazzToTransform == Pattern.class) {
			return PatternTransformer.SHARED_INSTANCE;
		}
		else if (clazzToTransform == Class.class) {
			return ClassTransformer.SHARED_INSTANCE;
		}
		else {
			throw new TransformationException("Transformer not found for class " + clazzToTransform.getName());
		}
	}
}
