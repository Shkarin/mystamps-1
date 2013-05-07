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
package ru.mystamps.web.service;

import java.util.Date;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

import com.google.common.base.Optional;
import com.google.common.base.Strings;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.security.authentication.encoding.PasswordEncoder;

import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

import ru.mystamps.web.entity.User;
import ru.mystamps.web.entity.UsersActivation;
import ru.mystamps.web.dao.JdbcUserDao;
import ru.mystamps.web.dao.UserDao;
import ru.mystamps.web.service.dto.ActivateAccountDto;

import static ru.mystamps.web.entity.User.Role.USER;

@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
	
	private static final Logger LOG = LoggerFactory.getLogger(UserServiceImpl.class);
	
	private final UserDao userDao;
	private final JdbcUserDao jdbcUserDao;
	private final UsersActivationService usersActivationService;
	private final CollectionService collectionService;
	private final PasswordEncoder encoder;
	
	@Override
	@Transactional
	public void registerUser(ActivateAccountDto dto) {
		Validate.isTrue(dto != null, "DTO should be non null");
		Validate.isTrue(dto.getLogin() != null, "Login should be non null");
		Validate.isTrue(dto.getPassword() != null, "Password should be non null");
		Validate.isTrue(dto.getActivationKey() != null, "Activation key should be non null");
		
		String activationKey = dto.getActivationKey();
		UsersActivation activation = usersActivationService.findByActivationKey(activationKey);
		if (activation == null) {
			LOG.warn("Cannot find registration request for activation key '{}'", activationKey);
			return;
		}
		
		String email = activation.getEmail();
		Date registrationDate = activation.getCreatedAt();
		
		String salt = generateSalt();
		
		String hash = encoder.encodePassword(dto.getPassword(), salt);
		Validate.validState(hash != null, "Generated hash must be non null");
		
		String login = dto.getLogin();
		Optional<String> name = Optional.fromNullable(Strings.emptyToNull(dto.getName()));
		
		Date now = new Date();
		
		User user = new User();
		user.setLogin(login);
		user.setRole(USER);
		user.setName(name.or(login));
		user.setEmail(email);
		user.setRegisteredAt(registrationDate);
		user.setActivatedAt(now);
		user.setHash(hash);
		user.setSalt(salt);
		
		userDao.save(user);
		usersActivationService.remove(activation);
		
		collectionService.createCollection(user);
		
		LOG.info(
			"Added user (login='{}', name='{}', activation key='{}')",
			login,
			name.or(login),
			activationKey
		);
	}
	
	@Override
	@Transactional(readOnly = true)
	public User findByLogin(String login) {
		Validate.isTrue(login != null, "Login should be non null");
		
		return userDao.findByLogin(login);
	}
	
	@Override
	@Transactional(readOnly = true)
	public long countByLogin(String login) {
		Validate.isTrue(login != null, "Login should be non null");
		
		return jdbcUserDao.countByLogin(login);
	}
	
	/**
	 * Generate password salt.
	 * @return string which contains letters and numbers in 10 characters length
	 **/
	private static String generateSalt() {
		return RandomStringUtils.randomAlphanumeric(User.SALT_LENGTH);
	}
	
}
