--
-- Auto-generated by Maven, based on values from src/main/resources/test/spring/test-data.properties
--
INSERT INTO users(id, login, name, email, registered_at, activated_at, hash, salt)
VALUES (
	1,
	'@valid_user_login@',
	'@valid_user_name@',
	'coder@rock.home',
	NOW(),
	NOW(),
	'@valid_user_password_hash@',
	'@valid_user_password_salt@'
);
