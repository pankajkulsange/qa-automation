package runners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.DataProvider;

@CucumberOptions(
    features = "src/test/resources/features",
    glue = {"steps"},
    plugin = {
        "pretty",
        "html:target/cucumber-reports/html/cucumber-pretty.html",
        "json:target/cucumber-reports/cucumber.json",
        "junit:target/cucumber-reports/junit.xml",
        "rerun:target/cucumber-reports/rerun.txt"
    },
    monochrome = true,
    dryRun = false,
    publish = false
)
public class TestRunner extends AbstractTestNGCucumberTests {
    
    @Override
    @DataProvider(parallel = true)
    public Object[][] scenarios() {
        return super.scenarios();
    }
}
