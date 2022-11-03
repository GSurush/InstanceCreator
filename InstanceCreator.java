import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.IllegalAccessException;
import java.lang.Class;
import java.lang.Deprecated;
import java.lang.reflect.Parameter;

public class InstanceCreator {
	private String errors = "";

	public InstanceCreator() {

	}

	public static String getNameOf(Class<?> arg0) {
		return arg0.getName();
	}

	public <T> T getInstance(Class<T> classD) {
		T result = null;
		Class<?> cl = classD;

		Object[] params = new Object[cl.getConstructors()[0].getParameterCount()];
		if (cl.getConstructors()[0].getParameterCount() == 0) {
			params = null;
		} else {
			params = cl.getConstructors()[0].getParameters();
		}
		try {
			try {
				try {
					Object newI = cl.getConstructors()[0].newInstance(params);
					result = (T) newI;
				} catch (InvocationTargetException e) {
					errors = errors + "\n" + e.getLocalizedMessage();
					e.printStackTrace();
				}
			} catch (InstantiationException e) {
				errors = errors + "\n" + e.getLocalizedMessage();
				e.printStackTrace();
			}
		} catch (IllegalAccessException e) {
			errors = errors + "\n" + e.getLocalizedMessage();
			e.printStackTrace();
		}
		return result;
	}

	public <T> T getInstance(Class<T> classD, int index) {
		T result = null;
		Class<?> cl = classD;
		int constructor;
		if (index >= cl.getConstructors().length) {
			constructor = cl.getConstructors().length - 1;
		} else {
			constructor = index;
		}

		Object[] params = new Object[cl.getConstructors()[constructor].getParameterCount()];
		if (cl.getConstructors()[constructor].getParameterCount() == 0) {
			params = null;
		} else {
			for (int i = 0; i < cl.getConstructors()[constructor].getParameterCount(); i++) {
				Parameter[] cs = cl.getConstructors()[constructor].getParameters();
				params[i] = new InstanceCreator().getInstance(cs[i].getType());
			}
		}
		try {
			try {
				try {
					Object newI = cl.getConstructors()[constructor].newInstance(params);
					result = (T) newI;
				} catch (InvocationTargetException e) {
					errors = errors + "\n" + e.getLocalizedMessage();
					e.printStackTrace();
				}
			} catch (InstantiationException e) {
				errors = errors + "\n" + e.getLocalizedMessage();
				e.printStackTrace();
			}
		} catch (IllegalAccessException e) {
			errors = errors + "\n" + e.getLocalizedMessage();
			e.printStackTrace();
		}
		return result;
	}

	public <T> T getInstance(String cannonicalClassName, int constructor) {
		Class<T> cl = null;
		try {
			cl = (Class<T>) Class.forName(cannonicalClassName);
		} catch (ClassNotFoundException e) {
			errors = errors + "\n" + e.getLocalizedMessage();
			e.printStackTrace();
		}
		T result = (T) getInstance(cl, constructor);
		return result;
	}

	public <T> T getInstance(String cannonicalClassName) {
		return getInstance(cannonicalClassName, 0);
	}

	public String getErrors() {
		return errors;
	}
}
