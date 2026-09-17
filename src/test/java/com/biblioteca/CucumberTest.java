package com.biblioteca;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "src/test/resources/features",
        glue = "com.biblioteca",
        plugin = {
                "pretty",
                "html:target/cucumber-reports.html",
                "junit:target/surefire-reports/TEST-cucumber.xml"
        }
)
public class CucumberTest {
}