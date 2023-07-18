alter table evento add column categoria_id bigint not null;

create table categoria (
	id bigint not null auto_increment,
	nome varchar(60) not null,
	primary key (id)
)engine=InnoDB default charset=utf8;

alter table evento add constraint fk_evento_categoria
foreign key (categoria_id) references categoria (id);