package com.hinode.crawler;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.RemoteWebElement;
import org.openqa.selenium.support.ui.Select;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import com.hinode.entity.House;
import com.hinode.entity.TabArea;
import com.hinode.entity.UrlMaster;
import com.hinode.service.HouseService;
import com.hinode.service.UrlMasterServcie;
import java.util.List;

@Component
public class Crawler {

	private static final Logger logger = LoggerFactory.getLogger(Crawler.class);

	@Autowired
	private HouseService houseService;

	@Autowired
	private UrlMasterServcie urlMasterServcie;

	private ArrayList<String> idDetailAreaExistHomes = new ArrayList<>();
	private ArrayList<String> idDetailAreaHadCrawlers = new ArrayList<>();
	private ArrayList<String> linkPaging = new ArrayList<>();
	private ArrayList<String> idProvinces = new ArrayList<>();
	private ArrayList<String> linkDetailhouses = new ArrayList<>();
	private int idDetailAreaExistHome = 0;
	private String exPathHrefHouse = "//div[@class='col-md-6'][1]/a[@href]";
	private String exPathLogon = "//a[@href='javascript:logon()']";
	private String exPathAreaSearch = "//a[@href='javascript:areaSearch()']";
	private String exPathSearchBukken = "//a[@href='javascript:searchBukken()']";
	private String exPathTopPage = "//a[@href='javascript:toppage()']";
	private ArrayList<String> urls = new ArrayList<String>();
	private String musashiPage = "musashi";
	private UrlMaster musashiMaster = new UrlMaster();
	ChromeDriver driver = null;

	@Scheduled(cron = "0 04 22 * * *")
	public void crawlerData() throws IOException {
		try {

			logger.info("Query get list url crawler");
			urls = new ArrayList<String>();
			urls = houseService.getUrlCrawler();
			logger.info("Have " + urls.size() + "records");

			logger.info("Start crawler web Musashi");
			crawlerWebMusashi();
			logger.info("End crawler web Musashi");
			
			logger.info("Start crawler web Leopalace");
			crawlerWebLeopalce();
			logger.info("End crawler web Leopalace");
		} catch (Exception e) {
			logger.error(e.toString());
			if (driver != null) {
				driver.close();
			}
		}
	}

	public void crawlerWebMusashi() {
		logger.info("Get urls masters");
		List<UrlMaster> urlMasters = urlMasterServcie.getAllUrlMaster();
		logger.info("UrlMasters have " + urlMasters.size() + "records");

		logger.info("For get url Corresponding");
		for (UrlMaster urlMaster : urlMasters) {
			if (musashiPage.equals(urlMaster.getPageName())) {
				musashiMaster = urlMaster;
				logger.info("musashiMaster: " + urlMaster.toString());
			}
		}
		if (musashiMaster.getLinkLogin() != null && !musashiMaster.getLinkLogin().isEmpty()) {
			String urlLogin = musashiMaster.getLinkLogin();
			String[] urlConditions = musashiMaster.getLinkSearchData().split(",");

			logger.info("Create chrome driver....");
			System.setProperty("webdriver.chrome.driver",
					"C:\\Users\\namth\\Downloads\\chromedriver_win32\\chromedriver.exe");
			driver = new ChromeDriver();
			
			logger.info("Login musashi ....");
			driver.get(urlLogin);
			driver.findElementById("user_login").clear();
			driver.findElementById("user_login").sendKeys("sale.hinode@gmail.com");
			driver.findElementById("user_pass").clear();
			driver.findElementById("user_pass").sendKeys("hinode2018");
			driver.findElement(By.xpath("//form[@id='loginform']/p[@class='submit']/button[@id='wp-submit']")).click();

			for (String urlCondition : urlConditions) {
				logger.info("Login musashi success ....");
				driver.get(urlCondition);

				logger.info("Start get url of: " + urlCondition);
				List<WebElement> urlDetails = driver.findElements(By.xpath(exPathHrefHouse));
				linkDetailhouses = new ArrayList<>();
				for (WebElement urlDetail : urlDetails) {
					linkDetailhouses.add(urlDetail.getAttribute("href"));
				}

				logger.info("Have " + linkDetailhouses.size() + "url in " + urlCondition);
				
				for (String item : linkDetailhouses) {
					if (!urls.contains(item)) {
						driver.get(item);
						logger.info("Start add to DB: " + item.toString());
						addProduct(item);
						logger.info("add success ...");
					}
				}
			}

			driver.close();
		}
	}

