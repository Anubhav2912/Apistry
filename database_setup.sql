-- MySQL Database Setup Script for Apistry
-- Run this script in your MySQL client to create the database

-- Create the database
CREATE DATABASE IF NOT EXISTS Apistry
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;

-- Use the database
USE Apistry;

-- Show the created database
SHOW DATABASES;

-- Verify the database is selected
SELECT DATABASE();

-- Note: The tables will be automatically created by Hibernate when you run the application
-- with spring.jpa.hibernate.ddl-auto=update in your application.yml

-- You can verify the tables after running the application:
-- SHOW TABLES;
