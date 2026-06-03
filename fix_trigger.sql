DROP TRIGGER IF EXISTS trg_user_person;
DELIMITER $$
CREATE TRIGGER trg_user_person
BEFORE INSERT ON user
FOR EACH ROW
BEGIN
  IF NEW.person_id IS NULL THEN
    INSERT INTO person (name) VALUES (NEW.username);
    SET NEW.person_id = LAST_INSERT_ID();
  END IF;
END$$
DELIMITER ;
