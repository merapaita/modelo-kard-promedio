-- CREACIÓN DE MODULOS
INSERT INTO module (name, base_path) VALUES ('GRUPO', '/agrupo');
INSERT INTO module (name, base_path) VALUES ('CLASE', '/aclase');
INSERT INTO module (name, base_path) VALUES ('FAMILIA', '/afamilia');
INSERT INTO module (name, base_path) VALUES ('ARTICULO', '/articulo');
INSERT INTO module (name, base_path) VALUES ('CUSTOMER', '/customers');
INSERT INTO module (name, base_path) VALUES ('AUTH', '/auth');

-- CREACIÓN DE MÓDULO PARA RETO SECCION 11
--INSERT INTO module (name, base_path) VALUES ('PERMISSION', '/permissions');

-- CREACIÓN DE OPERACIONES
INSERT INTO operation (name, path, http_method, permit_all, module_id) VALUES ('READ_ALL_GROUPS','', 'GET', false, 1);
INSERT INTO operation (name, path, http_method, permit_all, module_id) VALUES ('READ_ALL_GROUPS_PAGE','', 'GET', false, 1);
INSERT INTO operation (name, path, http_method, permit_all, module_id) VALUES ('READ_ONE_GROUP','/[0-9]*', 'GET', false, 1);
INSERT INTO operation (name, path, http_method, permit_all, module_id) VALUES ('CREATE_ONE_GROUP','', 'POST', false, 1);
INSERT INTO operation (name, path, http_method, permit_all, module_id) VALUES ('UPDATE_ONE_GROUP','/[0-9]*', 'PUT', false, 1);
INSERT INTO operation (name, path, http_method, permit_all, module_id) VALUES ('DELETE_ONE_GROUP','/[0-9]*', 'PUT', false, 1);

INSERT INTO operation (name, path, http_method, permit_all, module_id) VALUES ('READ_ALL_CLASS','', 'GET', false, 2);
INSERT INTO operation (name, path, http_method, permit_all, module_id) VALUES ('READ_ALL_CLASS_PAGE','', 'GET', false, 2);
INSERT INTO operation (name, path, http_method, permit_all, module_id) VALUES ('READ_ONE_CLASS','/[0-9]*', 'GET', false, 2);
INSERT INTO operation (name, path, http_method, permit_all, module_id) VALUES ('CREATE_ONE_CLASS','', 'POST', false, 2);
INSERT INTO operation (name, path, http_method, permit_all, module_id) VALUES ('UPDATE_ONE_CLASS','/[0-9]*', 'PUT', false, 2);
INSERT INTO operation (name, path, http_method, permit_all, module_id) VALUES ('DISABLE_ONE_CLASS','/[0-9]*', 'PUT', false, 2);

INSERT INTO operation (name, path, http_method, permit_all, module_id) VALUES ('READ_ALL','', 'GET', false, 5);
INSERT INTO operation (name, path, http_method, permit_all, module_id) VALUES ('REGISTER_ONE','', 'POST', true, 5);

INSERT INTO operation (name, path, http_method, permit_all, module_id) VALUES ('AUTHENTICATE','/authenticate', 'POST', true, 6);
INSERT INTO operation (name, path, http_method, permit_all, module_id) VALUES ('VALIDATE-TOKEN','/validate-token', 'GET', true, 6);
INSERT INTO operation (name, path, http_method, permit_all, module_id) VALUES ('READ_MY_PROFILE','/profile','GET', false, 6);
INSERT INTO operation (name, path, http_method, permit_all, module_id) VALUES ('LOGOUT','/logout','POST', true, 6);

-- CREACIÓN DE OPERACIONES DE MÓDULO PARA RETO SECCION 11
--INSERT INTO operation (name, path, http_method, permit_all, module_id) VALUES ('READ_ALL_PERMISSIONS','','GET', false, 5);
--INSERT INTO operation (name, path, http_method, permit_all, module_id) VALUES ('READ_ONE_PERMISSION','/[0-9]*','GET', false, 5);
--INSERT INTO operation (name, path, http_method, permit_all, module_id) VALUES ('CREATE_ONE_PERMISSION','','POST', false, 5);
--INSERT INTO operation (name, path, http_method, permit_all, module_id) VALUES ('DELETE_ONE_PERMISSION','/[0-9]*','DELETE', false, 5);

-- CREACIÓN DE ROLES
INSERT INTO role (name) VALUES ('CUSTOMER');
INSERT INTO role (name) VALUES ('ASSISTANT_ADMINISTRATOR');
INSERT INTO role (name) VALUES ('ADMINISTRATOR');

