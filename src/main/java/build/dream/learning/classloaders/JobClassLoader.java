package build.dream.learning.classloaders;

import org.apache.tomcat.util.codec.binary.Base64;

public class JobClassLoader extends ClassLoader {
    private String classBase64;

    public JobClassLoader() {

    }

    public JobClassLoader(ClassLoader parent, String classBase64) {
        super(parent);
        this.classBase64 = classBase64;
    }

    public String getClassBase64() {
        return classBase64;
    }

    public void setClassBase64(String classBase64) {
        this.classBase64 = classBase64;
    }

    @Override
    protected Class<?> findClass(String name) {
        byte[] bytes = Base64.decodeBase64(classBase64);
        return defineClass(name, bytes, 0, bytes.length);
    }
}
