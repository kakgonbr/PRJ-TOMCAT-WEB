package temp.kakgonbri.maven.web.template;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class JUnitTest {
    @Test
    public void doTest()
    {
        System.out.println("BEGIN TEST");
        TestTarget target = new TestTarget();
        Assertions.assertTrue(target.doSomething());
        System.out.println("TEST SUCCESSFUL");
    }
}