-- CREACIÓN DE PERMISOS
INSERT INTO granted_permission (role_id, operation_id) VALUES (1, 17);

INSERT INTO granted_permission (role_id, operation_id) VALUES (2, 1);
INSERT INTO granted_permission (role_id, operation_id) VALUES (2, 2);
INSERT INTO granted_permission (role_id, operation_id) VALUES (2, 3);
INSERT INTO granted_permission (role_id, operation_id) VALUES (2, 4);

INSERT INTO granted_permission (role_id, operation_id) VALUES (2, 7);
INSERT INTO granted_permission (role_id, operation_id) VALUES (2, 8);
INSERT INTO granted_permission (role_id, operation_id) VALUES (2, 9);
INSERT INTO granted_permission (role_id, operation_id) VALUES (2, 10);
INSERT INTO granted_permission (role_id, operation_id) VALUES (2, 17);
INSERT INTO granted_permission (role_id, operation_id) VALUES (2, 18);

INSERT INTO granted_permission (role_id, operation_id) VALUES (3, 1);
INSERT INTO granted_permission (role_id, operation_id) VALUES (3, 2);
INSERT INTO granted_permission (role_id, operation_id) VALUES (3, 3);
INSERT INTO granted_permission (role_id, operation_id) VALUES (3, 4);
INSERT INTO granted_permission (role_id, operation_id) VALUES (3, 5);
INSERT INTO granted_permission (role_id, operation_id) VALUES (3, 6);

INSERT INTO granted_permission (role_id, operation_id) VALUES (3, 7);
INSERT INTO granted_permission (role_id, operation_id) VALUES (3, 8);
INSERT INTO granted_permission (role_id, operation_id) VALUES (3, 9);
INSERT INTO granted_permission (role_id, operation_id) VALUES (3, 10);
INSERT INTO granted_permission (role_id, operation_id) VALUES (3, 11);
INSERT INTO granted_permission (role_id, operation_id) VALUES (3, 12);
INSERT INTO granted_permission (role_id, operation_id) VALUES (3, 17);
INSERT INTO granted_permission (role_id, operation_id) VALUES (3, 18);

-- CREACIÓN DE PERMISOS PARA RETO SECCION 11
--INSERT INTO granted_permission (role_id, operation_id) VALUES (3, 16);
--INSERT INTO granted_permission (role_id, operation_id) VALUES (3, 17);
--INSERT INTO granted_permission (role_id, operation_id) VALUES (3, 18);
--INSERT INTO granted_permission (role_id, operation_id) VALUES (3, 19);


-- CREACIÓN DE USUARIOS
--INSERT INTO user (username, name, password) VALUES ('lmarquez', 'luis márquez', '$2a$10$ywh1O2EwghHmFIMGeHgsx.9lMw5IXpg4jafeFS.Oi6nFv0181gHli');
--INSERT INTO user (username, name, password) VALUES ('fperez', 'fulano pérez', '$2a$10$V29z7/qC9wpHfzRMxGOHye5RMAxCid2/MzJalk0dsiA3zZ9CJfub.');
--INSERT INTO user (username, name, password) VALUES ('mhernandez', 'mengano hernández', '$2a$10$TMbMuEZ8utU5iq8MOoxpmOc6QWQuYuwgx1xJF8lSMNkKP3hIrwYFG');
--INSERT INTO user (username, name, password, role) VALUES ('lmarquez', 'luis márquez', '$2a$10$ywh1O2EwghHmFIMGeHgsx.9lMw5IXpg4jafeFS.Oi6nFv0181gHli', 'CUSTOMER');
--INSERT INTO user (username, name, password, role) VALUES ('fperez', 'fulano pérez', '$2a$10$V29z7/qC9wpHfzRMxGOHye5RMAxCid2/MzJalk0dsiA3zZ9CJfub.', 'ASSISTANT_ADMINISTRATOR');
--INSERT INTO user (username, name, password, role) VALUES ('mhernandez', 'mengano hernández', '$2a$10$TMbMuEZ8utU5iq8MOoxpmOc6QWQuYuwgx1xJF8lSMNkKP3hIrwYFG', 'ADMINISTRATOR');

