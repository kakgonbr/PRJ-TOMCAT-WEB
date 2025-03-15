package temp.kakgonbri.maven.web.template;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class UtilUsernameTest {
    @Test
    public void doTest()
    {
        System.out.println("misc.Utils.Validator.username");

        Assertions.assertTrue(misc.Utils.Validator.username("ajhjda_jda212"));
        Assertions.assertTrue(misc.Utils.Validator.username("user11234567123"));
        Assertions.assertTrue(misc.Utils.Validator.username("my-guy-1"));
        Assertions.assertTrue(misc.Utils.Validator.username("hellow4rld"));
        
        Assertions.assertFalse(misc.Utils.Validator.username("space space"));
        Assertions.assertFalse(misc.Utils.Validator.username("AAAAAAAAAAAAAA!!!"));
        Assertions.assertFalse(misc.Utils.Validator.username("short"));
        Assertions.assertFalse(misc.Utils.Validator.username("tooloooooooooooooooooooooooooooooooooooooooooooooooooong"));
    }
}
