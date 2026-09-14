-- Creates one database per microservice.
-- Mounted into the MySQL container's /docker-entrypoint-initdb.d/ so it
-- runs automatically the first time the container starts.

CREATE DATABASE IF NOT EXISTS zencart_user_db;
CREATE DATABASE IF NOT EXISTS zencart_product_db;
CREATE DATABASE IF NOT EXISTS zencart_order_db;
CREATE DATABASE IF NOT EXISTS zencart_payment_db;
CREATE DATABASE IF NOT EXISTS zencart_notification_db;
