/*
 * Copyright (C) 2009-2015 Slava Semushin <slava.semushin@gmail.com>
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.
 */
package ru.mystamps.web.it.step;

import org.springframework.beans.factory.annotation.Autowired;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import ru.mystamps.web.it.page.AddCountryPage;

import static org.junit.Assert.assertThat;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;

public class AddCountrySteps {
	
	// TODO: generate to make them unique
	private static final String VALID_COUNTRY_NAME_EN = "Russia";
	private static final String VALID_COUNTRY_NAME_RU = "Россия";
	
	private static final String INVALID_COUNTRY_NAME_EN = "";
	private static final String INVALID_COUNTRY_NAME_RU = "";
	
	private final AddCountryPage page;
	
	@Autowired
	public AddCountrySteps(AddCountryPage page) {
		this.page = page;
	}
	
	@When("^I open create country page$")
	public void openCreateCountryPage() {
		page.open();
	}
	
	@And("^I fill create country form with valid values$")
	public void fillFormWithValidValues() {
		page.fillForm(VALID_COUNTRY_NAME_EN, VALID_COUNTRY_NAME_RU);
	}
	
	@And("^I fill create country form with invalid values$")
	public void fillFormWithInvalidValues() {
		page.fillForm(INVALID_COUNTRY_NAME_EN, INVALID_COUNTRY_NAME_RU);
	}
	
	@And("^I fill field \"([^\"]*)\" with value \"([^\"]*)\" in create country form$")
	public void fillField(String fieldName, String value) {
		page.fillFieldByName(fieldName, value);
	}
	
	@And("^I submit create country form$")
	public void submitForm() {
		page.submitForm();
	}
	
	@Then("^I see that field \"([^\"]*)\" has error \"([^\"]*)\" in create country form$")
	public void fieldShouldHaveAnError(String fieldName, String errorMessage) {
		assertThat(page.getErrorByFieldName(fieldName), is(equalTo(errorMessage)));
	}
	
	@Then("^I see that field \"([^\"]*)\" has no error in create country form$")
	public void fieldShouldNotHaveAnError(String fieldName) {
		assertThat(page.fieldHasError(fieldName), is(false));
	}
	
}
