package org.redgogh.coreutils.experiment;

import org.junit.Test;

/**
 * @author Red Gogh
 */
public class AvailableProcessorsNumberTest {

    @Test
    public void test() {
        System.out.println(Runtime.getRuntime().availableProcessors());
    }

}
