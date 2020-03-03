package edu.uoc.ictflag.core.dal;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Utility for making deep copies (vs. clone()'s shallow copies) of 
 * objects. Objects are first serialized and then deserialized. Error
 * checking is fairly minimal in this implementation. If an object is
 * encountered that cannot be serialized (or that references an object
 * that cannot be serialized) an error is printed to System.err and
 * null is returned. Depending on your specific application, it might
 * make more sense to have copy(...) re-throw the exception.
 */
public class DeepClone
{
	/**
	 * Returns a copy of the object, or null if the object cannot
	 * be serialized.
	 * @throws IOException 
	 */
	@SuppressWarnings("unchecked")
	public static <T> T clone(T orig) throws Exception
	{
		T obj = null;
		
		// Write the object out to a byte array
		FastByteArrayOutputStream fbos = new FastByteArrayOutputStream();
		ObjectOutputStream out = new ObjectOutputStream(fbos);
		out.writeObject(orig);
		out.flush();
		out.close();
		
		// Retrieve an input stream from the byte array and read
		// a copy of the object back in. 
		ObjectInputStream in = new ObjectInputStream(fbos.getInputStream());
		obj = (T) in.readObject();
		
		return obj;
	}
}