INSERT INTO user (username, name, password, role_id) VALUES ('lmarquez', 'luis márquez', '$2a$10$ywh1O2EwghHmFIMGeHgsx.9lMw5IXpg4jafeFS.Oi6nFv0181gHli', 1);
INSERT INTO user (username, name, password, role_id) VALUES ('fperez', 'fulano pérez', '$2a$10$V29z7/qC9wpHfzRMxGOHye5RMAxCid2/MzJalk0dsiA3zZ9CJfub.', 2);
INSERT INTO user (username, name, password, role_id) VALUES ('mhernandez', 'mengano hernández', '$2a$10$TMbMuEZ8utU5iq8MOoxpmOc6QWQuYuwgx1xJF8lSMNkKP3hIrwYFG', 3);

-- CREACIÓN DE CATEGORIAS
--INSERT INTO category (name, status) VALUES ('Electrónica', 'ENABLED');
--INSERT INTO category (name, status) VALUES ('Ropa', 'ENABLED');
--INSERT INTO category (name, status) VALUES ('Deportes', 'ENABLED');
--INSERT INTO category (name, status) VALUES ('Hogar', 'ENABLED');

-- CREACIÓN DE PRODUCTOS
--INSERT INTO product (name, price, status, category_id) VALUES ('Smartphone', 500.00, 'ENABLED', 1);
--INSERT INTO product (name, price, status, category_id) VALUES ('Auriculares Bluetooth', 50.00, 'DISABLED', 1);
--INSERT INTO product (name, price, status, category_id) VALUES ('Tablet', 300.00, 'ENABLED', 1);

--INSERT INTO product (name, price, status, category_id) VALUES ('Camiseta', 25.00, 'ENABLED', 2);
--INSERT INTO product (name, price, status, category_id) VALUES ('Pantalones', 35.00, 'ENABLED', 2);
--INSERT INTO product (name, price, status, category_id) VALUES ('Zapatos', 45.00, 'ENABLED', 2);
--
--INSERT INTO product (name, price, status, category_id) VALUES ('Balón de Fútbol', 20.00, 'ENABLED', 3);
--INSERT INTO product (name, price, status, category_id) VALUES ('Raqueta de Tenis', 80.00, 'DISABLED', 3);
--
--INSERT INTO product (name, price, status, category_id) VALUES ('Aspiradora', 120.00, 'ENABLED', 4);
--INSERT INTO product (name, price, status, category_id) VALUES ('Licuadora', 50.00, 'ENABLED', 4);

