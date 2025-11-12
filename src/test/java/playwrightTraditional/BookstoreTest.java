package playwrightTraditional;

import com.microsoft.playwright.*;
import org.junit.jupiter.api.*;
import java.nio.file.Paths;
import static org.junit.jupiter.api.Assertions.*;

public class BookstoreTest {
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
    void testBookstoreSearchAndAddToCart() {
        // Wait for and fill search box
        page.waitForSelector("input[name='searchterm']");
        page.locator("input[name='searchterm']").fill("earbuds");
        page.keyboard().press("Enter");
        page.waitForTimeout(5000);

        // Expanding Brand filter
        page.waitForSelector("text=Brand");
        page.getByText("Brand").click();
        page.waitForSelector("label:has-text('JBL')");
        page.getByText("JBL").click();

        // Expanding Color filter
        page.getByText("Color").click();
        page.getByText("Black").click();

        // Expanding Price filter
        page.getByText("Price").click();
        page.getByText("Over $50").click();

        // Clicking on product
        page.waitForSelector("text=JBL Quantum True Wireless Noise Cancelling");
        page.getByText("JBL Quantum True Wireless Noise Cancelling").click();

        String productName = page.locator("h1.product-name").innerText();
        assertTrue(productName.contains("JBL Quantum"), "Product name missing");

        String price = page.locator(".price").innerText();
        assertTrue(price.contains("$"), "Price missing");

        // Adding to Cart
        page.waitForSelector("button:has-text('Add to Cart')");
        page.locator("button:has-text('Add to Cart')").click();
        page.waitForSelector("text=1 Item");

        String cartText = page.locator("text=/1 Item/").innerText();
        assertTrue(cartText.contains("1"), "Cart should have 1 item");

        // Going to cart
        page.locator("a[href*='cart']").click();
        page.waitForURL("**/cart");
        assertTrue(page.url().contains("cart"));
    }

    @AfterEach
    void teardown() {
        context.close();
    }

    @AfterAll
    static void cleanup() {
        browser.close();
        playwright.close();
    }
}


