package bean;

/**
 * Created by kami on 15/9/28.
 */
public class Course {
    private int cid;
    private String stuId;
    private String year;
    private int xq;

    public Course(int cid, String stuId, String year, int xq) {
        this.cid = cid;
        this.stuId = stuId;
        this.year = year;
        this.xq = xq;
    }

    public int getCid() {
        return cid;
    }

    public void setCid(int cid) {
        this.cid = cid;
    }

    public String getStuId() {
        return stuId;
    }

    public void setStuId(String stuId) {
        this.stuId = stuId;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public int getXq() {
        return xq;
    }

    public void setXq(int xq) {
        this.xq = xq;
    }
}
