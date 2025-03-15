package temp.kakgonbri.maven.web.template;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class UtilPasswordTest {
    @Test
    public void doTest()
    {
        System.out.println("misc.Utils.Validator.password");

        Assertions.assertTrue(misc.Utils.Validator.password("_}24I:9t58Tu?m@e"));
        Assertions.assertTrue(misc.Utils.Validator.password("De@190569"));
        Assertions.assertTrue(misc.Utils.Validator.password("Password1!"));
        Assertions.assertTrue(misc.Utils.Validator.password("#m_4xF%t\"Bu5jeb$"));
        Assertions.assertTrue(misc.Utils.Validator.password("|YlzEc|1"));
        
        Assertions.assertFalse(misc.Utils.Validator.password("12345678aA"));
        Assertions.assertFalse(misc.Utils.Validator.password("^@},^@},^@},"));
        Assertions.assertFalse(misc.Utils.Validator.password(" 12345678aA@ "));
        Assertions.assertFalse(misc.Utils.Validator.password("12345678aA@   "));
    }
}
