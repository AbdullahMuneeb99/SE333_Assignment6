package playwrightTraditional;

import com.microsoft.playwright.*;
import org.junit.jupiter.api.*;
import java.nio.file.Paths;
import static org.junit.jupiter.api.Assertions.*;

public class YourShoppingCartFinalTest {
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
    void testCartDeleteFlow() {
        // Add product
        page.waitForSelector("input[name='searchterm']");
        page.locator("input[name='searchterm']").fill("notebook");
        page.keyboard().press("Enter");
        page.waitForTimeout(5000);
        page.locator(".product-title").first().click();
        page.waitForSelector("button:has-text('Add to Cart')");
        page.locator("button:has-text('Add to Cart')").click();

        // Going to cart
        page.waitForSelector("a[href*='cart']");
        page.locator("a[href*='cart']").click();
        page.waitForURL("**/cart");
        assertTrue(page.url().contains("cart"), "Should navigate to cart");

        // Deleting product if visible
        if (page.locator("button:has-text('Remove')").count() > 0) {
            page.locator("button:has-text('Remove')").first().click();
            page.waitForSelector("text=Your cart is empty");
            assertTrue(page.content().toLowerCase().contains("empty"), "Cart should be empty");
        }
    }

    @AfterEach
    void teardown() { context.close(); }
    @AfterAll
    static void cleanup() { browser.close(); playwright.close(); }
}


