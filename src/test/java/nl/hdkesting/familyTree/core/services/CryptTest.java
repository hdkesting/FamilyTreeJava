package nl.hdkesting.familyTree.core.services;

import org.junit.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class CryptTest {
    @Test
    public void PrintBryptedPassword() {
        var cryptor = new BCryptPasswordEncoder(); // make sure to use the correct number of iterations
        String encoded = cryptor.encode("Pa$$w0rd"); // showing the highly secure password in source
        System.out.println(encoded);
    }
}
