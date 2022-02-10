INSERT INTO okr.okr_user VALUES (default, 'ADMIN1', '1NIMDA');

INSERT INTO okr.okr_roles VALUES (default, 'READ_ONLY');
INSERT INTO okr.okr_roles VALUES (default, 'CO_OKR_ADMIN');
INSERT INTO okr.okr_roles VALUES (default, 'BUO_OKR_ADMIN');

INSERT INTO okr.user_roles VALUES (1, 1);
INSERT INTO okr.user_roles VALUES (1, 2);
INSERT INTO okr.user_roles VALUES (1, 3);

INSERT INTO okr.company_objective VALUES (default, 'O1/2022', 'Attraktiv sein für Werkstudenten', 1);
INSERT INTO okr.company_objective VALUES (default, 'O2/2022', 'Neue Kunden gewinnen', 1);
INSERT INTO okr.company_objective VALUES (default, 'O3/2022', 'Neue Features entworfen', 1);
INSERT INTO okr.company_objective VALUES (default, 'O4/2022', 'Als Umweltfreundlich gelten', 1);

INSERT INTO okr.business_unit VALUES (default, 'Sales Team');
INSERT INTO okr.business_unit VALUES (default, 'Developer Team');

INSERT INTO okr.company_objective_key_result VALUES (default, 'KR1', 'Stellenanzeigen an Universitäten und Hochschulen schalten', 0, 0, 50, 'created', 1, 1);
INSERT INTO okr.company_objective_key_result VALUES (default, 'KR2', 'Praktika anbieten', 0, 0, 75, 'created', 1, 1);
INSERT INTO okr.company_objective_key_result VALUES (default, 'KR3', 'Seminare anbieten', 0, 0, 35, 'created', 1, 1);

INSERT INTO okr.company_objective_key_result VALUES (default, 'KR1', 'Neue Produkte entwickeln', 0, 0, 80, 'created', 2, 1);
INSERT INTO okr.company_objective_key_result VALUES (default, 'KR2', 'Produkte weiterentwickeln', 0, 0, 75, 'created', 2, 1);
INSERT INTO okr.company_objective_key_result VALUES (default, 'KR3', 'Werbeaktionen starten', 0, 0, 60, 'created', 2, 1);

INSERT INTO okr.business_unit_objective VALUES (default, 'O1/2022', 'Team vergrößern', 1, 1);
INSERT INTO okr.business_unit_objective VALUES (default, 'O2/2022', 'Werbeinteraktion vergrößern', 1, 1);
INSERT INTO okr.business_unit_objective VALUES (default, 'O3/2022', 'Neue Produkte beworben', 1, 1);
INSERT INTO okr.business_unit_objective VALUES (default, 'O4/2022', 'Weihnachtsgeschäft verbessern', 1, 1);

INSERT INTO okr.business_unit_objective VALUES (default, 'O1/2022', 'Team vergrößern', 2, 1);
INSERT INTO okr.business_unit_objective VALUES (default, 'O2/2022', 'Neue Produkte entwickeln', 2, 1);
INSERT INTO okr.business_unit_objective VALUES (default, 'O3/2022', 'Produkte finalisieren', 2, 1);
INSERT INTO okr.business_unit_objective VALUES (default, 'O4/2022', 'Neue Techniken ausprobieren', 2, 1);

INSERT INTO okr.business_unit_objective_key_result VALUES (default, 'KR1', 'Anzeigen Uni Mainz', 0, 10, 60, 'created', 1, 1, 1);
INSERT INTO okr.business_unit_objective_key_result VALUES (default, 'KR2', 'Praktika Uni Mainz', 0, 5, 50, 'created', 1, 2, 1);
INSERT INTO okr.business_unit_objective_key_result VALUES (default, 'KR3', 'Seminare Uni Mainz', 0, 3, 45, 'created', 1, 3, 1);

INSERT INTO okr.business_unit_objective_key_result VALUES (default, 'KR1', 'Anzeigen TH Bingen', 0, 10, 30, 'created', 5, 1, 1);
INSERT INTO okr.business_unit_objective_key_result VALUES (default, 'KR2', 'Praktika TH Bingen', 0, 5, 77, 'created', 5, 2, 1);
INSERT INTO okr.business_unit_objective_key_result VALUES (default, 'KR3', 'Seminare TH Bingen', 0, 3, 98, 'created', 5, 3, 1);