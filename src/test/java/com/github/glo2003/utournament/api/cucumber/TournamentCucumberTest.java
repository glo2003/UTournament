package com.github.glo2003.utournament.api.cucumber;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(plugin = {"pretty"}, tags = "", features = "src/test/resources/features")
public class TournamentCucumberTest {
}
