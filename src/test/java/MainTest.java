import org.example.Main;
import org.junit.Before;
import org.junit.Test;
import java.io.IOException;
import static org.junit.Assert.*;

public class MainTest {
    private Main main;

    @Before
    public void setUp() {
        main = new Main();
    }

    @Test
    public void testGetRoles() {
        try {
            String roles = main.getRoles();
            assertNotNull("Роли не должны быть null", roles);
            System.out.println("Роли: " + roles);
        } catch (IOException e) {
            fail("Исключение при вызове getRoles: " + e.getMessage());
        }
    }

    @Test
    public void testSignUp() {
        try {
            String email = main.signUp();
            assertNotNull("Email не должен быть null", email);
            assertEquals("etern@example.com", email);
        } catch (IOException e) {
            fail("Исключение при вызове signUp: " + e.getMessage());
        }
    }

    @Test
    public void testGetCode() {
        try {
            String email = "etern@example.com";
            String code = main.getCode(email);
            assertNotNull("Код не должен быть null", code);
            System.out.println("Код: " + code);
        } catch (IOException e) {
            fail("Исключение при вызове getCode: " + e.getMessage());
        }
    }

    @Test
    public void testEncode() {
        try {
            String email = "etern@example.com";
            String code = "some-code";
            String token = main.encode(email, code);
            assertNotNull("Token не должен быть null", token);
            System.out.println("Token: " + token);
        } catch (IOException e) {
            fail("Исключение при вызове encode: " + e.getMessage());
        }
    }

    @Test
    public void testSetStatus() {
        try {
            String token = "some-token";
            main.setStatus(token);
            System.out.println("Статус успешно установлен с токеном: " + token);
        } catch (IOException e) {
            fail("Исключение при вызове setStatus: " + e.getMessage());
        }
    }
}
