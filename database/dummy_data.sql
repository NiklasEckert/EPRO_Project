INSERT INTO okr.okr_user VALUES (default, 'CO_ADMIN', '$2a$10$QAQrdDfrG.f0tk8yVT3ebeYxh02bGunFE0QdO2ZkuZYIvnjLNO1LC'); /*NIMDA_OC*/
INSERT INTO okr.okr_user VALUES (default, 'BUO_ADMIN', '$2a$10$l5SiZ94PySqqgUY4MxBCKOOrAi6vkpyL.qjps741KjTbp1oTI8scK');/*NIMDA_OUB*/
INSERT INTO okr.okr_user VALUES (default, 'READ_ONLY', '$2a$10$rbeeL63Dj1p2r.l4olXzMe13FixPGdUZavisSFOXYipe0QdjsVd/m');/*YLNO_DAER*/

INSERT INTO okr.okr_roles VALUES (default, 'ROLE_READ_ONLY');
INSERT INTO okr.okr_roles VALUES (default, 'ROLE_CO_OKR_ADMIN');
INSERT INTO okr.okr_roles VALUES (default, 'ROLE_BUO_OKR_ADMIN');

INSERT INTO okr.user_roles VALUES (1, 2);
INSERT INTO okr.user_roles VALUES (2, 3);
INSERT INTO okr.user_roles VALUES (3, 1);

INSERT INTO okr.company_objective VALUES (default, 'O1/2022', 'Attraktiv sein für Werkstudenten', 1);
INSERT INTO okr.company_objective VALUES (default, 'O2/2022', 'Neue Kunden gewinnen', 1);
INSERT INTO okr.company_objective VALUES (default, 'O3/2022', 'Neue Features entworfen', 1);
INSERT INTO okr.company_objective VALUES (default, 'O4/2022', 'Als Umweltfreundlich gelten', 1);

INSERT INTO okr.business_unit VALUES (default, 'Sales Team');
INSERT INTO okr.business_unit VALUES (default, 'Developer Team');

INSERT INTO okr.company_objective_key_result VALUES (default, 'KR1', 'Stellenanzeigen an Universitäten und Hochschulen schalten', 5, 20, 50, 'created', 1, 1);
INSERT INTO okr.company_objective_key_result VALUES (default, 'KR2', 'Praktika anbieten', 7, 10, 75, 'created', 1, 1);
INSERT INTO okr.company_objective_key_result VALUES (default, 'KR3', 'Seminare anbieten', 2, 69, 35, 'created', 1, 1);

INSERT INTO okr.company_objective_key_result VALUES (default, 'KR1', 'Neue Produkte entwickeln', 0, 1, 80, 'created', 2, 1);
INSERT INTO okr.company_objective_key_result VALUES (default, 'KR2', 'Produkte weiterentwickeln', 2, 4, 75, 'created', 2, 1);
INSERT INTO okr.company_objective_key_result VALUES (default, 'KR3', 'Werbeaktionen starten', 11, 10, 60, 'created', 2, 1);

INSERT INTO okr.business_unit_objective VALUES (default, 'O1/2022', 'Team vergrößern', 1, 2);
INSERT INTO okr.business_unit_objective VALUES (default, 'O2/2022', 'Werbeinteraktion vergrößern', 1, 2);
INSERT INTO okr.business_unit_objective VALUES (default, 'O3/2022', 'Neue Produkte beworben', 1, 2);
INSERT INTO okr.business_unit_objective VALUES (default, 'O4/2022', 'Weihnachtsgeschäft verbessern', 1, 2);

INSERT INTO okr.business_unit_objective VALUES (default, 'O1/2022', 'Team vergrößern', 2, 2);
INSERT INTO okr.business_unit_objective VALUES (default, 'O2/2022', 'Neue Produkte entwickeln', 2, 2);
INSERT INTO okr.business_unit_objective VALUES (default, 'O3/2022', 'Produkte finalisieren', 2, 2);
INSERT INTO okr.business_unit_objective VALUES (default, 'O4/2022', 'Neue Techniken ausprobieren', 2, 2);

INSERT INTO okr.business_unit_objective_key_result VALUES (default, 'KR1', 'Anzeigen Uni Mainz', 4, 10, 60, 'created', 1, 1, 2);
INSERT INTO okr.business_unit_objective_key_result VALUES (default, 'KR2', 'Praktika Uni Mainz', 5, 5, 50, 'created', 1, 2, 2);
INSERT INTO okr.business_unit_objective_key_result VALUES (default, 'KR3', 'Seminare Uni Mainz', 3, 3, 45, 'created', 1, 3, 2);

INSERT INTO okr.business_unit_objective_key_result VALUES (default, 'KR1', 'Anzeigen TH Bingen', 1, 10, 30, 'created', 5, 1, 2);
INSERT INTO okr.business_unit_objective_key_result VALUES (default, 'KR2', 'Praktika TH Bingen', 2, 5, 77, 'created', 5, 2, 2);
INSERT INTO okr.business_unit_objective_key_result VALUES (default, 'KR3', 'Seminare TH Bingen', 3, 3, 98, 'created', 5, 3, 2);