	public void addProduct(String url) {
		House house = new House();
		if (checkExitElementByXpath(driver, "//tbody/tr[2]/td[@class='syozaiti']/a")) {
			house.setAddress(driver.findElement(By.xpath("//tbody/tr[2]/td[@class='syozaiti']/a")).getText());
		} else {
			house.setAddress(null);
		}
		if (checkExitElementByXpath(driver, "//table[@class='table bukken-info']/tbody/tr[2]/td[2]")) {
			house.setArea(
					driver.findElement(By.xpath("//table[@class='table bukken-info']/tbody/tr[2]/td[2]")).getText());
		} else {
			house.setArea(null);
		}
		if (checkExitElementByXpath(driver, "//table[@class='table bukken-info']/tbody/tr[2]/td[1]")) {
			house.setBuildFrom(
					driver.findElement(By.xpath("//table[@class='table bukken-info']/tbody/tr[2]/td[1]")).getText());
		} else {
			house.setBuildFrom(null);
		}
		house.setDelFlg("0");
		if (checkExitElementByXpath(driver, "//table[@class='table bukken-info']/tbody/tr[11]/td[2]")) {
			house.setDepositeFee(
					driver.findElement(By.xpath("//table[@class='table bukken-info']/tbody/tr[11]/td[2]")).getText());
		} else {
			house.setDepositeFee(null);
		}
		if (checkExitElementByXpath(driver, "//table[@class='table bukken-info']/tbody/tr[11]/td[1]")) {
			house.setGuaranteeFee(
					driver.findElement(By.xpath("//table[@class='table bukken-info']/tbody/tr[11]/td[1]")).getText());
		} else {
			house.setGuaranteeFee(null);
		}
		if (checkExitElementByXpath(driver, "//table[@class='table bukken-info']/tbody/tr[3]/td[2]")) {
			house.setManageFee(
					driver.findElement(By.xpath("//table[@class='table bukken-info']/tbody/tr[3]/td[2]")).getText());
		} else {
			house.setManageFee(null);
		}

		if (checkExitElementByXpath(driver, "//table[@class='table bukken-info']/tbody/tr[5]/td")) {
			String movableDate = driver.findElement(By.xpath("//table[@class='table bukken-info']/tbody/tr[5]/td"))
					.getText();
			if ("即入居可".equals(movableDate)) {
				house.setMovableDate(LocalDate.now().toString());
			} else {
				house.setMovableDate(movableDate);
			}
		} else {
			house.setMovableDate(null);
		}
		if (checkExitElementByXpath(driver, "//h1[@class='entryPostTitle']")) {
			house.setName(driver.findElement(By.xpath("//h1[@class='entryPostTitle']")).getText());
		} else {
			house.setName(null);
		}

		if (checkExitElementByXpath(driver, "//table[@class='table bukken-info']/tbody/tr[12]/td")) {
			house.setOther(
					driver.findElement(By.xpath("//table[@class='table bukken-info']/tbody/tr[12]/td")).getText());
		} else {
			house.setOther(null);
		}

		house.setPersonInCharge("");

		if (checkExitElementByXpath(driver, "//table[@class='table bukken-info']/tbody/tr[3]/td[1]")) {
			house.setRentFee(
					driver.findElement(By.xpath("//table[@class='table bukken-info']/tbody/tr[3]/td[1]")).getText());
		} else {
			house.setRentFee(null);
		}

		if (checkExitElementByXpath(driver, "//table[@class='table bukken-info']/tbody/tr[1]/td[@class='madori']")) {
			house.setRoomType(
					driver.findElement(By.xpath("//table[@class='table bukken-info']/tbody/tr[1]/td[@class='madori']"))
							.getText());
		} else {
			house.setRoomType(null);
		}
		house.setStaffContact("");
		if (checkExitElementByXpath(driver, "//table[@class='table bukken-info']/tbody/tr[8]/td")) {
			house.setStation(
					driver.findElement(By.xpath("//table[@class='table bukken-info']/tbody/tr[8]/td")).getText());
		} else {
			house.setStation(null);
		}
		if (checkExitElementByXpath(driver, "//div[@class='row result-spec']/div[@class='col-md-2']/img[@src]")) {
			house.setUrlImage(
					driver.findElement(By.xpath("//div[@class='row result-spec']/div[@class='col-md-2']/img[@src]"))
							.getAttribute("src"));
		} else {
			house.setUrlImage(null);
		}

		house.setStatus("0");
		house.setIsCrawler(true);
		house.setUrlCrawler(url);
		houseService.add(house);

	}

