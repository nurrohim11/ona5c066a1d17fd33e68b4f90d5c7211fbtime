package gmedia.net.id.OnTime.utils;

public class ServerUrl {
//    public static String BaseURLLocalHost = "http://192.168.20.51/gmedia/ontime/";
    //public static String BaseURLLocalHost = "http://gmedia.bz/ontime/";
    public static String BaseURLLocalHost = "https://absenontime.com/";
    public static String UrlLogin = BaseURLLocalHost + "Rest/auth";
    public static String saveInstalledApp = BaseURLLocalHost + "Rest/save_app";
    public static String ScanAbsen = BaseURLLocalHost + "Rest_Scan/scan";
    public static String UpdateLocation = BaseURLLocalHost + "Rest_Scan/scan_location";
    public static String Profile = BaseURLLocalHost + "Rest_Profile/";
    public static String ListJadwalKerja = BaseURLLocalHost + "Rest_Jadwal/index";
    public static String jadwalHari = BaseURLLocalHost + "Rest_Jadwal/jadwal_today";
    public static String listPengumuman = BaseURLLocalHost + "Rest_News/index";
    public static String viewPengumuman = BaseURLLocalHost+"Rest_News/view_news";
    public static String listNews = BaseURLLocalHost + "Rest_News/view_news";
    public static String detailNews = BaseURLLocalHost + "Rest_News/detail_news";
    public static String addCuti = BaseURLLocalHost + "Rest_Cuti/add_cuti";
    public static String viewCuti = BaseURLLocalHost + "Rest_Cuti/index";
    public static String historyCuti = BaseURLLocalHost + "Rest_Cuti/history_cuti";
    public static String addIjin = BaseURLLocalHost + "Rest_Ijin/add_ijin";
    public static String viewIjin = BaseURLLocalHost + "Rest_Ijin/index";
    public static String historyIjin = BaseURLLocalHost + "Rest_Ijin/history_ijin";
    public static String viewAbsensi = BaseURLLocalHost + "Rest_Absensi/index";
    public static String viewLembur = BaseURLLocalHost + "Rest_Lembur/index";
    public static String addLembur = BaseURLLocalHost + "Rest_Lembur/add_lembur";
	public static String viewApprovalCuti = BaseURLLocalHost + "Rest_Cuti/view_approval";
	public static String listApprovalCuti = BaseURLLocalHost+"Rest_Cuti/list_approval";
    public static String viewApprovalIjin = BaseURLLocalHost + "Rest_Ijin/view_approval";
    public static String listApprovalIjin = BaseURLLocalHost + "Rest_Ijin/list_approval";
	public static String approvalCuti = BaseURLLocalHost + "Rest_Cuti/approve_cuti";
	public static String approvalIjin = BaseURLLocalHost + "Rest_Ijin/approve_ijin";
	public static String infoGaji = BaseURLLocalHost + "Rest_Gaji/";
	public static String viewScanlog = BaseURLLocalHost + "Rest_Scan/view_scan";
	public static String sisaCuti = BaseURLLocalHost + "Rest_Cuti/jumlah_cuti";
	public static String gantiPassword = BaseURLLocalHost + "Rest/change_password";
	public static String viewTerlambat = BaseURLLocalHost + "Rest_Absensi/terlambat";
	public static String upVersion = BaseURLLocalHost + "Rest/version";
	public static String urlIpPublic = "https://api.ipify.org";
	public static String updateLocationS = "https://erpsmg.gmedia.id/hrd/rest/track_location/";
	public static String urlPostReimburse = BaseURLLocalHost+"Rest_Reimburse/add_reimburse";
    public static String urlRiwayatReimburse = BaseURLLocalHost+"Rest_Reimburse/list_reimburse";
    public static String urlListApprovalReimburs = BaseURLLocalHost+"Rest_Reimburse/list_apv_reimburse";
    public static String urlPostApproval = BaseURLLocalHost+"Rest_Reimburse/approve_reimburse";
    public static String urlViewKunjunganTgl = BaseURLLocalHost+"Rest_Jadwal/kunjungan_tgl";
    public static String urlViewKunjunganHari = BaseURLLocalHost+"Rest_Jadwal/kunjungan_hari";
}
