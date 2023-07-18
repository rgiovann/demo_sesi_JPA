set foreign_key_checks = 0;
delete from evento;
delete from categoria;
set foreign_key_checks = 1;
alter table evento auto_increment = 1;
alter table categoria auto_increment = 1;

INSERT INTO categoria (id, nome ) VALUES (1, 'Exposição'),
										 (2, 'Feira'),
										 (3, 'Bootcamp'),
										 (4, 'Aula');
										 
INSERT INTO evento (id, nome,nome_admin,data,categoria_id)
			VALUES (null, 'Carros da década de 40 e 50','João da Silva', utc_timestamp,1),
            (null, 'Feira do Notebook','Mauro de Souza', utc_timestamp,2),
            (null, 'Mergulho Java Spring','Helio de Souza',utc_timestamp,3),
            (null, 'Imersão Angular TypeScript','João da Silva', utc_timestamp,3),
            (null, 'Semana de Nivelamento Calouros','Amelia das Flores Campos', utc_timestamp,4);