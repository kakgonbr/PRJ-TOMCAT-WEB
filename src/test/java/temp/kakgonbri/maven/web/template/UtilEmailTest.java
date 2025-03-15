package temp.kakgonbri.maven.web.template;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class UtilEmailTest {
    @Test
    public void doTest()
    {
        System.out.println("misc.Utils.Validator.email");

        Assertions.assertTrue(misc.Utils.Validator.email("abc@example.com"));
        Assertions.assertTrue(misc.Utils.Validator.email("yes@hello.com."));
        Assertions.assertTrue(misc.Utils.Validator.email("abc@example.com.vn"));
        
        Assertions.assertFalse(misc.Utils.Validator.email("abc@example@example.com"));
        Assertions.assertFalse(misc.Utils.Validator.email(""));
        Assertions.assertFalse(misc.Utils.Validator.email("hajhdsajda.com"));
        Assertions.assertFalse(misc.Utils.Validator.email("@example.com"));
        Assertions.assertFalse(misc.Utils.Validator.email("+^%&*ssa@gmail.com"));
    }
}