	public void login() {
		logger.info("Create chrome drive (leopalace) ....");
		String url = "https://www.leopalace21.com/apps/tradeCondition/logonAction.do";
		System.setProperty("webdriver.chrome.driver",
				"C:\\Users\\namth\\Downloads\\chromedriver_win32\\chromedriver.exe");
		driver = new ChromeDriver();
		driver.get(url);
		driver.findElementById("textfield").clear();
		driver.findElementById("textfield").sendKeys("sale.hinode@gmail.com");
		driver.findElementById("textfield2").clear();
		driver.findElementById("textfield2").sendKeys("2018");
		if (checkExitElementByXpath(driver, exPathLogon)) {
			driver.findElement(By.xpath(exPathLogon)).click();
		}
	}

	public void crawlerWebLeopalce() {

		ArrayList<TabArea> tabs = new ArrayList<>();
		tabs.add(new TabArea("//a[text()='北海道・東北エリア']", "area1"));
		tabs.add(new TabArea("//a[text()='関東・甲信越エリア']", "area2"));
		tabs.add(new TabArea("//a[text()='北陸・中部エリア']", "area3"));
		tabs.add(new TabArea("//a[text()='近畿エリア']", "area4"));
		tabs.add(new TabArea("//a[text()='中国・四国エリア']", "area5"));
		tabs.add(new TabArea("//a[text()='九州・沖縄エリア']", "area6"));

		for (TabArea tab : tabs) {
		
			logger.info("Login tab ...." + tab.getTabName());
			login();
			logger.info("Login success leopalace ....");
			if (checkExitElementByXpath(driver, tab.getTabName())) {
				driver.findElement(By.xpath(tab.getTabName())).click();
				cloneTab(driver, tab.getIdTab());
				driver.close();
			}
		}
	}

	public void cloneTab(ChromeDriver driver, String idTab) {

		// tìm số tỉnh theo khu vực
		String xpathProvinceNumber = String.format("//div[@id='%s']//ul//li//label//input[@id]", idTab);
		List<WebElement> provinces = driver.findElements(By.xpath(xpathProvinceNumber));
		
		idProvinces = new ArrayList<>();
		for (WebElement province : provinces) {
			idProvinces.add(province.getAttribute("id"));
		}

		logger.info("Have " + idProvinces.size() + "provinces");
		
		// Vào chi tiết từng khu vực
		for (String idProvince : idProvinces) {
			logger.info("Go to " + idProvince);
			if (checkExitElementById(driver, idProvince)) {
				driver.findElement(By.id(idProvince)).click();
			}
			if (checkExitElementByXpath(driver, exPathAreaSearch)) {
				driver.findElement(By.xpath(exPathAreaSearch)).click();
			}

			// tìm số phường, thành phố, thị trấn, làng có trong tỉnh
			int conditionNumberInProvince = driver.findElements(By.xpath("//table//tr//td//label")).size();

			// tìm ra số phường, thành phố, thị trấn, làng trong tỉnh có nhà cho thuê
			idDetailAreaExistHomes = new ArrayList<>();
			for (int j = 0; j < conditionNumberInProvince; j++) {
				String idCheckboxDetail = "C" + j;
				String xpathCheckboxDetailArea = String.format("//table//tr//td//label[@for='C%s']", j);
				String textCheckboxDetailArea = driver.findElement(By.xpath(xpathCheckboxDetailArea)).getText();
				textCheckboxDetailArea = textCheckboxDetailArea.substring(textCheckboxDetailArea.indexOf('(') + 1,
						textCheckboxDetailArea.indexOf(')'));
				if (!"0".equals(textCheckboxDetailArea)) {
					idDetailAreaExistHomes.add(idCheckboxDetail);
				}
			}
			
			logger.info("Have " + idDetailAreaExistHomes.size() + "area exits homes");

			// chọn 10 điều kiện 1 lần để thỏa mãn với yêu cầu của trang craw
			isSelect10City(driver);

			// quay lại để chọn tỉnh khác trong khu vực
			if (checkExitElementByXpath(driver, exPathTopPage)) {
				driver.findElement(By.xpath(exPathTopPage)).click();
			}
		}
	}

