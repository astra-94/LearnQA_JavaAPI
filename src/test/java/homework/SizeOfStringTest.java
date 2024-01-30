package homework;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import java.util.Random;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SizeOfStringTest {
    @Test
    public void testSizeOfString() {
        Random rand = new Random();
        int n = rand.nextInt(30);
        String string = RandomStringUtils.randomAlphanumeric(n).toUpperCase();
        System.out.println(string);
        assertTrue(string.length() > 15, "The string less than 15 symbols");
    }
}
