-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
-- -----------------------------------------------------
-- Schema ris_projekat
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema ris_projekat
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `ris_projekat` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci ;
USE `ris_projekat` ;

-- -----------------------------------------------------
-- Table `ris_projekat`.`album`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ris_projekat`.`album` (
  `idAlbum` INT NOT NULL AUTO_INCREMENT,
  `naziv` VARCHAR(100) NOT NULL,
  `izvodjac` VARCHAR(100) NOT NULL,
  `godIzdanja` INT NOT NULL,
  `brKomNaStanju` INT NOT NULL,
  `cena` DOUBLE NOT NULL,
  `albumArt` MEDIUMBLOB NOT NULL,
  `zanr` VARCHAR(100) NOT NULL,
  `spotifyCode` VARCHAR(150) NOT NULL,
  PRIMARY KEY (`idAlbum`))
ENGINE = InnoDB
AUTO_INCREMENT = 18
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `ris_projekat`.`role`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ris_projekat`.`role` (
  `idRole` INT NOT NULL AUTO_INCREMENT,
  `naziv` VARCHAR(100) NOT NULL,
  PRIMARY KEY (`idRole`))
ENGINE = InnoDB
AUTO_INCREMENT = 3
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `ris_projekat`.`korisnik`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ris_projekat`.`korisnik` (
  `idKorisnik` INT NOT NULL AUTO_INCREMENT,
  `ime` VARCHAR(100) NOT NULL,
  `prezime` VARCHAR(100) NOT NULL,
  `username` VARCHAR(100) NOT NULL,
  `password` VARCHAR(500) NOT NULL,
  `adresa` VARCHAR(300) NOT NULL,
  `role_fk` INT NOT NULL,
  PRIMARY KEY (`idKorisnik`),
  UNIQUE INDEX `username_UNIQUE` (`username` ASC) VISIBLE,
  INDEX `role_fk_idx` (`role_fk` ASC) VISIBLE,
  CONSTRAINT `role_fk`
    FOREIGN KEY (`role_fk`)
    REFERENCES `ris_projekat`.`role` (`idRole`))
ENGINE = InnoDB
AUTO_INCREMENT = 14
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `ris_projekat`.`narudzbina`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ris_projekat`.`narudzbina` (
  `idNarudzbina` INT NOT NULL AUTO_INCREMENT,
  `brKartice` VARCHAR(20) NOT NULL,
  `imeNaKartici` VARCHAR(150) NOT NULL,
  `rokVazenjaKartice` DATE NOT NULL,
  `cvv` VARCHAR(4) NOT NULL,
  `iznos` DOUBLE NOT NULL,
  `kupac_fk` INT NOT NULL,
  `prodavac_fk` INT NULL DEFAULT NULL,
  PRIMARY KEY (`idNarudzbina`),
  INDEX `prodavac_fk_idx` (`prodavac_fk` ASC) VISIBLE,
  INDEX `kupac_fk_idx` (`kupac_fk` ASC) VISIBLE,
  CONSTRAINT `kupac_fk`
    FOREIGN KEY (`kupac_fk`)
    REFERENCES `ris_projekat`.`korisnik` (`idKorisnik`),
  CONSTRAINT `prodavac_fk`
    FOREIGN KEY (`prodavac_fk`)
    REFERENCES `ris_projekat`.`korisnik` (`idKorisnik`))
ENGINE = InnoDB
AUTO_INCREMENT = 57
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `ris_projekat`.`isporuka`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ris_projekat`.`isporuka` (
  `idIsporuka` INT NOT NULL AUTO_INCREMENT,
  `mesto` VARCHAR(100) NOT NULL,
  `postBr` VARCHAR(50) NOT NULL,
  `adresa` VARCHAR(100) NOT NULL,
  `datumPripreme` DATE NOT NULL,
  `narudzbina_fk` INT NOT NULL,
  PRIMARY KEY (`idIsporuka`),
  UNIQUE INDEX `narudzbina_fk_UNIQUE` (`narudzbina_fk` ASC) VISIBLE,
  CONSTRAINT `narudzbina_isporuka_fk`
    FOREIGN KEY (`narudzbina_fk`)
    REFERENCES `ris_projekat`.`narudzbina` (`idNarudzbina`))
ENGINE = InnoDB
AUTO_INCREMENT = 23
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `ris_projekat`.`stavkanarudzbina`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ris_projekat`.`stavkanarudzbina` (
  `idStavkaNarudzbina` INT NOT NULL AUTO_INCREMENT,
  `kolicina` INT NOT NULL,
  `narudzbina_fk` INT NOT NULL,
  `album_fk` INT NOT NULL,
  PRIMARY KEY (`idStavkaNarudzbina`),
  INDEX `album_fk_idx` (`album_fk` ASC) VISIBLE,
  INDEX `narudzbina_fk_idx` (`narudzbina_fk` ASC) VISIBLE,
  CONSTRAINT `album_fk`
    FOREIGN KEY (`album_fk`)
    REFERENCES `ris_projekat`.`album` (`idAlbum`),
  CONSTRAINT `narudzbina_fk`
    FOREIGN KEY (`narudzbina_fk`)
    REFERENCES `ris_projekat`.`narudzbina` (`idNarudzbina`))
ENGINE = InnoDB
AUTO_INCREMENT = 53
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
