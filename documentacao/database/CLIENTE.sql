create table CLIENTE
(
    ID_CLIENTE NUMBER default 19 not null constraint CLIENTE_PK primary key,
    NM_CLIENTE VARCHAR2(255)
)
/

create index "cliente_NM_CLIENTE_index"
    on CLIENTE (NM_CLIENTE)
/

INSERT INTO CLIENTE (ID_CLIENTE, NM_CLIENTE) VALUES (1, 'Benefrancis do Nascimento');
INSERT INTO CLIENTE (ID_CLIENTE, NM_CLIENTE) VALUES (2, 'Bruno Sudré do Nascimento');
INSERT INTO CLIENTE (ID_CLIENTE, NM_CLIENTE) VALUES (3, 'Erick Sudré do Nascimento');
INSERT INTO CLIENTE (ID_CLIENTE, NM_CLIENTE) VALUES (4, 'Davi Lucca Sudré do Nascimento');

