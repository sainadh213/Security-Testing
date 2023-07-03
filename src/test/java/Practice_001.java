import org.openqa.selenium.By;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.zaproxy.clientapi.core.ApiResponse;
import org.zaproxy.clientapi.core.ClientApi;

import io.github.bonigarcia.wdm.WebDriverManager;

public class Practice_001 {
	static final String proxyAddress = "localhost";
	static final int proxyPort = 8081;
	static final String apiKey = "ahaa2egqqquqn60ddq8gl03op3";
	String appName = "ECDC";

	private WebDriver driver;
	private ClientApi api;

	@BeforeMethod
	public void setup() {
		String proxyURL = proxyAddress + ":" + proxyPort;

		Proxy proxy = new Proxy();
		proxy.setHttpProxy(proxyURL);
		proxy.setSslProxy(proxyURL);

		ChromeOptions co = new ChromeOptions();
		co.addArguments("--remote-allow-origins=*");
		co.setAcceptInsecureCerts(true);
		co.setProxy(proxy);
		WebDriverManager.chromedriver().setup();
		driver = new ChromeDriver(co);

		api = new ClientApi(proxyAddress, proxyPort, apiKey);
	}

	@Test
	public void securityTest() throws Throwable {
		driver.get("https://ecdc.v35.dev.zeroco.de/");
		Thread.sleep(2000);
		driver.findElement(By.id("appUserName")).sendKeys("admin");
		driver.findElement(By.id("appPassword")).sendKeys("Ecdc#1234");
		driver.findElement(By.id("loginBtn")).click();
		Thread.sleep(2000);
		driver.findElement(By.xpath("//span[@class='icon-certificate']")).click();
	}

	@AfterMethod
	public void tearDown() throws Throwable {
		if (api != null) {
			String title = "Security Report";
			String template = "traditional-html";
			String description = "This is Security Test Report of "+appName+" Application";
			String reportfilename = appName + " Security Report.html";
			String targetFolder = "D:\\Selenium\\Security\\Reports";
			ApiResponse response = api.reports.generate(title, template, null, description, null, null, null, null,
					null, reportfilename, null, targetFolder, null);
			System.out.println("Zap Report generated at this location:" + response.toString());

			
		}
		//driver.close();
	}
  
}
