SELECT T_Pemindatediff(NOW(), Tgl_Pinjam) FROM t_peminjaman

SELECT Kd_Peminjaman, T_Peminjaman.Kd_Koleksi, Judul_Koleksi, Tgl_Pinjam,DATEDIFF(NOW(), t_peminjaman.Estimasi_Pengembalian)  AS 'Keterlamabatan'
FROM T_Peminjaman , T_Koleksi
WHERE T_Koleksi.Kd_Koleksi = T_Peminjaman.Kd_Koleksi

SELECT COUNT(Kd_Peminjaman)AS Total_Pinjaman,T_Anggota.Kd_Anggota,Status_Anggota  FROM T_Peminjaman,T_Anggota WHERE T_Peminjaman.Kd_Anggota = T_Anggota.Kd_Anggota

SELECT * FROM T_Koleksi, T_Peminjaman WHERE T_Koleksi.Kd_Koleksi = T_Peminjaman.Kd_Koleksi AND
 Kd_Anggota LIKE '%S%' OR Judul_Koleksi LIKE '%s%' AND T_Peminjaman.Status = 'DIPINJAM'
ORDER BY Judul_Koleksi ASC

SELECT COUNT(Kd_Peminjaman) AS 'Banyak_Kode' FROM T_Peminjaman WHERE MONTH(Tgl_Pinjam) = '5' AND YEAR(Tgl_Pinjam) = '2020'

//DATA
SELECT T_Peminjaman.Kd_Peminjaman, T_Anggota.Nama_Anggota, T_Koleksi.Judul_Koleksi
FROM T_Peminjaman JOIN T_Anggota USING(Kd_Anggota) JOIN T_Koleksi USING(Kd_Koleksi)
WHERE T_Peminjaman.Status != 'DIPINJAM' AND T_Peminjaman.Tgl_Kembali BETWEEN '2020-07-01' AND LAST_DAY( '2020-07-01')




//DATA GLOBAL
SELECT DISTINCT(SELECT COUNT(Kd_Peminjaman) FROM T_Peminjaman WHERE STATUS='DIKEMBALIKAN') AS 'BanyakPengembalian', 
(SELECT COUNT(Kd_Peminjaman) FROM T_Peminjaman WHERE STATUS = 'HILANG') AS 'BanyakPengembalian',
(SELECT COUNT(Kd_Peminjaman) FROM T_Peminjaman WHERE STATUS!='DIPINJAM') AS 'TotalPengembalian',
(SELECT COUNT(Kd_Peminjaman) FROM T_Peminjaman WHERE STATUS='DIPINJAM') AS 'BelumDikembalikan', 
(SELECT COUNT(Kd_Peminjaman) FROM T_Peminjaman) AS 'BanyakPinjaman'
(SELECT COUNT(Kd_Peminjaman) FROM T_Peminjaman) AS 'BanyakPinjaman'
FROM t_peminjaman
WHERE T_Peminjaman.Tgl_Kembali BETWEEN '2020-07-01' AND LAST_DAY( '2020-07-01')