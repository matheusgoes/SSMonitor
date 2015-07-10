package goes.com.br.ssmonitor;

/**
 * Created by jaack05 on 19/04/15.
 */
public class Phone {

    private long id;
    private int mcc;
    private int mnc;
    private int torres, networkTypeCode;
    private double dbm, latitude, longitude;
    private String phoneType, operadora;
    private String netWorkType;
    private int cid;
    private int lac;
    private int ASU;


    public Phone() {
        mcc = mnc = torres = 0;
    }

    public void setDbm(double dbm) {
        this.dbm = dbm;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setMcc(int mcc) {
        this.mcc = mcc;
    }

    public void setMnc(int mnc) {
        this.mnc = mnc;
    }

    public void setNetWorkType(String netWorkType) {
        this.netWorkType = netWorkType;
    }

    public void setNetworkTypeCode(int networkTypeCode) {
        this.networkTypeCode = networkTypeCode;
    }

    public void setOperadora(String operadora) {
        this.operadora = operadora;
    }

    public void setPhoneType(String phoneType) {
        this.phoneType = phoneType;
    }

    public void setTorres(int torres) {
        this.torres = torres;
    }

    public double getDbm() {
        return dbm;
    }

    public int getMcc() {
        return mcc;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public int getMnc() {
        return mnc;
    }

    public int getNetworkTypeCode() {
        return networkTypeCode;
    }

    public int getTorres() {
        return torres;
    }

    public String getNetWorkType() {
        return netWorkType;
    }

    public String getOperadora() {
        return operadora;
    }

    public String getPhoneType() {
        return phoneType;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getCid() {
        return cid;
    }

    public void setCid(int cid) {
        this.cid = cid;
    }

    public int getLac() {
        return lac;
    }

    public void setLac(int lac) {
        this.lac = lac;
    }

    public int getASU() {
        return ASU;
    }

    public void setASU(int ASU) {
        this.ASU = ASU;
    }
}
