package temp.kakgonbri.maven.web.template;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class UtilPhoneTest {
    @Test
    public void doTest()
    {
        System.out.println("misc.Utils.Validator.phoneNumber");

        Assertions.assertTrue(misc.Utils.Validator.phoneNumber("0123456789"));
        Assertions.assertTrue(misc.Utils.Validator.phoneNumber("+84123456789"));
        Assertions.assertTrue(misc.Utils.Validator.phoneNumber("+84 123456789"));
        Assertions.assertTrue(misc.Utils.Validator.phoneNumber("+01123456789"));
        Assertions.assertTrue(misc.Utils.Validator.phoneNumber("+01 123456789"));
        
        Assertions.assertFalse(misc.Utils.Validator.phoneNumber("+01 123456789hdajskd"));
        Assertions.assertFalse(misc.Utils.Validator.phoneNumber("asamd sa+01 123456789hdajskd"));
        Assertions.assertFalse(misc.Utils.Validator.phoneNumber("+01 "));
        Assertions.assertFalse(misc.Utils.Validator.phoneNumber("+01 1234567"));
    }
}
