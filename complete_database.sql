-- ========================================================================
-- House Price Prediction System - Complete Database Schema
-- Run this entire script in MySQL to rebuild the database from scratch
-- ========================================================================

CREATE DATABASE IF NOT EXISTS house_price_prediction_system;
USE house_price_prediction_system;

-- Drop existing tables to ensure clean creation (Order matters for foreign keys)
DROP TABLE IF EXISTS searchhistory;
DROP TABLE IF EXISTS model_params;
DROP TABLE IF EXISTS property;
DROP TABLE IF EXISTS location;
DROP TABLE IF EXISTS city;
DROP TABLE IF EXISTS state;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS adminlogin;

-- ========================================================================
-- 1. STATE TABLE
-- ========================================================================
CREATE TABLE state (
    stateid   INT(5)       PRIMARY KEY AUTO_INCREMENT,
    statename VARCHAR(200) NOT NULL UNIQUE,
    status    INT(1)       NOT NULL DEFAULT 1
);

-- ========================================================================
-- 2. CITY TABLE
-- ========================================================================
CREATE TABLE city (
    ctid      INT(5)       PRIMARY KEY AUTO_INCREMENT,
    cityname  VARCHAR(200) NOT NULL UNIQUE,
    stateid   INT(5)       NOT NULL,
    status    INT(1)       NOT NULL DEFAULT 1,
    FOREIGN KEY (stateid) REFERENCES state(stateid) 
        ON DELETE CASCADE ON UPDATE CASCADE
);

-- ========================================================================
-- 3. LOCATION TABLE
-- ========================================================================
CREATE TABLE location (
    locid     INT(5)       PRIMARY KEY AUTO_INCREMENT,
    locname   VARCHAR(200) NOT NULL,
    ctid      INT(5)       NOT NULL,
    status    INT(1)       NOT NULL DEFAULT 1,
    FOREIGN KEY (ctid) REFERENCES city(ctid) 
        ON DELETE CASCADE ON UPDATE CASCADE
);

-- ========================================================================
-- 4. PROPERTY TABLE
-- Note: actualprice and status are required for the AI Engine & App Logic
-- ========================================================================
CREATE TABLE property (
    pid          INT(5)         PRIMARY KEY AUTO_INCREMENT,
    Pname        VARCHAR(200)   NOT NULL,
    paddress     VARCHAR(200)   NOT NULL UNIQUE,
    age          INT(5)         NOT NULL CHECK (age > 0),
    asqfeet      INT(5)         NOT NULL,
    nbath        INT(5)         NOT NULL DEFAULT 1,
    nbed         INT(5)         NOT NULL DEFAULT 1,
    locid        INT(5)         NOT NULL,
    actualprice  DOUBLE         NOT NULL DEFAULT 0, -- Needed for Machine Learning
    status       INT(1)         NOT NULL DEFAULT 1, -- Needed for soft deletes
    FOREIGN KEY (locid) REFERENCES location(locid) 
        ON DELETE CASCADE ON UPDATE CASCADE
);

-- ========================================================================
-- 5. ADMIN LOGIN TABLE (Fallback check)
-- ========================================================================
CREATE TABLE adminlogin (
    username VARCHAR(100) NOT NULL PRIMARY KEY,
    password VARCHAR(100) NOT NULL
);
-- Insert default admin
INSERT INTO adminlogin(username, password) VALUES('omkar', 'omkar');

-- ========================================================================
-- 6. USERS TABLE (Primary Auth Table)
-- ========================================================================
CREATE TABLE users (
    userid   INT(5)       PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    usertype VARCHAR(20)  NOT NULL DEFAULT 'user'
);
-- Insert default admin & test user
INSERT INTO users(username, password, usertype) VALUES('omkar', 'omkar@gmail.com', 'admin');
INSERT INTO users(username, password, usertype) VALUES('rohan', 'rohan@gmail.com', 'user');

-- ========================================================================
-- 7. LINEAR REGRESSION MODEL PARAMETERS
-- ========================================================================
CREATE TABLE model_params (
    paramid      INT(5)   PRIMARY KEY AUTO_INCREMENT,
    intercept    DOUBLE   NOT NULL DEFAULT 0,
    slope_sqfeet DOUBLE   NOT NULL DEFAULT 0,
    slope_nbed   DOUBLE   NOT NULL DEFAULT 0,
    slope_nbath  DOUBLE   NOT NULL DEFAULT 0,
    slope_age    DOUBLE   NOT NULL DEFAULT 0,
    r_squared    DOUBLE   NOT NULL DEFAULT 0,
    mse          DOUBLE   NOT NULL DEFAULT 0,
    trained_at   DATETIME DEFAULT CURRENT_TIMESTAMP
);

-- ========================================================================
-- 8. SEARCH HISTORY TABLE
-- ========================================================================
CREATE TABLE searchhistory (
    histid          INT(5)        PRIMARY KEY AUTO_INCREMENT,
    userid          INT(5)        NOT NULL,
    statename       VARCHAR(200)  DEFAULT 'N/A',
    cityname        VARCHAR(200)  DEFAULT 'N/A',
    locname         VARCHAR(200)  DEFAULT 'N/A',
    asqfeet         INT(5)        DEFAULT 0,
    nbed            INT(5)        DEFAULT 0,
    nbath           INT(5)        DEFAULT 0,
    age             INT(5)        DEFAULT 0,
    predicted_price DOUBLE        DEFAULT 0,
    search_date     DATETIME      DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (userid) REFERENCES users(userid) 
        ON DELETE CASCADE ON UPDATE CASCADE
);

-- DONE
SHOW TABLES;
