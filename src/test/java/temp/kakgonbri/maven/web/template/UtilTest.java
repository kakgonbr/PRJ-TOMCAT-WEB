package temp.kakgonbri.maven.web.template;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class UtilTest {
    @Test
    public void doTest()
    {
        System.out.println("BEGIN UTIL TEST");

        Assertions.assertEquals(misc.Utils.formatDate(new java.util.Date()), java.time.LocalDate.now().format(config.Config.Time.outputFormatDate));
    }
}
