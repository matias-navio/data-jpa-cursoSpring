

INSERT INTO clientes (nombre, apellido, email, create_at, foto) VALUES ('Matias', 'Navio', 'navio@gmail.com', '2023-03-26', '');
INSERT INTO clientes (nombre, apellido, email, create_at, foto) VALUES ('Juan', 'Elias', 'juan@gmail.com', '2022-03-26', '');
INSERT INTO clientes (nombre, apellido, email, create_at, foto) VALUES ('Giuliano', 'Blengini', 'gordo@gmail.com', '2022-03-26', '');
INSERT INTO clientes (nombre, apellido, email, create_at, foto) VALUES ('Matias', 'Mondello', 'matias@gmail.com', '2022-03-26', '');
INSERT INTO clientes (nombre, apellido, email, create_at, foto) VALUES ('Juani', 'Cenci', 'juani@gmail.com', '2022-03-26', '');
INSERT INTO clientes (nombre, apellido, email, create_at, foto) VALUES ('Octavio', 'Salndeña', 'octa@gmail.com', '2022-03-26', '');
INSERT INTO clientes (nombre, apellido, email, create_at, foto) VALUES ('Martin', 'Perez', 'marto@gmail.com', '2022-03-26', '');
INSERT INTO clientes (nombre, apellido, email, create_at, foto) VALUES ('Juana', 'Martinez', 'juana@gmail.com', '2022-03-26', '');
INSERT INTO clientes (nombre, apellido, email, create_at, foto) VALUES ('Maria', 'Robles', 'maria@gmail.com', '2022-03-26', '');
INSERT INTO clientes (nombre, apellido, email, create_at, foto) VALUES ('Guillermo', 'Rodriguez', 'guille@gmail.com', '2022-03-26', '');
INSERT INTO clientes (nombre, apellido, email, create_at, foto) VALUES ('Marina', 'Bellville', 'juamari@gmail.com', '2022-03-26', '');
INSERT INTO clientes (nombre, apellido, email, create_at, foto) VALUES ('Romina', 'Torres', 'romi@gmail.com', '2022-03-26', '');
INSERT INTO clientes (nombre, apellido, email, create_at, foto) VALUES ('Agustina', 'Villegas', 'agus@gmail.com', '2022-03-26', '');
INSERT INTO clientes (nombre, apellido, email, create_at, foto) VALUES ('Guillermina', 'Richard', 'guillee@gmail.com', '2022-03-26', '');
INSERT INTO clientes (nombre, apellido, email, create_at, foto) VALUES ('Franco', 'Miranda', 'franco@gmail.com', '2022-03-26', '');
INSERT INTO clientes (nombre, apellido, email, create_at, foto) VALUES ('Francisco', 'Baigorria', 'fran@gmail.com', '2022-03-26', '');
INSERT INTO clientes (nombre, apellido, email, create_at, foto) VALUES ('Millena', 'Giles', 'mille@gmail.com', '2022-03-26', '');
INSERT INTO clientes (nombre, apellido, email, create_at, foto) VALUES ('Marcos', 'Vesquez', 'marcos@gmail.com', '2022-03-26', '');
INSERT INTO clientes (nombre, apellido, email, create_at, foto) VALUES ('Luisina', 'Carrillo', 'lui@gmail.com', '2022-03-26', '');
INSERT INTO clientes (nombre, apellido, email, create_at, foto) VALUES ('Olivia', 'Marcosi', 'oli@gmail.com', '2022-03-26', '');

--ingresamos productos
INSERT INTO productos (nombre, precio, create_at) VALUES ('Pantalla SAMSUNG 24', 50000, NOW());
INSERT INTO productos (nombre, precio, create_at) VALUES ('Camara KOLKE', 120000,NOW());
INSERT INTO productos (nombre, precio, create_at) VALUES ('Notebook LG KL10', 200000,NOW());
INSERT INTO productos (nombre, precio, create_at) VALUES ('Pantalla SONY 55', 100000,NOW());
INSERT INTO productos (nombre, precio, create_at) VALUES ('Notebook HP KTU11-0', 150000,NOW());
INSERT INTO productos (nombre, precio, create_at) VALUES ('Camara SONY RPY-120', 250000,NOW());
INSERT INTO productos (nombre, precio, create_at) VALUES ('Escritorio platinum', 100000,NOW());


--ingresamos algunas facturas
INSERT INTO facturas (descripcion, observasion, cliente_id, create_at) VALUES ('Factura de equipos de oficina', null, 1, NOW());
--items de la factura
INSERT INTO facturas_items(cantidad, producto_id, factura_id) VALUES (1, 1, 1)
INSERT INTO facturas_items(cantidad, producto_id, factura_id) VALUES (3, 7, 1)
INSERT INTO facturas_items(cantidad, producto_id, factura_id) VALUES (3, 4, 1)
INSERT INTO facturas_items(cantidad, producto_id, factura_id) VALUES (3, 7, 1)

INSERT INTO facturas (descripcion, observasion, cliente_id, create_at) VALUES ('Factura de cámara', null, 5, NOW());
INSERT INTO facturas_items(cantidad, producto_id, factura_id) VALUES (1, 2, 2)



