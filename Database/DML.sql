INSERT INTO T_Koleksi (Judul_Koleksi, Nama_Pengarang, Nama_Penerbit, Tahun_Terbit, No_Rak, Kd_Jenis, Kd_Tipe, Kd_Kategori, Kd_Terbitan, STATUS, Harga,Estimasi_Pengembalian) VALUES 
('ACCESS BY DESING','GEORGE A. COVINGTON','VAN NOSTRAND REIHOLD','2007','5','1','1','14','3','TERSEDIA','250000',NULL),
('ACCESS BY DESING','GEORGE A. COVINGTON','VAN NOSTRAND REIHOLD','2007','5','1','1','14','3','TERSEDIA','250000',NULL),
('ACCESS BY DESING','GEORGE A. COVINGTON','VAN NOSTRAND REIHOLD','2007','5','1','1','14','3','TERSEDIA','250000',NULL),
('ACCESS BY DESING','GEORGE A. COVINGTON','VAN NOSTRAND REIHOLD','2007','5','1','1','14','3','TERSEDIA','250000',NULL),
('ACCESS BY DESING','GEORGE A. COVINGTON','VAN NOSTRAND REIHOLD','2007','5','1','1','14','3','TERSEDIA','250000',NULL)


SELECT DISTINCT(SELECT COUNT(Kd_Peminjaman) FROM T_Peminjaman WHERE STATUS='DIKEMBALIKAN'  AND T_Peminjaman.Tgl_Kembali BETWEEN '2020-07-01' AND LAST_DAY('2020-07-01')) AS 'BanyakPengembalian',
(SELECT COUNT(Kd_Peminjaman) FROM T_Peminjaman WHERE STATUS = 'HILANG' AND T_Peminjaman.Tgl_Kembali BETWEEN '2020-07-01' AND LAST_DAY('2020-07-01')) AS 'BanyakHilang',
(SELECT COUNT(Kd_Peminjaman) FROM T_Peminjaman WHERE STATUS = 'DIKEMBALIKAN' AND T_Peminjaman.Tgl_Kembali BETWEEN '2020-07-01' AND LAST_DAY('2020-07-01')) AS 'TotalPengembalian',
(SELECT COUNT(Kd_Peminjaman) FROM T_Peminjaman WHERE STATUS = 'DIPINJAM' AND T_Peminjaman.Estimasi_Pengembalian BETWEEN '2020-07-01' AND LAST_DAY('2020-07-01')) AS 'BelumDikembalikan', 
(SELECT COUNT(Kd_Peminjaman) FROM T_Peminjaman WHERE T_Peminjaman.Tgl_Pinjam BETWEEN '2020-07-01' AND LAST_DAY('2020-07-01')) AS 'BanyakPinjaman';

  SELECT COUNT(Kd_Peminjaman) AS Total_Pinjaman, Status_Anggota
                            FROM T_Peminjaman,T_Anggota
                            WHERE T_Peminjaman.Kd_Anggota = T_Anggota.Kd_Anggota
                            AND T_Anggota.Kd_Anggota = '10118227' 
                            AND STATUS='DIPINJAM';


SELECT Kd_Peminjaman AS 'Banyak_Kode' FROM T_Peminjaman ORDER BY banyak_kode DESC LIMIT 1 WHERE MONTH(Tgl_Pinjam) = '" + Bulan + "' AND YEAR(Tgl_Pinjam) = '" + Tahun + "'

SELECT (Kd_Peminjaman) AS 'Banyak_Kode' FROM T_Peminjaman WHERE MONTH(Tgl_Pinjam) = '08-01' AND YEAR(Tgl_Pinjam) = '" + Tahun + "' ORDER BY Banyak_Kode DESC


--------------------------------------------------------------------------------------TAMPIL_DATA-----------------------------------------------------------------------------------------------------------
//DATA_BELUM_DIKEMBALIKAN
SELECT Kd_Peminjaman, Nama_anggota, Judul_Koleksi, DATEDIFF(NOW(), T_Peminjaman.Estimasi_Pengembalian) AS 'Keterlambatan1', IF(DATEDIFF(NOW(), T_Peminjaman.Estimasi_Pengembalian) < 0, 0, DATEDIFF(NOW(), T_Peminjaman.Estimasi_Pengembalian)) AS 'Keterlambatan'
FROM T_Peminjaman JOIN T_Koleksi USING(Kd_Koleksi) JOIN T_Anggota USING(Kd_Anggota) 
WHERE T_Peminjaman.Status ='DIPINJAM'

