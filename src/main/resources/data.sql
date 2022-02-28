-- Initial Data
INSERT INTO HERO(name, description, image_url, real_name) values('Manoliton', 'Hero de los manolitos', 'a.png', 'Javier');
INSERT INTO HERO(name, description, image_url, real_name) values('Javieron', 'Hero de los Javier', 'b.png', 'Manuel');

INSERT INTO SKILL(name, description) values('Fly', 'Can Fly');
INSERT INTO SKILL(name, description) values('Swing', 'Can Swing');
INSERT INTO SKILL(name, description) values('Walk', 'Can Walk');

INSERT INTO HERO_SKILLS  values(1, 1);
INSERT INTO HERO_SKILLS  values(1, 2);
INSERT INTO HERO_SKILLS  values(2, 1);
INSERT INTO HERO_SKILLS  values(2, 3);

