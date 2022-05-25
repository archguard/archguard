package tbs;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class AssertionRouletteTest {
    @Test
    public void testCloneNonBareRepoFromLocalTestServer() throws Exception {
        Calculate calculate = new Calculate();
        int result = calculate.add(7, 8);
        int success = 15;
        assertEquals(success, result);

        int subResult = calculate.sub(9, 2);
        int subSuccess = 7;
        assertEquals(subSuccess, subResult);
    }
}
