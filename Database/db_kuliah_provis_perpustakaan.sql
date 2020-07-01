/*
SQLyog Ultimate v12.4.3 (64 bit)
MySQL - 10.1.34-MariaDB : Database - db_kuliah_provis_perpustakaan
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`db_kuliah_provis_perpustakaan` /*!40100 DEFAULT CHARACTER SET latin1 */;

USE `db_kuliah_provis_perpustakaan`;

/*Table structure for table `t_anggota` */

DROP TABLE IF EXISTS `t_anggota`;

CREATE TABLE `t_anggota` (
  `Kd_Anggota` varchar(20) NOT NULL,
  `Nama_Anggota` varchar(30) DEFAULT NULL,
  `Status_Anggota` char(20) DEFAULT NULL,
  `Email` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`Kd_Anggota`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `t_anggota` */

insert  into `t_anggota`(`Kd_Anggota`,`Nama_Anggota`,`Status_Anggota`,`Email`) values 
('10118227','FAUZAN LUKMANUL HAKIM','BIASA','FAUZAN.10118227@MAHASISWA.UNIKOM.AC.ID'),
('10118256','GENTRA ARIA WIBAWA','KHUSUS','GENTRA@10118256@MAHASISWA.UNIKOM.AC.ID'),
('10118265','MOCHAMAD ALVY NUR RAMADHAN','BIASA','ALVY@10118265@MAHASISWA.UNIKOM.AC.ID');

/*Table structure for table `t_jenis_koleksi` */

DROP TABLE IF EXISTS `t_jenis_koleksi`;

CREATE TABLE `t_jenis_koleksi` (
  `Kd_Jenis` int(11) NOT NULL AUTO_INCREMENT,
  `Nama_Jenis` char(30) DEFAULT NULL,
  PRIMARY KEY (`Kd_Jenis`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;

/*Data for the table `t_jenis_koleksi` */

insert  into `t_jenis_koleksi`(`Kd_Jenis`,`Nama_Jenis`) values 
(1,'SIRKULASI'),
(2,'REFERENSI'),
(3,'AUDIO VISUAL');

/*Table structure for table `t_kategori_koleksi` */

DROP TABLE IF EXISTS `t_kategori_koleksi`;

CREATE TABLE `t_kategori_koleksi` (
  `Kd_Kategori` int(11) NOT NULL AUTO_INCREMENT,
  `Nama_Kategori` char(30) DEFAULT NULL,
  PRIMARY KEY (`Kd_Kategori`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=latin1;

/*Data for the table `t_kategori_koleksi` */

insert  into `t_kategori_koleksi`(`Kd_Kategori`,`Nama_Kategori`) values 
(1,'PENGETAHUAN UMUM'),
(2,'KOMPUTER'),
(3,'FILSAFAT'),
(4,'AGAMA'),
(5,'SOSIAL'),
(6,'EKONOMI'),
(7,'HUKUM'),
(8,'PENDIDIKAN'),
(9,'BAHASA'),
(10,'MATEMATIKA'),
(11,'SCIENCE'),
(12,'AKUNTASI'),
(13,'MANAJEMEN'),
(14,'DESAIN'),
(15,'ARSITEKTUR'),
(16,'KESASTRAAN'),
(17,'GEOGRAFI'),
(18,'SEJARAH');

/*Table structure for table `t_koleksi` */

DROP TABLE IF EXISTS `t_koleksi`;

CREATE TABLE `t_koleksi` (
  `Kd_Koleksi` int(11) NOT NULL AUTO_INCREMENT,
  `Judul_Koleksi` varchar(50) DEFAULT NULL,
  `Nama_Pengarang` char(30) DEFAULT NULL,
  `Nama_Penerbit` char(30) DEFAULT NULL,
  `Tahun_Terbit` char(4) DEFAULT NULL,
  `No_Rak` char(10) DEFAULT NULL,
  `Kd_Jenis` int(11) DEFAULT NULL,
  `Kd_Tipe` int(11) DEFAULT NULL,
  `Kd_Kategori` int(11) DEFAULT NULL,
  `Kd_Terbitan` int(11) DEFAULT NULL,
  `Status` char(20) DEFAULT NULL,
  `Estimasi_Pengembalian` date DEFAULT NULL,
  `Harga` int(11) DEFAULT NULL,
  PRIMARY KEY (`Kd_Koleksi`),
  KEY `Kd_Terbitan` (`Kd_Terbitan`),
  KEY `Kd_Kategori` (`Kd_Kategori`),
  KEY `Kd_Tipe` (`Kd_Tipe`),
  KEY `Kd_Jenis` (`Kd_Jenis`),
  CONSTRAINT `t_koleksi_ibfk_1` FOREIGN KEY (`Kd_Terbitan`) REFERENCES `t_terbitan_koleksi` (`Kd_Terbitan`),
  CONSTRAINT `t_koleksi_ibfk_2` FOREIGN KEY (`Kd_Kategori`) REFERENCES `t_kategori_koleksi` (`Kd_Kategori`),
  CONSTRAINT `t_koleksi_ibfk_3` FOREIGN KEY (`Kd_Tipe`) REFERENCES `t_tipe_koleksi` (`Kd_Tipe`),
  CONSTRAINT `t_koleksi_ibfk_4` FOREIGN KEY (`Kd_Jenis`) REFERENCES `t_jenis_koleksi` (`Kd_Jenis`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;

/*Data for the table `t_koleksi` */

insert  into `t_koleksi`(`Kd_Koleksi`,`Judul_Koleksi`,`Nama_Pengarang`,`Nama_Penerbit`,`Tahun_Terbit`,`No_Rak`,`Kd_Jenis`,`Kd_Tipe`,`Kd_Kategori`,`Kd_Terbitan`,`Status`,`Estimasi_Pengembalian`,`Harga`) values 
(1,'ACCESS BY DESIGN','GEORGE A. COVINGTON','VAN NOSTRAND REIHOLD','2007','5',1,1,14,3,'DIPINJAM','2020-07-08',250000);

/*Table structure for table `t_master` */

DROP TABLE IF EXISTS `t_master`;

CREATE TABLE `t_master` (
  `Username` char(20) NOT NULL,
  `Password` char(20) DEFAULT NULL,
  `Hak_Akses` char(20) DEFAULT NULL,
  `Kd_Pegawai` char(20) DEFAULT NULL,
  PRIMARY KEY (`Username`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `t_master` */

insert  into `t_master`(`Username`,`Password`,`Hak_Akses`,`Kd_Pegawai`) values 
('ADMIN','ADMIN3','ADMIN',NULL),
('USER','USER','USER',NULL);

/*Table structure for table `t_peminjaman` */

DROP TABLE IF EXISTS `t_peminjaman`;

CREATE TABLE `t_peminjaman` (
  `Kd_Peminjaman` char(10) NOT NULL,
  `Kd_Koleksi` int(11) DEFAULT NULL,
  `Kd_Anggota` varchar(20) DEFAULT NULL,
  `Tgl_Pinjam` date DEFAULT NULL,
  `Tgl_Kembali` date DEFAULT NULL,
  `Estimasi_Pengembalian` date DEFAULT NULL,
  `Denda_Keterlambatan` int(11) DEFAULT NULL,
  `Status` char(15) DEFAULT NULL,
  PRIMARY KEY (`Kd_Peminjaman`),
  KEY `Kd_Anggota` (`Kd_Anggota`),
  KEY `Kd_Koleksi` (`Kd_Koleksi`),
  CONSTRAINT `t_peminjaman_ibfk_2` FOREIGN KEY (`Kd_Anggota`) REFERENCES `t_anggota` (`Kd_Anggota`),
  CONSTRAINT `t_peminjaman_ibfk_3` FOREIGN KEY (`Kd_Koleksi`) REFERENCES `t_koleksi` (`Kd_Koleksi`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `t_peminjaman` */

insert  into `t_peminjaman`(`Kd_Peminjaman`,`Kd_Koleksi`,`Kd_Anggota`,`Tgl_Pinjam`,`Tgl_Kembali`,`Estimasi_Pengembalian`,`Denda_Keterlambatan`,`Status`) values 
('20206.1',1,'10118227','2020-06-30','2020-06-30','2020-07-07',0,'DIKEMBALIKAN'),
('20206.2',1,'10118227','2020-06-30','2020-07-01','2020-07-07',0,'DIKEMBALIKAN'),
('20207.1',1,'10118227','2020-07-01',NULL,'2020-07-08',NULL,'DIPINJAM');

/*Table structure for table `t_pengembalian_bayar` */

DROP TABLE IF EXISTS `t_pengembalian_bayar`;

CREATE TABLE `t_pengembalian_bayar` (
  `Kd_Kehilangan` int(11) NOT NULL,
  `Tanggal_Ganti` date DEFAULT NULL,
  `Harga_Ganti` int(11) DEFAULT NULL,
  `Kd_Peminjaman` char(10) DEFAULT NULL,
  PRIMARY KEY (`Kd_Kehilangan`),
  KEY `Kd_Peminjaman` (`Kd_Peminjaman`),
  CONSTRAINT `t_pengembalian_bayar_ibfk_1` FOREIGN KEY (`Kd_Peminjaman`) REFERENCES `t_peminjaman` (`Kd_Peminjaman`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `t_pengembalian_bayar` */

/*Table structure for table `t_pengembalian_ganti` */

DROP TABLE IF EXISTS `t_pengembalian_ganti`;

CREATE TABLE `t_pengembalian_ganti` (
  `Kd_Kehilangan` int(11) NOT NULL AUTO_INCREMENT,
  `Tanggal_Ganti` date DEFAULT NULL,
  `Kode_Koleksi_Ganti` int(11) DEFAULT NULL,
  `Kd_Peminjaman` char(10) DEFAULT NULL,
  PRIMARY KEY (`Kd_Kehilangan`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `t_pengembalian_ganti` */

/*Table structure for table `t_terbitan_koleksi` */

DROP TABLE IF EXISTS `t_terbitan_koleksi`;

CREATE TABLE `t_terbitan_koleksi` (
  `Kd_Terbitan` int(11) NOT NULL AUTO_INCREMENT,
  `Nama_Terbitan` char(20) DEFAULT NULL,
  PRIMARY KEY (`Kd_Terbitan`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;

/*Data for the table `t_terbitan_koleksi` */

insert  into `t_terbitan_koleksi`(`Kd_Terbitan`,`Nama_Terbitan`) values 
(1,'DALAM NEGERI'),
(2,'DALAM NEGERI LANGKA'),
(3,'LUAR NEGERI');

/*Table structure for table `t_tipe_koleksi` */

DROP TABLE IF EXISTS `t_tipe_koleksi`;

CREATE TABLE `t_tipe_koleksi` (
  `Kd_Tipe` int(11) NOT NULL AUTO_INCREMENT,
  `Nama_Tipe` char(20) DEFAULT NULL,
  PRIMARY KEY (`Kd_Tipe`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;

/*Data for the table `t_tipe_koleksi` */

insert  into `t_tipe_koleksi`(`Kd_Tipe`,`Nama_Tipe`) values 
(1,'BUKU'),
(2,'CD / AUDIO VISUAL'),
(3,'BERKALA / SERIAL'),
(4,'KARTOGRAFIS');

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
