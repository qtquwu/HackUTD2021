package utils;

public class Reminder implements Comparable<Reminder>{
    private final String GUILD;
    private final String CHANNEL;
    private final String REMINDER;
    private final long TIME;


    public Reminder(String guild, String channel, String reminder, long time){
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

    public void sendReminder() throws InterruptedException {
        wait(TIME);
    }
}