-- Initial Data
INSERT INTO HERO(name, description, image_url, real_name) values('ManolitonTest', 'Hero de los manolitos', 'a.png', 'Javier');
INSERT INTO HERO(name, description, image_url, real_name) values('JavieronTest', 'Hero de los Javier', 'b.png', 'Manuel');

INSERT INTO SKILL(name, description) values('FlyT1', 'Can Fly');
INSERT INTO SKILL(name, description) values('SwingT2', 'Can Swing');
INSERT INTO SKILL(name, description) values('WalkT3', 'Can Walk');

INSERT INTO HERO_SKILLS  values(1, 1);
INSERT INTO HERO_SKILLS  values(1, 2);
INSERT INTO HERO_SKILLS  values(2, 1);
INSERT INTO HERO_SKILLS  values(2, 3);

