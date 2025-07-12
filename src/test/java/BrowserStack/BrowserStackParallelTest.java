package BrowserStack;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.*;

import java.lang.reflect.Method;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class BrowserStackParallelTest {

    static String USERNAME = "ameet_sy8UWz";
    static String ACCESS_KEY = "KDV5AQ3YEeYw83AVeXtD";
    static String HUB_URL = "https://" + USERNAME + ":" + ACCESS_KEY + "@hub-cloud.browserstack.com/wd/hub";

    ThreadLocal<WebDriver> driver = new ThreadLocal<>();

    @DataProvider(name = "parallelProvider", parallel = true)
    public Object[][] parallelProvider(Method method) {
        return new Object[][]{
                { getDesktopCaps("Chrome", "138", "Windows", "11", "Win11-Chrome138") },
                { getDesktopCaps("Edge", "138", "Windows", "11", "Win11-Edge138") },
                { getDesktopCaps("Safari", "16.5", "OS X", "Ventura", "Mac-Safari16.5") },
                { getDesktopCaps("Opera", "12.0", "Windows", "12.0", "Opera8.1") },
                { getMobileCaps("Chrome", "Samsung Galaxy A52", "12.0", "Galaxy-A52") },
        };
    }

    @Test(dataProvider = "parallelProvider")
    public void testParallel(MutableCapabilities caps) {
        WebDriver drv = null;
        try {
            drv = new RemoteWebDriver(new URL(HUB_URL), caps);
            driver.set(drv);

            System.out.println("🔧 Running on: " + caps.toString());
            ElPaisSimple.runTest(drv);

            markTestStatus("passed", "Test completed successfully", drv);
        } catch (Exception e) {
            markTestStatus("failed", e.getMessage(), drv);
        } finally {
            if (drv != null) drv.quit();
        }
    }

    private void markTestStatus(String status, String reason, WebDriver drv) {
        try {
            ((RemoteWebDriver) drv).executeScript(
                    "browserstack_executor: {\"action\":\"setSessionStatus\",\"arguments\":{" +
                            "\"status\":\"" + status + "\",\"reason\":\"" + reason + "\"}}"
            );
        } catch (Exception ignored) {}
    }

    private MutableCapabilities getDesktopCaps(String browser, String version, String os, String osVersion, String sessionName) {
        MutableCapabilities caps = new MutableCapabilities();
        caps.setCapability("browserName", browser);
        caps.setCapability("browserVersion", version);

        Map<String, Object> opts = new HashMap<>();
        opts.put("os", os);
        opts.put("osVersion", osVersion);
        opts.put("sessionName", sessionName);
        opts.put("buildName", "Parallel TestNG Demo");
        opts.put("video", "true");
        opts.put("debug", "true");
        opts.put("consoleLogs", "info");

        caps.setCapability("bstack:options", opts);
        return caps;
    }

    private MutableCapabilities getMobileCaps(String browserName, String device, String osVersion, String sessionName) {
        MutableCapabilities caps = new MutableCapabilities();
        caps.setCapability("browserName", browserName);

        Map<String, Object> opts = new HashMap<>();
        opts.put("deviceName", device);
        opts.put("osVersion", osVersion);
        opts.put("realMobile", "true");
        opts.put("sessionName", sessionName);
        opts.put("buildName", "Parallel TestNG Demo");
        opts.put("video", "true");
        opts.put("debug", "true");
        opts.put("consoleLogs", "info");

        caps.setCapability("bstack:options", opts);
        return caps;
    }
}
