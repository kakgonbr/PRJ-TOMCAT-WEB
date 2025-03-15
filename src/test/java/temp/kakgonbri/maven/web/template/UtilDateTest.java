package temp.kakgonbri.maven.web.template;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class UtilDateTest {
    @Test
    public void doTest()
    {
        System.out.println("misc.Utils.formatDate");

        Assertions.assertEquals(misc.Utils.formatDate(java.sql.Date.valueOf(java.time.LocalDate.now())), java.time.LocalDate.now().format(config.Config.Time.outputFormatDate));
    }
}
