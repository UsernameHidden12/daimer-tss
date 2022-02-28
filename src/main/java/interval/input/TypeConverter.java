package interval.input;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public final class TypeConverter<T> {

    private final Class<T> typeArgumentClass;

    /**
     * Instantiate a converter which accepts as input a string and constructs an instance of Class clazz with the string
     * as argument.
     * @param clazz Class of which to generate a new instance of with a string as argument
     */
    public TypeConverter(Class<T> clazz) {
        typeArgumentClass = clazz;
    }

    /**
     * A way to generate `new T(String s)` using Java Reflection.
     * @param s Argument for constructor
     * @return new T(s)
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    T get(String s) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Constructor<T> genericConstructor = typeArgumentClass.getConstructor(String.class);
        return genericConstructor.newInstance(s);
    }

}
