/*
 * Copyright (C) 2009-2014 Slava Semushin <slava.semushin@gmail.com>
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
package ru.mystamps.web.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import ru.mystamps.web.service.dto.LinkEntityDto;

class LinkEntityDtoRowMapper implements RowMapper<LinkEntityDto> {
	
	@Override
	public LinkEntityDto mapRow(ResultSet resultSet, int i) throws SQLException {
		Integer collectionId  = resultSet.getInt("collection_id");
		String collectionSlug = resultSet.getString("collection_slug");
		String ownerName      = resultSet.getString("owner_name");
		
		return new LinkEntityDto(collectionId, collectionSlug, ownerName);
	}
	
}