	public void isSelect10City(ChromeDriver driver) {
		
		logger.info("Select 10 city ....");
		int isCheckToo10Element = 0;
		idDetailAreaHadCrawlers = new ArrayList<>();

		// chọn checkbox 10 điều kiện và xóa checkbox khi quay lại
		for (idDetailAreaExistHome = 0; idDetailAreaExistHome < idDetailAreaExistHomes
				.size(); idDetailAreaExistHome++) {
			idDetailAreaHadCrawlers.add(idDetailAreaExistHomes.get(idDetailAreaExistHome));
			if (checkExitElementById(driver, idDetailAreaExistHomes.get(idDetailAreaExistHome))) {
				driver.findElement(By.id(idDetailAreaExistHomes.get(idDetailAreaExistHome))).click();
			}
			isCheckToo10Element++;
			if (isCheckToo10Element == 10 || idDetailAreaExistHome == idDetailAreaExistHomes.size() - 1) {
				isCheckToo10Element = 0;

				if (checkExitElementByXpath(driver, exPathSearchBukken)) {
					driver.findElement(By.xpath(exPathSearchBukken)).click();
				}

				// chọn hiển thị 100 dòng 1 trang
				if (checkExitElementByXpath(driver,
						"//form[3]/div[@id='contents']/div[@id='main']/div[@class='blok'][2]/div[@class='sort']/select[2]")) {
					Select select = new Select(driver.findElement(By.xpath(
							"//form[3]/div[@id='contents']/div[@id='main']/div[@class='blok'][2]/div[@class='sort']/select[2]")));
					if (!select.getFirstSelectedOption().getText().equals("100件表示")) {
						select.selectByValue("100");
					}
				}

				List<WebElement> pages = driver
						.findElements(By.xpath(" //div[@class='blok']//div[@class='pagenavi']//ul//li//a[@href]"));
				linkPaging = new ArrayList<>();
				for (WebElement page : pages) {
					linkPaging.add(page.getAttribute("href"));
				}

				// thêm dữ liệu crawler được vào db
				addProductWebLeopalce(driver, urls);

				for (int index = 0; index < linkPaging.size() - 1; index++) {
					String xpathLinkPage = String.format("//a[@href='%s']", linkPaging.get(index));

					if (checkExitElementByXpath(driver, xpathLinkPage)) {
						driver.findElement(By.xpath(xpathLinkPage)).click();
					}

					// thêm dữ liệu crawler được vào db
					addProductWebLeopalce(driver, urls);
				}
				if (checkExitElementByXpath(driver, exPathAreaSearch)) {
					driver.findElement(By.xpath(exPathAreaSearch)).click();
				}
				unSelectCityHadCrawler(driver);
				idDetailAreaHadCrawlers = new ArrayList<>();
			}
		}
	}

	// Xóa các điều kiện đã crawler
	public void unSelectCityHadCrawler(ChromeDriver driver) {
		for (String detailAreaHadCrawler : idDetailAreaHadCrawlers) {
			if (checkExitElementById(driver, detailAreaHadCrawler)) {
				driver.findElement(By.id(detailAreaHadCrawler)).click();
			}
		}
	}

	public boolean checkExitElementByXpath(ChromeDriver driver, String xpath) {
		return driver.findElements(By.xpath(xpath)).size() != 0;
	}

	public boolean checkExitElementById(ChromeDriver driver, String xpath) {
		return driver.findElements(By.id(xpath)).size() != 0;
	}

	public boolean checkExitElementByClassName(ChromeDriver driver, String xpath) {
		return driver.findElements(By.className(xpath)).size() != 0;
	}