//DATA_BANYAK_PEMINJAMAN
SELECT COUNT(Kd_Peminjaman)AS 'BanyakPeminjaman', Nama_Anggota
FROM T_Peminjaman JOIN T_Anggota USING (Kd_Anggota)
WHERE T_Peminjaman.Tgl_Pinjam BETWEEN '2020-08-01' AND  LAST_DAY('2020-08-01')
GROUP BY Kd_Anggota
SELECT COUNT(Kd_Peminjaman)AS 'BanyakPeminjaman', Nama_Anggota FROM T_Peminjaman JOIN T_Anggota USING (Kd_Anggota) GROUP BY Kd_Anggota

// DATA_PEMINJAMAN
SELECT T_Peminjaman.Kd_Peminjaman, T_Anggota.Nama_Anggota, T_Koleksi.Kd_Koleksi, T_Peminjaman.Estimasi_Pengembalian, T_Peminjaman.Status
FROM T_Peminjaman JOIN T_Anggota USING(Kd_Anggota) JOIN T_Koleksi USING(Kd_Koleksi)
WHERE T_Peminjaman.Tgl_Pinjam BETWEEN '" + Tanggal + "' AND LAST_DAY('" + Tanggal + "')

//DATA_PENGEMBALIAN
SELECT T_Peminjaman.Kd_Peminjaman, T_Anggota.Nama_Anggota, T_Koleksi.Judul_Koleksi 
FROM T_Peminjaman JOIN T_Anggota USING(Kd_Anggota) JOIN T_Koleksi USING(Kd_Koleksi)
WHERE T_Peminjaman.Status != 'DIPINJAM' AND
T_Peminjaman.Tgl_Kembali BETWEEN '" + Tanggal + "' AND LAST_DAY('" + Tanggal + "')


----------------------------------------------------------------------------------------REPORT-------------------------------------------------------------------------------------------------------------
//IREPORT

-Banyak Peminjaman     
Query1             
IF($P{bds} = 'Bulan') THEN
SELECT COUNT(Kd_Peminjaman)AS 'BanyakPeminjaman', Nama_Anggota
FROM T_Peminjaman JOIN T_Anggota USING (Kd_Anggota)
WHERE tgl_pinjam BETWEEN $P{tgl} AND LAST_DAY($P{tgl})
GROUP BY Kd_Anggota;
ELSEIF($P{bds} = 'Triwulan') THEN
SELECT COUNT(Kd_Peminjaman)AS 'BanyakPeminjaman', Nama_Anggota
FROM T_Peminjaman JOIN T_Anggota USING (Kd_Anggota)
WHERE tgl_pinjam BETWEEN $P{tgl} AND LAST_DAY($P{tgl}+ INTERVAL 2 MONTH)
GROUP BY Kd_Anggota;
ELSEIF($P{bds} = 'Semester') THEN
SELECT COUNT(Kd_Peminjaman)AS 'BanyakPeminjaman', Nama_Anggota
FROM T_Peminjaman JOIN T_Anggota USING (Kd_Anggota)
WHERE tgl_pinjam BETWEEN $P{tgl} AND LAST_DAY($P{tgl}+ INTERVAL 5 MONTH)
GROUP BY Kd_Anggota;
ELSEIF($P{bds} = 'Tahun') THEN
SELECT COUNT(Kd_Peminjaman)AS 'BanyakPeminjaman', Nama_Anggota
FROM T_Peminjaman JOIN T_Anggota USING (Kd_Anggota)
WHERE tgl_pinjam BETWEEN $P{tgl} AND LAST_DAY($P{tgl}+ INTERVAL 11 MONTH)
GROUP BY Kd_Anggota;
END IF;

