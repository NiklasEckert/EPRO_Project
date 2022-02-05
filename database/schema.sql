CREATE SCHEMA IF NOT EXISTS okr;

CREATE TABLE IF NOT EXISTS okr.company_objective (
    id SERIAL PRIMARY KEY,
    name text NOT NULL UNIQUE CHECK (name ~* 'O[1-4]{1}/[0-9]{4}'),
    description text NOT NULL
);

CREATE TABLE IF NOT EXISTS okr.business_unit (
  id SERIAL PRIMARY KEY,
  name text NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS okr.company_objective_key_result (
    id SERIAL PRIMARY KEY,
    name text NOT NULL CHECK (name ~* 'KR[1-5]{1}'),
    description text NOT NULL,
    current numeric NOT NULL,
    goal numeric NOT NULL,
    confidence_level integer CHECK (confidence_level >= 0 AND confidence_level <= 100),
    comment text,
    company_objective_id integer REFERENCES okr.company_objective (id),
    UNIQUE (name, company_objective_id)
);

CREATE TABLE IF NOT EXISTS okr.business_unit_objective (
  id SERIAL PRIMARY KEY,
  name text NOT NULL CHECK (name ~* 'O[1-4]{1}/[0-9]{4}'),
  description text NOT NULL,
  business_unit_id integer REFERENCES okr.business_unit (id) NOT NULL,
  UNIQUE (name, business_unit_id)
);

CREATE TABLE IF NOT EXISTS okr.h_company_objective_key_result (
    id SERIAL PRIMARY KEY,
    co_kr_id integer REFERENCES okr.company_objective_key_result (id),
    name text NOT NULL CHECK (name ~* 'KR[1-5]{1}'),
    description text NOT NULL,
    current numeric NOT NULL,
    goal numeric NOT NULL,
    confidence_level integer CHECK (confidence_level >= 0 AND confidence_level <= 100),
    comment text,
    company_objective_id integer REFERENCES okr.company_objective (id)
);

CREATE TABLE IF NOT EXISTS okr.business_unit_objective_key_result (
  id SERIAL PRIMARY KEY,
  name text NOT NULL CHECK (name ~* 'KR[1-5]{1}'),
  description text NOT NULL,
  current numeric NOT NULL,
  goal numeric NOT NULL,
  confidence_level integer CHECK (confidence_level >= 0 AND confidence_level <= 100),
  comment text,
  business_unit_objective_id integer REFERENCES okr.business_unit_objective (id),
  co_key_result_id integer REFERENCES okr.company_objective_key_result (id),
  UNIQUE (name, business_unit_objective_id)
);

CREATE TABLE IF NOT EXISTS okr.h_business_unit_objective_key_result (
  id SERIAL PRIMARY KEY,
  bo_kr_id integer REFERENCES okr.business_unit_objective_key_result (id),
  name text NOT NULL CHECK (name ~* 'KR[1-5]{1}'),
  description text NOT NULL,
  current numeric NOT NULL,
  goal numeric NOT NULL,
  confidence_level integer CHECK (confidence_level >= 0 AND confidence_level <= 100),
  comment text,
  business_unit_objective_id integer REFERENCES okr.business_unit_objective (id),
  co_key_result_id integer REFERENCES okr.company_objective_key_result (id)
);

CREATE OR REPLACE FUNCTION check_co_key_result_amount() RETURNS trigger AS $check_co_key_result_amount$
    DECLARE
        key_result_amount integer;
        company_objective_name text;
    BEGIN
        SELECT count(*)
        INTO key_result_amount
        FROM okr.company_objective_key_result
        WHERE company_objective_id = NEW.company_objective_id;

        SELECT name
        INTO company_objective_name
        FROM okr.company_objective
        WHERE okr.company_objective.id = NEW.company_objective_id;

        IF key_result_amount >= 5 THEN
            RAISE EXCEPTION 'Company objective % already has 5 key results.', company_objective_name;
        END IF;

        RETURN NEW;
    END;
$check_co_key_result_amount$ LANGUAGE plpgsql;

CREATE TRIGGER check_co_key_result_amount BEFORE INSERT OR UPDATE ON okr.company_objective_key_result
    FOR EACH ROW EXECUTE PROCEDURE check_co_key_result_amount();

CREATE OR REPLACE FUNCTION check_bu_key_result_amount() RETURNS trigger AS $check_bu_key_result_amount$
    DECLARE
        key_result_amount integer;
        business_unit_objective_name text;
    BEGIN
        SELECT count(*)
        INTO key_result_amount
        FROM okr.business_unit_objective_key_result
        WHERE business_unit_objective_id = NEW.business_unit_objective_id;

        SELECT name
        INTO business_unit_objective_name
        FROM okr.business_unit_objective
        WHERE okr.business_unit_objective.id = NEW.business_unit_objective_id;

        IF key_result_amount >= 5 THEN
            RAISE EXCEPTION 'Business unit objective % already has 5 key results.', business_unit_objective_name;
        END IF;

        RETURN NEW;
    END;
$check_bu_key_result_amount$ LANGUAGE plpgsql;

CREATE TRIGGER check_bu_key_result_amount BEFORE INSERT OR UPDATE ON okr.business_unit_objective_key_result
    FOR EACH ROW EXECUTE PROCEDURE check_bu_key_result_amount();

-- CREATE OR REPLACE FUNCTION set_current_and_goal_to_zero() RETURNS trigger AS $set_current_and_goal_to_zero$
--     BEGIN
--         NEW.current := 0;
--         NEW.goal := 0;
--
--         RETURN NEW;
--     END;
-- $set_current_and_goal_to_zero$ LANGUAGE plpgsql;
--
-- CREATE TRIGGER set_current_and_goal_to_zero BEFORE INSERT ON okr.company_objective_key_result
--     FOR EACH ROW EXECUTE PROCEDURE set_current_and_goal_to_zero();
--
-- CREATE OR REPLACE FUNCTION calculate_current_and_goal() RETURNS trigger AS $calculate_current_and_goal$
--     DECLARE
--         cur numeric;
--         gol numeric;
--     BEGIN
--         SELECT sum(current)
--         INTO cur
--         FROM okr.business_unit_objective_key_result
--         WHERE co_key_result_id = NEW.co_key_result_id;
--
--         SELECT sum(goal)
--         INTO gol
--         FROM okr.business_unit_objective_key_result
--         WHERE co_key_result_id = NEW.co_key_result_id;
--
--         UPDATE okr.company_objective_key_result
--         SET current = (cur),
--             goal = (gol)
--         WHERE id = NEW.co_key_result_id;
--
--         RETURN NEW;
--     END;
-- $calculate_current_and_goal$ LANGUAGE plpgsql;
--
-- CREATE TRIGGER calculate_current_and_goal AFTER INSERT OR UPDATE ON okr.business_unit_objective_key_result
--     FOR EACH ROW EXECUTE PROCEDURE calculate_current_and_goal();

CREATE OR REPLACE FUNCTION co_kr_update_to_history() RETURNS trigger AS $co_kr_update_to_history$
    BEGIN
        INSERT INTO okr.h_company_objective_key_result VALUES (
            default,
            OLD.id,
            OLD.name,
            OLD.description,
            OLD.current,
            OLD.goal,
            OLD.confidence_level,
            OLD.comment,
            OLD.company_objective_id
        );

        RETURN NEW;
    END;
$co_kr_update_to_history$ LANGUAGE plpgsql;

CREATE TRIGGER co_kr_update_to_history BEFORE UPDATE ON okr.company_objective_key_result
    FOR EACH ROW EXECUTE PROCEDURE co_kr_update_to_history();

CREATE OR REPLACE FUNCTION bu_kr_update_to_history() RETURNS trigger AS $bu_kr_update_to_history$
    BEGIN
        INSERT INTO okr.h_business_unit_objective_key_result VALUES (
            default,
            OLD.id,
            OLD.name,
            OLD.description,
            OLD.current,
            OLD.goal,
            OLD.confidence_level,
            OLD.comment,
            OLD.business_unit_objective_id,
            OLD.co_key_result_id
        );

        RETURN NEW;
    END;
$bu_kr_update_to_history$ LANGUAGE plpgsql;

CREATE TRIGGER bu_kr_update_to_history BEFORE UPDATE ON okr.business_unit_objective_key_result
    FOR EACH ROW EXECUTE PROCEDURE bu_kr_update_to_history();