	public void addProductWebLeopalce(ChromeDriver driver, ArrayList<String> urls) {
		List<WebElement> driver1 = driver.findElements(By.xpath("//div[@class='section']"));
		for (int item = 0; item < driver1.size(); item++) {
			String hrefDetail = ((RemoteWebElement) driver1.get(item))
					.findElementsByXPath("//li[@class='name']/a[@href]").get(item).getAttribute("href");
			String id = hrefDetail.substring(hrefDetail.indexOf('(') + 1, hrefDetail.indexOf(')'));

			// kiểm tra xem đã crawler chưa.
			if (!urls.contains(id)) {
				House house = new House();

				if (checkExitElementByXpath(driver, "// div[@class='info']/ul/li[@class='line_blue']")) {
					house.setAddress(
							driver.findElement(By.xpath("// div[@class='info']/ul/li[@class='line_blue']")).getText());
				} else {
					house.setAddress(null);
				}
				if (checkExitElementByXpath(driver, "// div[@class='info02']/dl[@class='clearfix'][3]//dd")) {
					house.setArea(driver.findElement(By.xpath("// div[@class='info02']/dl[@class='clearfix'][3]//dd"))
							.getText().substring(1));
				} else {
					house.setArea(null);
				}
				if (checkExitElementByXpath(driver, "// div[@class='info02']/dl[@class='clearfix'][4]/dd")) {
					house.setBuildFrom(
							driver.findElement(By.xpath("// div[@class='info02']/dl[@class='clearfix'][4]/dd"))
									.getText().substring(1));
				} else {
					house.setBuildFrom(null);
				}
				house.setDelFlg("0");
				if (checkExitElementByXpath(driver, "// div[@class='info']/ul/li[@class='day']")) {
					house.setMovableDate(driver.findElement(By.xpath("// div[@class='info']/ul/li[@class='day']"))
							.getText().replace("※４", ""));
				} else {
					house.setMovableDate(null);
				}
				if (checkExitElementByXpath(driver, "// div[@class='info']/ul/li[@class='name']")) {
					house.setName(driver.findElement(By.xpath("// div[@class='info']/ul/li[@class='name']")).getText());
				} else {
					house.setName(null);
				}

				if (checkExitElementByXpath(driver, "// div[@class='info02']/dl[@class='clearfix'][5]")) {
					house.setOther(
							driver.findElement(By.xpath("// div[@class='info02']/dl[@class='clearfix'][5]")).getText());
				} else {
					house.setOther(null);
				}

				house.setPersonInCharge("");

				if (checkExitElementByXpath(driver, "// div[@class='info']/ul/li[@class='chintai']")) {
					house.setRentFee(driver.findElement(By.xpath("// div[@class='info']/ul/li[@class='chintai']"))
							.getText().replace("※1", ""));
				} else {
					house.setRentFee(null);
				}

				if (checkExitElementByXpath(driver, "// div[@class='info02']/dl[@class='clearfix'][2]/dd")) {
					house.setRoomType(
							driver.findElement(By.xpath("// div[@class='info02']/dl[@class='clearfix'][2]/dd"))
									.getText().substring(1));
				} else {
					house.setRoomType(null);
				}
				house.setStaffContact("");
				String station = "";
				if (checkExitElementByXpath(driver, "// div[@class='info']/ul/li[6]")) {
					station += driver.findElement(By.xpath("// div[@class='info']/ul/li[6]")).getText();
				}
				if (checkExitElementByXpath(driver, "//div[@class='info']/ul/li[7]")) {
					station += driver.findElement(By.xpath("//div[@class='info']/ul/li[7]")).getText().replace("※5",
							"");
				}
				if (station != "") {
					house.setStation(station);
				} else {
					house.setStation(null);
				}
				if (checkExitElementByXpath(driver, "//div[@class='img']/p[@class='photo']/a/img[@src]")) {
					house.setUrlImage(
							"https://www.leopalace21.com/app/image/request?TABLE=LEO_IMAGE&IMAGE=IM_IMAGE&IM_BK_NO="
									+ id);
				} else {
					house.setUrlImage(null);
				}
				house.setStatus("0");
				house.setIsCrawler(true);
				house.setUrlCrawler(id);
				
				logger.info("Add to DB:  " + house.toString());
				houseService.add(house);
				logger.info("Add success.....");
			}
		}

	}
}
