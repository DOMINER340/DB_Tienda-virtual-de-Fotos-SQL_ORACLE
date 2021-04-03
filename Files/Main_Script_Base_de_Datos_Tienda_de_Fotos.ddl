-- Generado por Oracle SQL Developer Data Modeler 20.2.0.167.1538
--   en:        2020-11-18 22:33:27 COT
--   sitio:      Oracle Database 12cR2
--   tipo:      Oracle Database 12cR2



DROP TABLE a CASCADE CONSTRAINTS;

DROP TABLE bank CASCADE CONSTRAINTS;

DROP TABLE c CASCADE CONSTRAINTS;

DROP TABLE ch CASCADE CONSTRAINTS;

DROP TABLE cpf CASCADE CONSTRAINTS;

DROP TABLE d CASCADE CONSTRAINTS;

DROP TABLE e CASCADE CONSTRAINTS;

DROP TABLE fs CASCADE CONSTRAINTS;

DROP TABLE link CASCADE CONSTRAINTS;

DROP TABLE p CASCADE CONSTRAINTS;

DROP TABLE paypal CASCADE CONSTRAINTS;

DROP TABLE ph CASCADE CONSTRAINTS;

DROP TABLE phxv CASCADE CONSTRAINTS;

DROP TABLE pm CASCADE CONSTRAINTS;

DROP TABLE ptvf CASCADE CONSTRAINTS;

DROP TABLE pxt CASCADE CONSTRAINTS;

DROP TABLE s CASCADE CONSTRAINTS;

DROP TABLE sc CASCADE CONSTRAINTS;

DROP TABLE shopcartph CASCADE CONSTRAINTS;

DROP TABLE sv CASCADE CONSTRAINTS;

DROP TABLE t CASCADE CONSTRAINTS;

DROP TABLE topic CASCADE CONSTRAINTS;

-- predefined type, no DDL - MDSYS.SDO_GEOMETRY

-- predefined type, no DDL - XMLTYPE

--  Es la tabla que contendrá la información de las cuentas/usuarios (Nombre de
--  usuario, contraseña, primer nombre, apellido, fecha de creación de la
--  cuenta, correo de registro y tipo de cuenta (la cual está restringida entre
--  COMPRADOR y FOTÓGRAFO)), para cada uno de estos datos existe un atributo
--  que respectivamente son (userName, password, name, lastName, creationDate,
--  email y type). La tabla está directamente relacionada con Photos, purchase
--  y ShopCarPH, puesto que para cada una de ellas necesitaremos información de
--  la cuenta.
CREATE TABLE a (
    username      VARCHAR2(50 CHAR) NOT NULL,
    password      VARCHAR2(20 CHAR) NOT NULL,
    name          VARCHAR2(50 CHAR) NOT NULL,
    lastname      VARCHAR2(50 CHAR) NOT NULL,
    creationdate  DATE DEFAULT sysdate NOT NULL,
    email         VARCHAR2(50 CHAR) NOT NULL,
    type          VARCHAR2(50 CHAR) NOT NULL,
    ptvf_name     VARCHAR2(50 CHAR) NOT NULL
);

ALTER TABLE a
    ADD CHECK ( email LIKE '%@%.%'
                AND email NOT LIKE '@%'
                AND email NOT LIKE '%@%@%' );

ALTER TABLE a
    ADD CHECK ( type IN ( 'COMPRADOR', 'FOTOGRAFO' ) );

COMMENT ON TABLE a IS
    'Es la tabla que contendrá la información de las cuentas/usuarios (Nombre de usuario, contraseña, primer nombre, apellido, fecha de creación de la cuenta, correo de registro y tipo de cuenta (la cual está restringida entre COMPRADOR y FOTÓGRAFO)), para cada uno de estos datos existe un atributo que respectivamente son (userName, password, name, lastName, creationDate, email y type). La tabla está directamente relacionada con Photos, purchase y ShopCarPH, puesto que para cada una de ellas necesitaremos información de la cuenta.';

ALTER TABLE a ADD CONSTRAINT a_pk PRIMARY KEY ( username );

--  Tabla que posee el id, el nombre de la entidad financiera y la comisión que
--  cobra por su uso (id, financialEName (que sus valores por ahora están
--  restringidos a BBVA y Bancolombia) y commission). Es el Banco o entidad
--  financiera que se encarga de los pagos por cada método de pago.
CREATE TABLE bank (
    id              VARCHAR2(50 CHAR) NOT NULL,
    financialename  VARCHAR2(50 CHAR) NOT NULL,
    commission      NUMBER(3) NOT NULL
);

COMMENT ON TABLE bank IS
    'Tabla que posee el id, el nombre de la entidad financiera y la comisión que cobra por su uso (id, financialEName (que sus valores por ahora están restringidos a BBVA y Bancolombia) y commission). Es el Banco o entidad financiera que se encarga de los pagos por cada método de pago.';

ALTER TABLE bank ADD CONSTRAINT bank_pk PRIMARY KEY ( id );

