insert into tab_produtos (nome,descricao,preco_venda_produto,data_inclusao_cadastro) values ('Kindle','Novo Apple Kindle 2.0',499.0,current_timestamp - interval '1 day');
insert into tab_produtos (nome,descricao,preco_venda_produto,data_inclusao_cadastro) values ('IPhone 12','Novo Apple IPhone 12 128GB',4999.0,current_timestamp + interval '10 days');
insert into tab_produtos (nome,descricao,preco_venda_produto,data_inclusao_cadastro) values ('Smart Watch Apple','Novo Apple Smart Watch XPTO',1499.0,current_timestamp - interval '5 days');
insert into tab_produtos (nome,descricao,preco_venda_produto,data_inclusao_cadastro) values ('Fone de Ouvido','Apple Lightning Ear Phone',199.0,current_timestamp + interval '50 day');
--
insert into tab_clientes (nome,sobrenome,sexo, ativo) values ('Edir','Soares de Aguiar','FEMININO','SIM');
--
insert into tab_categorias values (1,'Eletrônicos',null);
insert into tab_categorias values (2,'Cosméticos',null);
insert into tab_categorias values (3,'Celulares',1);
insert into tab_categorias values (4,'Acessórios para Celulares',3);
insert into tab_categorias values (5,'Perfumaria',2);
insert into tab_categorias values (6,'Ferramentas',null);
--
insert into tab_produtos_categorias values (1,1);
insert into tab_produtos_categorias values (2,3);
insert into tab_produtos_categorias values (3,1);
insert into tab_produtos_categorias values (4,4);