--
-- Auto-generated by Maven, based on values from src/main/resources/test/spring/test-data.properties
--

-- Used only in WhenAnonymousUserActivateAccount
INSERT INTO users_activation(act_key, email, created_at) VALUES
	('@not_activated_user1_act_key@', 'test1@example.org', NOW()),
	('@not_activated_user2_act_key@', 'test2@example.org', NOW());
