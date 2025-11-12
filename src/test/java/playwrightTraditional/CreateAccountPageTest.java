package playwrightTraditional;

import com.microsoft.playwright.*;
import org.junit.jupiter.api.*;
import java.nio.file.Paths;
import static org.junit.jupiter.api.Assertions.*;

public class CreateAccountPageTest {
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
    void testCreateAccountPageLoads() {
        page.waitForSelector("text=Sign In");
        page.getByText("Sign In").click();
        page.waitForSelector("text=Create an Account");
        page.getByText("Create an Account").click();

        assertTrue(page.url().contains("register"), "Should be on registration page");
        assertTrue(page.locator("input[name='firstName']").isVisible(), "First Name visible");
        assertTrue(page.locator("input[name='lastName']").isVisible(), "Last Name visible");
    }

    @AfterEach
    void teardown() { context.close(); }
    @AfterAll
    static void cleanup() { browser.close(); playwright.close(); }
}


