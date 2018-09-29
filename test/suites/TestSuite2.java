package hotel.test.suites;

import org.junit.platform.runner.JUnitPlatform;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.runner.RunWith;

@RunWith(JUnitPlatform.class)
@SelectPackages({"hotel.test.service", "hotel.test.entities", "hotel.test.integrationtest"})
public class TestSuite2 {
}
