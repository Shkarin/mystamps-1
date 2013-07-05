/*
 * Copyright (C) 2009-2013 Slava Semushin <slava.semushin@gmail.com>
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
package ru.mystamps.web.service

import javax.inject.Inject

import java.lang.Iterable
import java.util.Date

import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.transaction.annotation.Transactional

import org.apache.commons.lang3.Validate

import ru.mystamps.web.entity.{Country, User}
import ru.mystamps.web.dao.CountryDao
import ru.mystamps.web.service.dto.AddCountryDto

class CountryServiceImpl extends CountryService {
	
	@Inject
	private var countryDao: CountryDao = _
	
	@Transactional
	@PreAuthorize("hasAuthority('ROLE_USER')")
	override def add(dto: AddCountryDto, user: User): Country = {
		Validate.isTrue(dto != null, "DTO should be non null")
		Validate.isTrue(dto.getName() != null, "Country name should be non null")
		Validate.isTrue(user != null, "Current user must be non null")
		
		val country: Country = new Country()
		country.setName(dto.getName())
		
		val now: Date = new Date()
		country.getMetaInfo().setCreatedAt(now)
		country.getMetaInfo().setUpdatedAt(now)
		
		country.getMetaInfo().setCreatedBy(user)
		country.getMetaInfo().setUpdatedBy(user)
		
		return countryDao.save(country)
	}
	
	@Transactional(readOnly = true)
	override def findAll(): Iterable[Country] = {
		return countryDao.findAll()
	}
	
	@Transactional(readOnly = true)
	override def findByName(name: String): Country = {
		Validate.isTrue(name != null, "Name should be non null")
		return countryDao.findByName(name)
	}
	
}