-DATA_PEMINJAMAN
IF($P{bds} = 'Bulan') THEN
SELECT T_Peminjaman.Kd_Peminjaman, T_Anggota.Nama_Anggota, T_Koleksi.Kd_Koleksi, T_Peminjaman.Estimasi_Pengembalian, T_Peminjaman.Status
FROM T_Peminjaman JOIN T_Anggota USING(Kd_Anggota) JOIN T_Koleksi USING(Kd_Koleksi)
WHERE tgl_pinjam BETWEEN $P{tgl} AND LAST_DAY($P{tgl});
ELSEIF($P{bds} = 'Triwulan') THEN
SELECT COUNT(Kd_Peminjaman)AS 'BanyakPeminjaman', Nama_Anggota
FROM T_Peminjaman JOIN T_Anggota USING (Kd_Anggota)
WHERE tgl_pinjam BETWEEN $P{tgl} AND LAST_DAY($P{tgl}+ INTERVAL 2 MONTH);
ELSEIF($P{bds} = 'Semester') THEN
SELECT COUNT(Kd_Peminjaman)AS 'BanyakPeminjaman', Nama_Anggota
FROM T_Peminjaman JOIN T_Anggota USING (Kd_Anggota)
WHERE tgl_pinjam BETWEEN $P{tgl} AND LAST_DAY($P{tgl}+ INTERVAL 5 MONTH);
ELSEIF($P{bds} = 'Tahun') THEN
SELECT COUNT(Kd_Peminjaman)AS 'BanyakPeminjaman', Nama_Anggota
FROM T_Peminjaman JOIN T_Anggota USING (Kd_Anggota)
WHERE tgl_pinjam BETWEEN $P{tgl} AND LAST_DAY($P{tgl}+ INTERVAL 11 MONTH);
END IF;

-DATA_PENGEMBALIAN
IF($P{bds} = 'Bulan') THEN
SELECT T_Peminjaman.Kd_Peminjaman, T_Anggota.Nama_Anggota, T_Koleksi.Judul_Koleksi 
FROM T_Peminjaman JOIN T_Anggota USING(Kd_Anggota) JOIN T_Koleksi USING(Kd_Koleksi)
WHERE T_Peminjaman.Status != 'DIPINJAM' AND
Tgl_Kembali BETWEEN $P{tgl} AND LAST_DAY($P{tgl});
ELSEIF($P{bds} = 'Triwulan') THEN
SELECT T_Peminjaman.Kd_Peminjaman, T_Anggota.Nama_Anggota, T_Koleksi.Judul_Koleksi 
FROM T_Peminjaman JOIN T_Anggota USING(Kd_Anggota) JOIN T_Koleksi USING(Kd_Koleksi)
WHERE T_Peminjaman.Status != 'DIPINJAM' AND
Tgl_Kembali BETWEEN $P{tgl} AND LAST_DAY($P{tgl}+ INTERVAL 2 MONTH);
ELSEIF($P{bds} = 'Semester') THEN
SELECT T_Peminjaman.Kd_Peminjaman, T_Anggota.Nama_Anggota, T_Koleksi.Judul_Koleksi 
FROM T_Peminjaman JOIN T_Anggota USING(Kd_Anggota) JOIN T_Koleksi USING(Kd_Koleksi)
WHERE T_Peminjaman.Status != 'DIPINJAM' AND
Tgl_Kembali BETWEEN $P{tgl} AND LAST_DAY($P{tgl}+ INTERVAL 5 MONTH);
ELSEIF($P{bds} = 'Tahun') THEN
SELECT T_Peminjaman.Kd_Peminjaman, T_Anggota.Nama_Anggota, T_Koleksi.Judul_Koleksi 
FROM T_Peminjaman JOIN T_Anggota USING(Kd_Anggota) JOIN T_Koleksi USING(Kd_Koleksi)
WHERE T_Peminjaman.Status != 'DIPINJAM' AND
Tgl_Kembali BETWEEN $P{tgl} AND LAST_DAY($P{tgl}+ INTERVAL 11 MONTH);
END IF;


SELECT COUNT(Kd_Peminjaman) AS Total_Pinjaman, Status_Anggota
                            FROM T_Peminjaman,T_Anggota
                            WHERE T_Peminjaman.Kd_Anggota = T_Anggota.Kd_Anggota
                            AND T_Anggota.Kd_Anggota = '10118227' AND STATUS='DIPINJAM'