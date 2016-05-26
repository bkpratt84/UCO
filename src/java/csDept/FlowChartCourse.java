package csDept;

import java.util.ArrayList;

public class FlowChartCourse {

    public FlowChartCourse(String dept, int id, int level, int num, String val) {
        this.level = level;
        this.num = num;
        this.val = val;

        this.cid = dept + Integer.toString(id);
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getVal() {
        return val;
    }

    public void setVal(String val) {
        this.val = val;
    }

    public String getPre() {
        String ret = "";

        for (String s : pre) {
            ret = ret + " " + s;
        }

        return ret;
    }

    public void addPre(String dept, int id) {
        pre.add(dept + Integer.toString(id));
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getAvail() {
        String ret = "";

        for (String s : avail) {
            ret = ret + " " + s;
        }

        return ret;
    }

    public void addAvail(String season, int year) {
        avail.add(season + "-" + Integer.toString(year));
    }

    public boolean isLimited() {
        return !avail.isEmpty();
    }

    private int level;
    private int num;
    private String val;
    private String cid;
    private ArrayList<String> pre = new ArrayList();

    private ArrayList<String> avail = new ArrayList();
}
