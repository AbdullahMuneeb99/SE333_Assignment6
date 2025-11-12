package playwrightTraditional;

import com.microsoft.playwright.*;
import org.junit.jupiter.api.*;
import java.nio.file.Paths;
import static org.junit.jupiter.api.Assertions.*;

public class YourShoppingCartPageTest {
    static Playwright playwright;
    static Browser browser;
    BrowserContext context;
    Page page;

    @BeforeAll
    static void setupClass() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
    }

    @BeforeEach
    void setupTest() {
        context = browser.newContext(new Browser.NewContextOptions()
                .setRecordVideoDir(Paths.get("videos/"))
                .setRecordVideoSize(1280, 720));
        page = context.newPage();
        page.navigate("https://depaul.bncollege.com/");
        page.setDefaultTimeout(60000);
    }

    @Test
    void testCartPage() {
        // Ensuring at least one item in cart
        page.waitForSelector("input[name='searchterm']");
        page.locator("input[name='searchterm']").fill("earbuds");
        page.keyboard().press("Enter");
        page.waitForTimeout(4000);
        page.waitForSelector("text=JBL Quantum True Wireless Noise Cancelling");
        page.getByText("JBL Quantum True Wireless Noise Cancelling").click();
        page.waitForSelector("button:has-text('Add to Cart')");
        page.locator("button:has-text('Add to Cart')").click();

        // Go to cart
        page.waitForSelector("a[href*='cart']");
        page.locator("a[href*='cart']").click();
        page.waitForURL("**/cart");

        assertTrue(page.url().contains("cart"), "Should be on the cart page");
        assertTrue(page.content().toLowerCase().contains("shopping cart"), "Cart header should be visible");
    }

    @AfterEach
    void teardown() { context.close(); }
    @AfterAll
    static void cleanup() { browser.close(); playwright.close(); }
}