--  Contiene dos atributos id y name (identificación y nombre), es el país
--  donde se tomó la foto.
CREATE TABLE c (
    id    VARCHAR2(50 CHAR) NOT NULL,
    name  VARCHAR2(50 CHAR) NOT NULL
);

COMMENT ON TABLE c IS
    'Contiene dos atributos id y name (identificación y nombre), es el país donde se tomó la foto.';

ALTER TABLE c ADD CONSTRAINT c_pk PRIMARY KEY ( id );

--  Contiene dos atributos id y name (identificación y nombre), es un personaje
--  o figura reconocida que hace parte de la foto.
CREATE TABLE ch (
    id    VARCHAR2(50 CHAR) NOT NULL,
    name  VARCHAR2(50 CHAR) NOT NULL
);

COMMENT ON TABLE ch IS
    'Contiene dos atributos id y name (identificación y nombre), es un personaje o figura reconocida que hace parte de la foto.';

ALTER TABLE ch ADD CONSTRAINT ch_pk PRIMARY KEY ( id );

--  Es la tabla que contendrá los atributos necesarios para el formulario de
--  pago; identificación (idPayFormat), correo(emailAddr (que tiene un carácter
--  mínimo que es el “@”),Tipo de tarjeta (Cardtype (que están restringidas
--  entre 2 tipos que son VISA y MASTERCARD)),  titular del medio de pago
--  (headLineName), mes de expedición (expMonth) y año de expedición (exp
--  year).  Estará conectada con FormatSelect y con Bank, ya que al
--  seleccionarlo se activará el formato dependiendo del método de pago, y
--  también sabiendo a qué Entidad Financiera corresponde.
CREATE TABLE cpf (
    idpayformat   VARCHAR2(50 CHAR) NOT NULL,
    emailaddr     VARCHAR2(50 CHAR) NOT NULL,
    cardtype      VARCHAR2(50 CHAR) NOT NULL,
    cardnumber    VARCHAR2(16 CHAR) NOT NULL,
    headlinename  VARCHAR2(50 CHAR) NOT NULL,
    expmonth      NUMBER(2) NOT NULL,
    expyear       NUMBER(4) NOT NULL,
    registerdate  DATE DEFAULT sysdate NOT NULL,
    bank_id       VARCHAR2(50 CHAR) NOT NULL
);

ALTER TABLE cpf
    ADD CHECK ( emailaddr LIKE '%@%.%'
                AND emailaddr NOT LIKE '@%'
                AND emailaddr NOT LIKE '%@%@%' );

ALTER TABLE cpf
    ADD CHECK ( cardtype IN ( 'MASTERCARD', 'VISA' ) );

ALTER TABLE cpf ADD CHECK ( length(cardnumber) = 16 );

ALTER TABLE cpf ADD CHECK ( expmonth BETWEEN 1 AND 12 );

COMMENT ON TABLE cpf IS
    'Es la tabla que contendrá los atributos necesarios para el formulario de pago; identificación (idPayFormat), correo(emailAddr (que tiene un carácter mínimo que es el “@”),Tipo de tarjeta (Cardtype (que están restringidas entre 2 tipos que son VISA y MASTERCARD)),  titular del medio de pago (headLineName), mes de expedición (expMonth) y año de expedición (exp year).  Estará conectada con FormatSelect y con Bank, ya que al seleccionarlo se activará el formato dependiendo del método de pago, y también sabiendo a qué Entidad Financiera corresponde.';

ALTER TABLE cpf ADD CONSTRAINT cpf_pk PRIMARY KEY ( idpayformat );

--  Es la tabla que contiene los diferentes descuentos y cómo se aplicarán.
--  Esta tabla contiene los atributos id, name, percent y condition, que
--  respectivamente son identificación, nombre, porcentaje y condición.
--  Únicamente está conectada a purchase, puesto que es la que relacionará las
--  alteraciones del precio.
CREATE TABLE d (
    id         VARCHAR2(50 CHAR) NOT NULL,
    name       VARCHAR2(50 CHAR) NOT NULL,
    percent    NUMBER(3) NOT NULL,
    condition  NUMBER(8) NOT NULL
);

COMMENT ON TABLE d IS
    'Es la tabla que contiene los diferentes descuentos y cómo se aplicarán. Esta tabla contiene los atributos id, name, percent y condition, que respectivamente son identificación, nombre, porcentaje y condición. Únicamente está conectada a purchase, puesto que es la que relacionará las alteraciones del precio.';

ALTER TABLE d ADD CONSTRAINT d_pk PRIMARY KEY ( id );

--  Contiene dos atributos id y name (identificación y nombre), Es el evento
--  conmemorativo en la foto (navidad, Halloween, etc…).
CREATE TABLE e (
    id    VARCHAR2(50 CHAR) NOT NULL,
    name  VARCHAR2(50 CHAR) NOT NULL
);

COMMENT ON TABLE e IS
    'Contiene dos atributos id y name (identificación y nombre), Es el evento conmemorativo en la foto (navidad, Halloween, etc…).';

ALTER TABLE e ADD CONSTRAINT e_pk PRIMARY KEY ( id );

--  Es la tabla de los métodos de pago, la cual posee los formatos de pago
--  respectivos, establecerá qué formato se usará para dado método de pago,
--  contiene dos atributos idFS y formatName (identificación y nombre del
--  formato).
CREATE TABLE fs (
    idfs             VARCHAR2(50 CHAR) NOT NULL,
    formatname       VARCHAR2(50 CHAR) NOT NULL,
    cpf_idpayformat  VARCHAR2(50 CHAR),
    paypal_idpp      VARCHAR2(50 CHAR)
);

ALTER TABLE fs
    ADD CHECK ( formatname IN ( 'CREDITO', 'PAYPAL' ) );

COMMENT ON TABLE fs IS
    'Es la tabla de los métodos de pago, la cual posee los formatos de pago respectivos, establecerá qué formato se usará para dado método de pago, contiene dos atributos idFS y formatName (identificación y nombre del formato).';

ALTER TABLE fs ADD CONSTRAINT fs_pk PRIMARY KEY ( idfs );

--  es la tabla intermedia entre Purchase y Photos, la cual es la que
--  genera/almacena los links respectivos para descargar la(s) fotos, y dentro
--  de la misma almacena el link respectivo de descarga y el contador de
--  descargas por una foto, y así mismo, tendiendo las respectivas referencias
--  a Purchase y Photos, nombre de la foto y el id de la compra.
CREATE TABLE link (
    p_id         VARCHAR2(50 CHAR) NOT NULL,
    ph_name      VARCHAR2(50 CHAR) NOT NULL,
    link         VARCHAR2(50 CHAR) NOT NULL,
    downl_count  NUMBER(8) NOT NULL
);

COMMENT ON TABLE link IS
    'es la tabla intermedia entre Purchase y Photos, la cual es la que genera/almacena los links respectivos para descargar la(s) fotos, y dentro de la misma almacena el link respectivo de descarga y el contador de descargas por una foto, y así mismo, tendiendo las respectivas referencias a Purchase y Photos, nombre de la foto y el id de la compra.';

ALTER TABLE link ADD CONSTRAINT link_pk PRIMARY KEY ( p_id,
                                                      ph_name );

--  es una tabla que será la compra, tendrá los atributos de (identificación y
--  fecha de compra (la cual por defecto es un SYSDATE, para la fecha del
--  día)), expuestos como id y purchaseDate respectivamente. Estará conectada
--  con la tabla discounts, purchaseXTax, PaymentMethod, Shopping Car, Link y
--  Accounts. Ya que necesitaremos las tablas que afectan el precio y la
--  descarga de la(s) fotos, y a qué cuenta pertenece ese registro.
CREATE TABLE p (
    id            VARCHAR2(50 CHAR) NOT NULL,
    purchasedate  DATE DEFAULT sysdate NOT NULL,
    a_username    VARCHAR2(50 CHAR) NOT NULL,
    d_id          VARCHAR2(50 CHAR) NOT NULL,
    sc_id         VARCHAR2(50 CHAR) NOT NULL
);

COMMENT ON TABLE p IS
    'es una tabla que será la compra, tendrá los atributos de (identificación y fecha de compra (la cual por defecto es un SYSDATE, para la fecha del día)), expuestos como id y purchaseDate respectivamente. Estará conectada con la tabla discounts, purchaseXTax, PaymentMethod, Shopping Car, Link y Accounts. Ya que necesitaremos las tablas que afectan el precio y la descarga de la(s) fotos, y a qué cuenta pertenece ese registro.';

ALTER TABLE p ADD CONSTRAINT p_pk PRIMARY KEY ( id );

--  Es la tabla que contiene las credenciales necesarias para los pagos vía
--  Paypal, esta contiene los atributos de idPP y confirmationNumber
--  (Identificación de la cuenta paypal, y número de confirmación), la cual
--  tiene relación con la tabla Bank, que se conectará para saber la Entidad
--  Financiera.
CREATE TABLE paypal (
    idpp                VARCHAR2(50 CHAR) NOT NULL,
    confirmationnumber  VARCHAR2(50 CHAR) NOT NULL,
    bank_id             VARCHAR2(50 CHAR) NOT NULL
);

COMMENT ON TABLE paypal IS
    'Es la tabla que contiene las credenciales necesarias para los pagos vía Paypal, esta contiene los atributos de idPP y confirmationNumber (Identificación de la cuenta paypal, y número de confirmación), la cual tiene relación con la tabla Bank, que se conectará para saber la Entidad Financiera.';

ALTER TABLE paypal ADD CONSTRAINT paypal_pk PRIMARY KEY ( idpp );

--  Es la tabla que contendrá las fotos y su respectiva información como
--  nombre, descripción, tamaño, formato, resolución, fecha de creación, fecha
--  de subida, ubicación, precio, cantidad de vistas y estado (name,
--  description, sizePh, format (Se tienen valores entre BMP	,GIF,JPG y PNG)	,
--  resolution (toma valores entre ALTA, MEDIA y BAJA), date_create, upload_date
--  (fecha que por defecto es SYSDATE, para comprara que “date_create” es un
--  valor menor a la fecha del día que se publica), location, price,
--  amount_views, state) respectivamente, estará conectada además de
--  Project_TVF, accounts y shopping Car, con Country, Character, Topic,
--  Season, Link, PHXV y Events. Que servirán posteriormente para completar la
--  información y tener el registro de visitas de las mismas fotos.
CREATE TABLE ph (
    name          VARCHAR2(50 CHAR) NOT NULL,
    pimage        BLOB DEFAULT empty_blob(),
    description   VARCHAR2(50 CHAR) NOT NULL,
    sizeph        NUMBER(5, 3) NOT NULL,
    format        VARCHAR2(3 CHAR) NOT NULL,
    resolution    VARCHAR2(50 CHAR) NOT NULL,
    date_create   DATE NOT NULL,
    upload_date   DATE DEFAULT sysdate NOT NULL,
    location      VARCHAR2(50 CHAR),
    price         NUMBER(8) NOT NULL,
    amount_views  NUMBER(8) DEFAULT 0 NOT NULL,
    state         VARCHAR2(50 CHAR) DEFAULT 'ACTIVO' NOT NULL,
    a_username    VARCHAR2(50 CHAR) NOT NULL,
    ptvf_name     VARCHAR2(50 CHAR) NOT NULL,
    c_id          VARCHAR2(50 CHAR) NOT NULL,
    ch_id         VARCHAR2(50 CHAR),
    topic_id      VARCHAR2(50 CHAR),
    e_id          VARCHAR2(50 CHAR),
    s_id          VARCHAR2(50 CHAR)
);

ALTER TABLE ph
    ADD CHECK ( sizeph BETWEEN 0.001 AND 5 );

ALTER TABLE ph
    ADD CHECK ( format IN ( 'BMP', 'GIF', 'JPG', 'PNG' ) );

ALTER TABLE ph
    ADD CHECK ( resolution IN ( 'ALTA', 'BAJA', 'MEDIA' ) );

ALTER TABLE ph ADD CHECK ( date_create <= upload_date );

ALTER TABLE ph
    ADD CHECK ( state IN ( 'ACTIVO', 'INACTIVO' ) );

COMMENT ON TABLE ph IS
    'Es la tabla que contendrá las fotos y su respectiva información como nombre, descripción, tamaño, formato, resolución, fecha de creación, fecha de subida, ubicación, precio, cantidad de vistas y estado (name, description, sizePh, format (Se tienen valores entre BMP	,GIF,JPG y PNG)	, resolution (toma valores entre ALTA, MEDIA y BAJA), date_create, upload_date (fecha que por defecto es SYSDATE, para comprara que “date_create” es un valor menor a la fecha del día que se publica), location, price, amount_views, state) respectivamente, estará conectada además de Project_TVF, accounts y shopping Car, con Country, Character, Topic, Season, Link, PHXV y Events. Que servirán posteriormente para completar la información y tener el registro de visitas de las mismas fotos.';

ALTER TABLE ph ADD CONSTRAINT ph_pk PRIMARY KEY ( name );

--  es la tabla intermedia entre Photo y StatsVisitors, la cual posee como
--  atributo propio la cantidad de visualizaciones que se han hecho a la foto
--  por parte de StatsVisitors
CREATE TABLE phxv (
    sv_id        VARCHAR2(50 CHAR) NOT NULL,
    ph_name      VARCHAR2(50 CHAR) NOT NULL,
    amountviews  NUMBER(8) NOT NULL
);

COMMENT ON TABLE phxv IS
    'es la tabla intermedia entre Photo y StatsVisitors, la cual posee como atributo propio la cantidad de visualizaciones que se han hecho a la foto por parte de StatsVisitors';

ALTER TABLE phxv ADD CONSTRAINT phxv_pk PRIMARY KEY ( sv_id,
                                                      ph_name );

--  Es la tabla intermedia que se genera entre FormatSelect y Purchase, la cual
--  contiene el id de Purchase y de FormatSelect.
CREATE TABLE pm (
    p_id     VARCHAR2(50 CHAR) NOT NULL,
    fs_idfs  VARCHAR2(50 CHAR) NOT NULL
);

COMMENT ON TABLE pm IS
    'Es la tabla intermedia que se genera entre FormatSelect y Purchase, la cual contiene el id de Purchase y de FormatSelect.';

ALTER TABLE pm ADD CONSTRAINT pm_pk PRIMARY KEY ( p_id,
                                                  fs_idfs );

--  Es la tabla “cabeza” del proyecto, de esta se desglosan todas las
--  pertenecientes al proyecto, directamente la tabla ‘Accounts’ y ‘Photos’.
--  Contiene como atributo único (name), que es el nombre del proyecto.
CREATE TABLE ptvf (
    name VARCHAR2(50 CHAR) NOT NULL
);

COMMENT ON TABLE ptvf IS
    'Es la tabla “cabeza” del proyecto, de esta se desglosan todas las pertenecientes al proyecto, directamente la tabla ‘Accounts’ y ‘Photos’. Contiene como atributo único (name), que es el nombre del proyecto.';

ALTER TABLE ptvf ADD CONSTRAINT ptvf_pk PRIMARY KEY ( name );

--  Es la conexión entre la compra y los impuestos, obtendrá el nombre del
--  impuesto y lo conectará con la compra.
CREATE TABLE pxt (
    p_id    VARCHAR2(50 CHAR) NOT NULL,
    t_name  VARCHAR2(50 CHAR) NOT NULL
);

COMMENT ON TABLE pxt IS
    'Es la conexión entre la compra y los impuestos, obtendrá el nombre del impuesto y lo conectará con la compra.';

ALTER TABLE pxt ADD CONSTRAINT pxt_pk PRIMARY KEY ( t_name,
                                                    p_id );

--  Contiene dos atributos id y name (identificación y nombre), Es la temporada
--  en la cual fue tomada la foto (SUMMER(Verano), SPRING(Primavera), etc…).
CREATE TABLE s (
    id    VARCHAR2(50 CHAR) NOT NULL,
    name  VARCHAR2(50 CHAR) NOT NULL
);

ALTER TABLE s
    ADD CHECK ( name IN ( 'AUTUMM', 'SPRING', 'SUMMER', 'WINTER' ) );

COMMENT ON TABLE s IS
    'Contiene dos atributos id y name (identificación y nombre), Es la temporada en la cual fue tomada la foto (SUMMER(Verano), SPRING(Primavera), etc…).';

ALTER TABLE s ADD CONSTRAINT s_pk PRIMARY KEY ( id );

--  Es la tabla que contendrá las fotos que el usuario irá deseando comprar,
--  contendrá un único atributo id (identificación), está conectado con photos,
--  accounts y purchase, pues es un puente entre las fotos que el usuario desea
--  y el proceso de compra.
CREATE TABLE sc (
    id          VARCHAR2(50 CHAR) NOT NULL,
    a_username  VARCHAR2(50 CHAR) NOT NULL
);

COMMENT ON TABLE sc IS
    'Es la tabla que contendrá las fotos que el usuario irá deseando comprar, contendrá un único atributo id (identificación), está conectado con photos, accounts y purchase, pues es un puente entre las fotos que el usuario desea y el proceso de compra.';

ALTER TABLE sc ADD CONSTRAINT sc_pk PRIMARY KEY ( id );

--  Es la tabla intermedia entre la tabla Shopping Carts  y Photo, la cual
--  almacena tanto el nombre de la foto, como el id del carrito de compras
--  asociado a las fotos requeridas para la compra.
CREATE TABLE shopcartph (
    ph_name  VARCHAR2(50 CHAR) NOT NULL,
    sc_id    VARCHAR2(50 CHAR) NOT NULL
);

COMMENT ON TABLE shopcartph IS
    'Es la tabla intermedia entre la tabla Shopping Carts  y Photo, la cual almacena tanto el nombre de la foto, como el id del carrito de compras asociado a las fotos requeridas para la compra.';

ALTER TABLE shopcartph ADD CONSTRAINT shopcartph_pk PRIMARY KEY ( ph_name,
                                                                  sc_id );

--  tabla que se refiere al usuario visitante que ve fotos, mas no puede
--  comprarlas sin registrarse, y posee solamente su id.
CREATE TABLE sv (
    id VARCHAR2(50 CHAR) NOT NULL
);

COMMENT ON TABLE sv IS
    'tabla que se refiere al usuario visitante que ve fotos, mas no puede comprarlas sin registrarse, y posee solamente su id.';

ALTER TABLE sv ADD CONSTRAINT sv_pk PRIMARY KEY ( id );

--  Es la tabla que contiene los diferentes impuestos. Contiene los atributos
--  de nombre y porcentaje (name, percent) respectivamente.
CREATE TABLE t (
    name     VARCHAR2(50 CHAR) NOT NULL,
    percent  NUMBER(3) NOT NULL
);

COMMENT ON TABLE t IS
    'Es la tabla que contiene los diferentes impuestos. Contiene los atributos de nombre y porcentaje (name, percent) respectivamente.';

ALTER TABLE t ADD CONSTRAINT t_pk PRIMARY KEY ( name );

--  Contiene dos atributos id y name (identificación y nombre), es la temática
--  principal de la foto (paisaje, retrato, etc…).
CREATE TABLE topic (
    id    VARCHAR2(50 CHAR) NOT NULL,
    name  VARCHAR2(50 CHAR) NOT NULL
);

COMMENT ON TABLE topic IS
    'Contiene dos atributos id y name (identificación y nombre), es la temática principal de la foto (paisaje, retrato, etc…).';

ALTER TABLE topic ADD CONSTRAINT topic_pk PRIMARY KEY ( id );

ALTER TABLE a
    ADD CONSTRAINT a_ptvf_fk FOREIGN KEY ( ptvf_name )
        REFERENCES ptvf ( name );

ALTER TABLE cpf
    ADD CONSTRAINT cpf_bank_fk FOREIGN KEY ( bank_id )
        REFERENCES bank ( id );

ALTER TABLE fs
    ADD CONSTRAINT fs_cpf_fk FOREIGN KEY ( cpf_idpayformat )
        REFERENCES cpf ( idpayformat );

ALTER TABLE fs
    ADD CONSTRAINT fs_paypal_fk FOREIGN KEY ( paypal_idpp )
        REFERENCES paypal ( idpp );

ALTER TABLE link
    ADD CONSTRAINT link_p_fk FOREIGN KEY ( p_id )
        REFERENCES p ( id );

ALTER TABLE link
    ADD CONSTRAINT link_ph_fk FOREIGN KEY ( ph_name )
        REFERENCES ph ( name );

ALTER TABLE p
    ADD CONSTRAINT p_a_fk FOREIGN KEY ( a_username )
        REFERENCES a ( username );

ALTER TABLE p
    ADD CONSTRAINT p_d_fk FOREIGN KEY ( d_id )
        REFERENCES d ( id );

ALTER TABLE p
    ADD CONSTRAINT p_sc_fk FOREIGN KEY ( sc_id )
        REFERENCES sc ( id );

ALTER TABLE paypal
    ADD CONSTRAINT paypal_bank_fk FOREIGN KEY ( bank_id )
        REFERENCES bank ( id );

ALTER TABLE ph
    ADD CONSTRAINT ph_a_fk FOREIGN KEY ( a_username )
        REFERENCES a ( username );

ALTER TABLE ph
    ADD CONSTRAINT ph_c_fk FOREIGN KEY ( c_id )
        REFERENCES c ( id );

ALTER TABLE ph
    ADD CONSTRAINT ph_ch_fk FOREIGN KEY ( ch_id )
        REFERENCES ch ( id );

ALTER TABLE ph
    ADD CONSTRAINT ph_e_fk FOREIGN KEY ( e_id )
        REFERENCES e ( id );

ALTER TABLE ph
    ADD CONSTRAINT ph_ptvf_fk FOREIGN KEY ( ptvf_name )
        REFERENCES ptvf ( name );

ALTER TABLE ph
    ADD CONSTRAINT ph_s_fk FOREIGN KEY ( s_id )
        REFERENCES s ( id );

ALTER TABLE ph
    ADD CONSTRAINT ph_topic_fk FOREIGN KEY ( topic_id )
        REFERENCES topic ( id );

ALTER TABLE phxv
    ADD CONSTRAINT phxv_ph_fk FOREIGN KEY ( ph_name )
        REFERENCES ph ( name );

ALTER TABLE phxv
    ADD CONSTRAINT phxv_sv_fk FOREIGN KEY ( sv_id )
        REFERENCES sv ( id );

ALTER TABLE pm
    ADD CONSTRAINT pm_fs_fk FOREIGN KEY ( fs_idfs )
        REFERENCES fs ( idfs );

ALTER TABLE pm
    ADD CONSTRAINT pm_p_fk FOREIGN KEY ( p_id )
        REFERENCES p ( id );

ALTER TABLE pxt
    ADD CONSTRAINT pxt_p_fk FOREIGN KEY ( p_id )
        REFERENCES p ( id );

ALTER TABLE pxt
    ADD CONSTRAINT pxt_t_fk FOREIGN KEY ( t_name )
        REFERENCES t ( name );

ALTER TABLE sc
    ADD CONSTRAINT sc_a_fk FOREIGN KEY ( a_username )
        REFERENCES a ( username );

ALTER TABLE shopcartph
    ADD CONSTRAINT shopcartph_ph_fk FOREIGN KEY ( ph_name )
        REFERENCES ph ( name );

ALTER TABLE shopcartph
    ADD CONSTRAINT shopcartph_sc_fk FOREIGN KEY ( sc_id )
        REFERENCES sc ( id );



-- Informe de Resumen de Oracle SQL Developer Data Modeler: 
-- 
-- CREATE TABLE                            22
-- CREATE INDEX                             0
-- ALTER TABLE                             61
-- CREATE VIEW                              0
-- ALTER VIEW                               0
-- CREATE PACKAGE                           0
-- CREATE PACKAGE BODY                      0
-- CREATE PROCEDURE                         0
-- CREATE FUNCTION                          0
-- CREATE TRIGGER                           0
-- ALTER TRIGGER                            0
-- CREATE COLLECTION TYPE                   0
-- CREATE STRUCTURED TYPE                   0
-- CREATE STRUCTURED TYPE BODY              0
-- CREATE CLUSTER                           0
-- CREATE CONTEXT                           0
-- CREATE DATABASE                          0
-- CREATE DIMENSION                         0
-- CREATE DIRECTORY                         0
-- CREATE DISK GROUP                        0
-- CREATE ROLE                              0
-- CREATE ROLLBACK SEGMENT                  0
-- CREATE SEQUENCE                          0
-- CREATE MATERIALIZED VIEW                 0
-- CREATE MATERIALIZED VIEW LOG             0
-- CREATE SYNONYM                           0
-- CREATE TABLESPACE                        0
-- CREATE USER                              0
-- 
-- DROP TABLESPACE                          0
-- DROP DATABASE                            0
-- 
-- REDACTION POLICY                         0
-- 
-- ORDS DROP SCHEMA                         0
-- ORDS ENABLE SCHEMA                       0
-- ORDS ENABLE OBJECT                       0
-- 
-- ERRORS                                   0
-- WARNINGS                                 0




/*se ingresa el nombre del proyecto*/
Insert into ptvf values ('tvf');

/*se ingresa los bancos*/
insert into bank values (1, 'BBVA',2);
insert into bank values (2, 'BANCOLOMBIA',3);

/* se agregan las cuentas  */
Insert into A values('MiguelA','321ssap', 'Miguel', 'Uribe', DEFAULT, 
                    'miguel.uribe@javeriana.edu.co', 'FOTOGRAFO', 'tvf');

Insert into A values('PabloR','pa123ss', 'Pablo', 'Rodriguez', DEFAULT,  
                    'pablo.rodriguezg@javeriana.edu.co', 'FOTOGRAFO', 'tvf');

Insert into A values('Stebanv','123456789', 'Steban', 'vanegas', DEFAULT, 
                    'steban_vanegasc@javeriana.edu', 'COMPRADOR', 'tvf');

Insert into A values('AlvaroQ' ,'1pa2ss3', 'Alvaro', 'Quintero',DEFAULT , 
                    'quintero.alvaro@javeriana.edu.co', 'COMPRADOR', 'tvf');

/* se agregan l0s paises  */
Insert into c values(1, 'Colombia');
Insert into c values(2, 'Argentina');
Insert into c values(3, 'Mexico');
Insert into c values(4, 'Canada');
Insert into c values(5, 'España');
Insert into c values(6, 'Italia');
Insert into c values(7, 'Francia');
Insert into c values(8, 'Rusia');
Insert into c values(9, 'Japon');
Insert into c values(10, 'Egipto');

/* se agregan los personajes  */
Insert into ch values(1, 'Eiffel Tower');
Insert into ch values(2, 'sphinx');
Insert into ch values(3, 'Statue of Liberty');
Insert into ch values(4, 'Vladimir Putin');
Insert into ch values(5, 'Christ the Redeemer');

/* se agregan los descuentos  */
Insert into d values (1,'between 1 and 3 photos', 5, 3);
Insert into d values (2,'between 4 and 10 photos', 10, 10);
Insert into d values (3,'more than 10', 15, 11);

/* se agregan los eventos  */

Insert into e values(1, 'Christmas');
Insert into e values(2, 'Halloween');
Insert into e values(3, 'Easter');
Insert into e values(4, 'Valentines Day');
Insert into e values(5, 'saint patricks day');

/* se agregan los temas*/
Insert into topic values(1, 'Landscape');
Insert into topic values(2, 'Portrait');
Insert into topic values(3, 'Sports');
Insert into topic values(4, 'Animals');
Insert into topic values(5, 'Architecture');

/* se agregan los impuestos*/
Insert into t values('rete fuente', 7);
Insert into t values('IVA', 16);
Insert into t values('ICA', 6) ;

/* se agregan las temporadas*/
insert into S values (1, 'SPRING');
insert into S values (2,'SUMMER');
insert into S values (3, 'AUTUMM');
insert into S values (4, 'WINTER');

/* se agregan las fotos */
Insert into ph values ('christmas in Otawa',DEFAULT,'parliament during christmas', 
            3,'JPG','MEDIA',
            TO_DATE('2013/12/23 8:30:25', 'YYYY/MM/DD HH:MI:SS'),
            DEFAULT,'Parliament buildings',15000, 4, DEFAULT,
            'PabloR','tvf',4,NULL,5,1,4);           

Insert into ph values ('Halloween night',DEFAULT,'colombian Streets - Halloween night',
            5,'PNG','ALTA',TO_DATE('2018/10/31 9:30:25', 'YYYY/MM/DD HH:MI:SS') ,
            DEFAULT, 'Downtown of Bogota',20000,5, DEFAULT,
            'PabloR','tvf', 1,NULL,3,2,3);

Insert into ph values ('Saint valentines day in Paris',DEFAULT,'a couple near to Eiffel tower ', 
            3,'JPG','BAJA',TO_DATE('2019/04/14 10:30:25', 'YYYY/MM/DD HH:MI:SS') , 
            DEFAULT, 'Eiffel Tower',25000, 3, DEFAULT,
            'MiguelA','tvf',7,1,2,4,1);
            
Insert into ph values ('Moon Quick Flash',DEFAULT,'exposure time 5,1 Seg ', 
            4,'JPG','ALTA',TO_DATE('2012/12/12 12:12:12', 'YYYY/MM/DD HH:MI:SS') , 
            DEFAULT, NULL,50000, 0, DEFAULT,
            'MiguelA','tvf',7,null,null,null,1);            
   
Insert into ph values ('Leaves',DEFAULT,'Leaves in Autum', 
            3,'PNG','BAJA',TO_DATE('2020/10/1 09:15:02', 'YYYY/MM/DD HH:MI:SS') , 
            DEFAULT, 'Canada Park',10000, 1, DEFAULT,
            'MiguelA','tvf',4,NULL,NULL,NULL,3);

Insert into ph values ('Spider-man',DEFAULT,'Our friendly neighboor Spider-Man', 
            3,'PNG','ALTA',TO_DATE('2020/11/1 12:31:01', 'YYYY/MM/DD HH:MI:SS') , 
            DEFAULT, 'Canada',30000, 30, DEFAULT,
            'PabloR','tvf',4,NULL,2,NULL,1);

Insert into ph values ('The Weekend',DEFAULT,'The Weekend in concert', 
            2,'JPG','ALTA',TO_DATE('2014/09/2 21:34:32', 'YYYY/MM/DD HH:MI:SS') , 
            DEFAULT, 'MOVISTAR ARENA',100000, 55, DEFAULT,
            'PabloR','tvf',1,NULL,2,NULL,2);

Insert into ph values ('Owl',DEFAULT,'Nature Owl', 
            3,'JPG','MEDIA',TO_DATE('2019/10/12 06:45:22', 'YYYY/MM/DD HH:MI:SS') , 
            DEFAULT, 'Canada Park',10000, 6, DEFAULT,
            'PabloR','tvf',4,NULL,4,NULL,1);


Insert into ph values ('My Birds',DEFAULT,'My friendly birds', 
            4,'PNG','ALTA',TO_DATE('2010/02/1 08:15:02', 'YYYY/MM/DD HH:MI:SS') , 
            DEFAULT, 'Canada Park',10000, 1, DEFAULT,
            'MiguelA','tvf',4,NULL,4,NULL,1);

Insert into ph values ('Saturday Night',DEFAULT,'Having some fun with oir frinds', 
            1,'JPG','BAJA',TO_DATE('2008/10/14 01:25:02', 'YYYY/MM/DD HH:MI:SS') , 
            DEFAULT, 'My House',10000, 1, DEFAULT,
            'MiguelA','tvf',5,NULL,2,5,1);

Insert into ph values ('My park',DEFAULT,'Another park from My Beutiful Canada', 
            3,'PNG','MEDIA',TO_DATE('2020/10/1 07:31:02', 'YYYY/MM/DD HH:MI:SS') , 
            DEFAULT, 'Canada Park',10000, 1, DEFAULT,
            'MiguelA','tvf',4,NULL,1,NULL,2);

Insert into ph values ('Black in the Hole',DEFAULT,'Black Hole', 
            2,'JPG','BAJA',TO_DATE('2019/10/1 00:15:02', 'YYYY/MM/DD HH:MI:SS') , 
            DEFAULT, 'Universe',1000000, 100, DEFAULT,
            'MiguelA','tvf',8,NULL,1,NULL,3);

            
/* se agregan los carritos de compra  */
Insert into sc values (1, 'Stebanv');
Insert into sc values (2, 'MiguelA');


/* se agrega los inserts de la tabla intermedia entre carrito_De_compras y id_fotos  */
Insert into shopcartph values ( 'christmas in Otawa', 1);
Insert into shopcartph values ( 'Halloween night' ,1);
Insert into shopcartph values ('Saint valentines day in Paris',1 );
Insert into shopcartph values ( 'Halloween night',2);


/* se agregan las compras  */
Insert into p values (1, DEFAULT, 'Stebanv', 1, 1);
Insert into p values (2, DEFAULT, 'MiguelA', 1, 2);


/* se agregan las compras x impuestos */
Insert into pxt values (1,'rete fuente');
Insert into pxt values (1,'IVA');
Insert into pxt values (1,'ICA');

Insert into pxt values (2,'IVA');
Insert into pxt values (2,'ICA');


/* se agrega un formulario de pago por credito */
Insert into cpf values (1, 'steban_vanegasc@javeriana.edu.co', 'VISA',
                        1234567891234567, 'Steban f vanegas c',
                        7,2023, DEFAULT,1 );


/* se agrega un formulario de pago por efectivo */
Insert into paypal values (1, 987654321,2);


/* se agrega un formulario de pago por efectivo */
Insert INTO fs values (1,'CREDITO',1,NULL);
Insert INTO fs values (2,'PAYPAL',NULL,1);

/* se agregan datos  al tabla intermedia metodo de pago */
Insert INTO pm values (1,1);
Insert INTO pm values (2,2);

/* se agregan datos  al tabla StatsVisitor   */
insert into SV values (1);
insert into SV values (2);
insert into SV values (3);


/* se agregan datos  al tabla PHXV   */
insert into PHXV values (1,'Halloween night', 1);
insert into PHXV values (2, 'Halloween night', 2);
insert into PHXV values (3, 'christmas in Otawa', 3);
insert into PHXV values (1, 'Saint valentines day in Paris', 2);

/* se agregan datos  al tabla Link   */

insert into Link values (1 , 'christmas in Otawa' ,'www.tvfBD.com/christmas', 1);
insert into Link values (1 ,'Halloween night','www.tvfBD.com/Halloween', 2);   
insert into Link values (1 ,'Saint valentines day in Paris','www.tvfBD.com/valentines', 3);   
insert into Link values (2 ,'Halloween night', 'www.tvfBD.com/night', 3); 



