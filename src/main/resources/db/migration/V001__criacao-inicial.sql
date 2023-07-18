create table evento (
	id bigint not null auto_increment,
	nome varchar(60) not null,
    nome_admin varchar(60) not null,
    data datetime not null,
	primary key (id)
)engine=InnoDB default charset=utf8;
