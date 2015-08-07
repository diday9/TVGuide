package test.freelancer.com.fltest;

/**
 * Created by Android 18 on 8/7/2015.
 */
public class ProgramInfo {



    public String name;

    public String startTime;

    public String endTime;

    public String channel;

    public String rating;

    public ProgramInfo(String name, String start, String end, String ch, String rate){
        this.name = name;
        this.startTime = start;
        this.endTime = end;
        this.channel = ch;
        this.rating = rate;
    }


    public String getName(){
        return name;
    }
    public void setName(String n){
        name = n;
    }

    public String getStartTime(){
        return startTime;
    }
    public void setStartTime(String n){
        startTime = n;
    }

    public String getEndTime(){
        return endTime;
    }
    public void setEndTime(String n){
        endTime = n;
    }

    public String getChannel(){
        return channel;
    }
    public void setChannel(String n){
        channel = n;
    }

    public String getRating(){
        return rating;
    }
    public void setRating(String n){
        rating = n;
    }

}