insert  into `agrupo` (id_grupo, descri, grupo, tipo) values (1,'ABRASIVOS','02','B'),(2,'ACEROS','03','B'),(3,'AGRICOLA Y PESQUERO','04','B'),(4,'AGROPECUARIA Y JARDINERIA  :   REPUESTOS, ACCESORIOS Y MATERIALES','05','B'),(5,'AIRE ACONDICIONADO Y REFRIGERACION  :   REPUESTOS Y ACCESORIOS','06','B'),(6,'AISLANTES TERMICOS, REFRACTARIOS Y REFRIGERACION  :   MATERIALES','07','B'),(7,'ALIMENTO PARA ANIMALES','08','B'),(8,'ALIMENTOS Y BEBIDAS PARA PERSONAS','09','B'),(9,'ARMAMENTO, MUNIC.. Y EXPLOS.  :   RPTOS, ACCESORIOS Y MATERIALES','10','B'),(10,'AIRE ACONDICIONADO Y REFRIGERACION','11','B'),(11,'ARTES GRAFICAS E IMPRESIONES  :   REPUESTOS Y ACCESORIOS','12','B'),(12,'ASEO, LIMPIEZA Y TOCADOR  :   REPUESTOS, ACCESORIOS, UTILES Y MATERIAL','13','B'),(13,'BIENES DE ACTIVO FIJO NO CATALOGADOS POR SBN','14','B'),(14,'CLAVOS, PERNOS, TORNILLOS, SUJETADORES Y AFINES','15','B'),(15,'COCINA, COMEDOR, CAFETERIA  :   REPUESTOS Y ACCESORIOS','16','B'),(16,'COMBUSTIBLES, CARBURANTES Y LUBICANTES','17','B'),(17,'ANIMALES','18','B'),(18,'COMUNICACIONES Y TELECOMUNICACIONES: REPUESTOS Y ACCESORIOS','19','B'),(19,'CONSTRUCCIONES  :   MATERIALES, RPTOS Y ACC.  INCLUYE SANITARIOS','20','B'),(20,'CULTURA ARTE Y ARTESANIA   :    BIENES','22','B'),(21,'DEPORTES Y RECREACION   :   IMPLEMENTOS','23','B'),(22,'ASEO Y LIMPIEZA','25','B'),(23,'ELECTRONICA  :   MATERIALES','26','B'),(24,'ELEVACION, GENERAC, DISTRIB,  Y CONT, DE LA ELECTRICAD : RPTOS. Y ACCE','27','B'),(25,'ELECTRICIDAD :  MATERIALES Y ACCESORIOS','28','B'),(26,'ENERGIA NUCLEAR, SOLAR Y GEOTERMAL  :   RPTOS, ACCES. Y MAT.','30','B'),(27,'ENSE?ANZA :  RPTOS. ACCESORIOS,  MATERIALES DIDACTICO Y UTILES','31','B'),(28,'COCINA Y COMEDOR','32','B'),(29,'EXTERMINACION :  REPUESTOS,  ACCESORIOS  Y PRODUCTOS','33','B'),(30,'FERTILIZANTES','34','B'),(31,'FISICA Y QUIMICA  :   MATERIALES, PRODUCTOS Y REACTIVOS','35','B'),(32,'FOTOGRAFIA, CINEMAT, TV Y FONOT. :  RPTOS.ACCES. Y MATERIALES','37','B'),(33,'CULTURA Y ARTE','39','B'),(34,'FUNDICION Y SOLDADURA  :   RPTOS, ACCESORIOS Y MATERIALES','40','B'),(35,'HERRAMIENTAS  MANUAL Y MECANICAS  :   INCLUYE  ACCESORIOS','41','B'),(36,'HERRAMIENTAS PORTATILES MECANIZADAS :  INCLUYE ACCESORIOS','42','B'),(37,'BIENES ELABORADOS, EDITADOS E IMPRESOS POR LA INSTITUCION','43','B'),(38,'IMPRESION, ENCUADERNACION Y PUBLICACION :  MATERIALES','44','B'),(39,'INDUSTRIA  MAQUINAS.  : REPUESTOS Y ACCESORIOS','45','B'),(40,'ELECTRICIDAD Y ELECTRONICA','46','B'),(41,'BIENES  IMPRESOS : FORMATOS,  FORMULARIOS Y SIMILARES IMPRESOS','47','B'),(42,'INGENIERIA, DIBUJO Y CARTOGRAFIA :  UTILES Y MATERIALES','48','B'),(43,'INSTRUMENTAL MED Y HOSPITAL :  REPTOS, ACCES, Y MATERIALES','49','B'),(44,'INDUSTRIA :  PROD. PARA FABRICACION, EXCEPTO MATERIAS PRIMAS','50','B'),(45,'LABOR. Y GABINETE: IMPLEM., RPTOS. ACC. Y MATERIALES','51','B'),(46,'LAVANDERIA  :   REPUESTO Y ACCESORIOS','52','B'),(47,'HOSPITALIZACION','53','B'),(48,'LIBROS, DIARIOS,  REVISTAS Y PUBLICACIONES PERIODICAS','54','B'),(49,'MATERIAS PRIMAS','57','B'),(50,'MEDICINAS','59','B'),(51,'INSTRUMENTO DE MEDICION','60','B'),(52,'METEOROLOGIA INSTRUMENTOS : REPUESTOS Y ACCESORIOS','61','B'),(53,'METALES NO FERROSOS','62','B'),(54,'MINERO Y FERROVIARIO: REPUESTOS, ACCESORIOS Y MATERIALES','63','B'),(55,'ENSERES Y ACCESORIOS  :  INCLUYE AULAS Y TALLERES','64','B'),(56,'MOVIMIENTO Y ACARREO DE TIERRA Y SIMILARES : RPTOS. YACC.','65','B'),(57,'MOTORES Y BOMBAS : REPUESTOS Y ACCESORIOS','66','B'),(58,'MAQUINARIA, VEHICULOS Y OTROS','67','B'),(59,'MUSICA: ACCESORIOS Y REPUESTOS','68','B'),(60,'OFICINA EQUIPO Y MAQUINARIAS   :  REPUESTOS Y ACCESORIOS','70','B'),(61,'OFICINA  :   UTILES Y MATERIALES','71','B'),(62,'PINTURAS, BARNIS, PEGAM. Y OT. REVESTIM  :  PROD.Y ACCES','73','B'),(63,'OFICINA','74','B'),(64,'PRECIS, MEDIC, REG Y CONTROL: REPUESTOS Y ACCESORIOS','75','B'),(65,'PROCES. AUTOM. DE DATOS  :   RPTOS, ACCESORIOS Y MATERIALES','76','B'),(66,'PISCICULTURA: MATERIALES Y REPUESTOS','77','B'),(67,'RELOJERIA  :   REPUESTOS Y ACCESORIOS','78','B'),(68,'ROPA DE CAMA Y EQUIPO DE CAMPA?A','79','B'),(69,'SEGURIDAD: REPUESTOS, ACCESORIOS Y PROTECTORES','80','B'),(70,'RECREACION Y DEPORTE','81','B'),(71,'SEMOVIENTES, VETERINARIA Y GANADERIA','83','B'),(72,'SE?ALIZADORES DE TRANSITO','84','B'),(73,'SIMBOLOS, DISTINTIVOS, CONDECORACIONES Y OBJETOS DECORATIVOS','85','B'),(74,'TALLERES  :   REPUESTOS Y ACCESORIOS','87','B'),(75,'SEGURIDAD INDUSTRIAL','88','B'),(76,'TELAS Y MATERIALES TEXTILES. INCLUYE VESTUARIO','89','B'),(77,'TRANSMISION Y RODAMIENTO: REPUESTOS, ARTICULOS Y ACCESORIOS','90','B'),(78,'TRANSPORTE ACUATICO Y AEREO: REPUESTOS Y ACCESORIOS','91','B'),(79,'TRANSPORTE TERRESTRE: REPUESTOS Y ACCESORIOS','94','B'),(80,'TELECOMUNICACIONES','95','B'),(81,'TUBERIAS, CONEXIONES Y ACCESORIOS INC: MANGUERAS Y EMPAQUETA','96','B'),(82,'ZAPATERIA Y TALABARTERIA: PROD., MATERIALES.,  ACC. Y RPTOS','99','B'),(83,'FICTICIO','00','F'),(84,'OBRAS RELACIONADAS CON A AGRICULTURA','01','O'),(85,'OBRAS RELACIONADAS CON EL TRANSPORTE','02','O'),(86,'OBRAS RELACIONADAS CON LA ENERGIA Y MINERIA','03','O'),(87,'OBRAS RELACIONADAS CON EL TURISMO','04','O'),(88,'OBRAS RELACIONADAS CON LA VIVIENDA Y CONSTRUCCION','05','O'),(89,'OBRAS RELACIONADAS CON LAS COMUNICACIONES','06','O'),(90,'ACONDICIONAMIENTO','02','S'),(91,'ALIMENTACION','04','S'),(92,'SERVICIO DE ASEO Y LIMPIEZA','06','S'),(93,'ASESORIA, CONSULTORIA E INTERVENCIONES ESPECIALIZADAS','07','S'),(94,'SERVICIOS NO PERSONALES','08','S'),(95,'SERVICIOS PRESTADOS POR TERCEROS','09','S'),(96,'ATENCIONES Y CELEBRACIONES','10','S'),(97,'COMUNICACION, INFORMACION Y PUBLICIDAD','15','S'),(98,'DEPORTES, RECREACION Y ESPECTACULOS','27','S'),(99,'EMBALAJE Y ALMACENAJE','33','S'),(100,'ENSE?ANZA Y CAPACITACION','35','S'),(101,'ESTUDIOS E INVESTIGACIONES','37','S'),(102,'CONSTRUCCION, FABRICACION Y CONFECCIONES EN GENERAL','41','S'),(103,'IMPRESIONES GRAFICAS Y ENCUADERNACION','50','S'),(104,'INSTALACIONES DIVERSAS','52','S'),(105,'MANTENIMIENTO PREDICTIVO, PREVENTIVO Y CORRECTIVO','60','S'),(106,'PRODUCCIONES, REPRODUCCIONES Y GRABACIONES','70','S'),(107,'SERVICIO SEGURIDAD Y VIGILANCIA','84','S'),(108,'SEGUROS','85','S'),(109,'SERVICIOS JUDICIALES Y NOTARIALES','86','S'),(110,'SERVICIOS PUBLICOS Y GENERALES','87','S'),(111,'SERVICIOS REGULADOS POR EL ESTADO','88','S'),(112,'TRANSPORTE Y TRASLADO','90','S'),(113,'USO TEMPORAL DE BIENES MUEBLES,  INMUEBLES Y OTROS','94','S'),(114,'PRODUCTOS FARMACEUTICOS','58','B'),(131,'FIDEOS','FI','B');
