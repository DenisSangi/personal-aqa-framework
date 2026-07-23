import org.testng.Assert;
import org.testng.annotations.Test;

public class PlaceholderTest {

    private int sum = 7;

    @Test
    public void PlaceHolderTestPositive() {
        Placeholder placeholder = new Placeholder();
        Assert.assertEquals(sum, placeholder.countSum(3, 4));
    }

    @Test
    public void PlaceHolderTestNegative() {
        Placeholder placeholder = new Placeholder();
        Assert.assertNotEquals(sum, placeholder.countSum(3, 3));
    }
}
