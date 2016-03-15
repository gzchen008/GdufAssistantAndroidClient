package bean;

/**
 * Created by kami on 15/9/28.
 */
public class EachCourse {

    private String title;
    private String classroom;
    private int when;   //从第几节课开始1-12
    private int howlong;  //持续多少节课
    private int whichday;  //星期几上，0表示周日，1为周一 0-6
    private int whichweek; //0为不分单双周，1为单周，2为双周

    public EachCourse( String title, String classroom, int when, int howlong, int whichday, int whichweek) {
        this.title = title;
        this.classroom = classroom;
        this.when = when;
        this.howlong = howlong;
        this.whichday = whichday;
        this.whichweek = whichweek;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getClassroom() {
        return classroom;
    }

    public void setClassroom(String classroom) {
        this.classroom = classroom;
    }

    public int getWhen() {
        return when;
    }

    public void setWhen(int cwhen) {
        this.when = cwhen;
    }

    public int getHowlong() {
        return howlong;
    }

    public void setHowlong(int howlong) {
        this.howlong = howlong;
    }

    public int getWhichday() {
        return whichday;
    }

    public void setWhichday(int whichday) {
        this.whichday = whichday;
    }

    public int getWhichweek() {
        return whichweek;
    }

    public void setWhichweek(int whichweek) {
        this.whichweek = whichweek;
    }

}
