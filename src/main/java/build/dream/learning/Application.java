package build.dream.learning;

import org.apache.commons.lang.SerializationUtils;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@EnableDiscoveryClient
@ServletComponentScan
public class Application {
    public static void main(String[] args) throws IOException {
        SpringApplication.run(Application.class, args);
        Class<Application> applicationClass = Application.class;
        InputStream inputStream = Application.class.getClassLoader().getResourceAsStream(applicationClass.getName().replaceAll("\\.", "/") + ".class");

        int length = 0;
        byte[] buffer = new byte[1024];
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        while ((length = inputStream.read(buffer, 0, 1024)) != -1) {
            byteArrayOutputStream.write(buffer, 0, length);
        }
        System.out.println(Base64.encodeBase64String(byteArrayOutputStream.toByteArray()));
    }
}
