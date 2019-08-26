package nl.hdkesting.familyTree.core.services;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Service
public class ApplicationProperties {
    private final Properties props;

    public ApplicationProperties() {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        props = new Properties();
        try (InputStream resourceStream = loader.getResourceAsStream("application.properties")) {
            props.load(resourceStream);
        } catch (IOException ex) {
            // TODO do something useful
        }
    }

    public String getGedcomSource() {
        return props.getProperty("gedcom.source");
    }

    public String getEncodedAdminUsername() {
        return props.getProperty("admin.user");
    }

    public String getEncodedAdminPassword() {
        return props.getProperty("admin.password");
    }
}
