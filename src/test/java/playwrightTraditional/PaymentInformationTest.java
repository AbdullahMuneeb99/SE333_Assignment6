package playwrightTraditional;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.AriaRole;
import org.junit.jupiter.api.*;

import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

public class PaymentInformationTest {
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
    }

    @Test
    void testPaymentInfoVisibility() {
        // Opening cart, even if empty
        page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Cart")).click();
        page.waitForTimeout(3000);

        // Proceeding to checkout if available
        if (page.getByText("Checkout").count() > 0) {
            page.getByText("Checkout").click();
            page.waitForTimeout(4000);

            assertTrue(page.content().toLowerCase().contains("payment") ||
                            page.content().toLowerCase().contains("card"),
                    "Payment information section should be visible");
        } else {
            System.out.println("Cart is empty — skipping checkout step.");
        }
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

