-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema BrainrotTranslator
-- -----------------------------------------------------
DROP SCHEMA IF EXISTS `BrainrotTranslator` ;

-- -----------------------------------------------------
-- Schema BrainrotTranslator
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `BrainrotTranslator` DEFAULT CHARACTER SET utf8 ;
USE `BrainrotTranslator` ;

-- -----------------------------------------------------
-- Table `BrainrotTranslator`.`user`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `BrainrotTranslator`.`user` ;

CREATE TABLE IF NOT EXISTS `BrainrotTranslator`.`user` (
  `userid` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  `password` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`userid`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `BrainrotTranslator`.`leaderboard`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `BrainrotTranslator`.`leaderboard` ;

CREATE TABLE IF NOT EXISTS `BrainrotTranslator`.`leaderboard` (
  `qscoreid` INT NOT NULL AUTO_INCREMENT,
  `userid` INT NOT NULL,
  `quizscore` INT NOT NULL DEFAULT 0,
  `gametype` INT NOT NULL COMMENT 'save score with 1 if quiz.\n\nsave score with 2 if match-it',
  PRIMARY KEY (`qscoreid`),
  INDEX `userid_idx` (`userid` ASC),
  CONSTRAINT `userid`
    FOREIGN KEY (`userid`)
    REFERENCES `BrainrotTranslator`.`user` (`userid`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `BrainrotTranslator`.`Match-It`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `BrainrotTranslator`.`Match-It` ;

CREATE TABLE IF NOT EXISTS `BrainrotTranslator`.`Match-It` (
  `idmatch-it` INT NOT NULL AUTO_INCREMENT,
  `quote` VARCHAR(255) NOT NULL,
  `imagename` VARCHAR(100) NOT NULL,
  PRIMARY KEY (`idmatch-it`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `BrainrotTranslator`.`Questions`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `BrainrotTranslator`.`Questions` ;

CREATE TABLE IF NOT EXISTS `BrainrotTranslator`.`Questions` (
  `questionid` INT NOT NULL AUTO_INCREMENT,
  `question` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`questionid`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `BrainrotTranslator`.`dictionary`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `BrainrotTranslator`.`dictionary` ;

CREATE TABLE IF NOT EXISTS `BrainrotTranslator`.`dictionary` (
  `slangid` INT NOT NULL AUTO_INCREMENT,
  `slang` VARCHAR(500) NOT NULL,
  `defintion` VARCHAR(1000) NOT NULL,
  `imagename` VARCHAR(100) NOT NULL,
  PRIMARY KEY (`slangid`))
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
