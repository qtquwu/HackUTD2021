package utils;

public class Reminder implements Comparable<Reminder>{
    private String GUILD;
    private String CHANNEL;
    private String REMINDER;
    private long TIME;

    void Reminder(String guild, String channel, String reminder, long time){
        this.GUILD = guild;
        this.CHANNEL = channel;
        this.REMINDER = reminder;
        this.TIME = time;
    }

    public long getTIME(){
        return TIME;
    }

    public int compareTo(Reminder o) {
        return 0;
                //TIME.compareTo(o.getTIME());
    }
}