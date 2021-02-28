package utils;

import java.time.*;
import java.util.Scanner;

/*
    The Assignment class is used to store information about assignments, remembering everything about them in order to determine
    When to remind students about them
 */
public class Assignment implements StringSaveable {
    protected String channelID;
    protected String name;

    protected long instantDue;

    boolean sentWeekReminder = false;
    boolean sentDayReminder = false;
    boolean sent4hrReminder = false;
    /*
    Constructor Arguments:
    guild: the guild (server) the assignment storage is made in
    name: the title of the assignment
    LocalDate: the day the assignment is due
    Time: the time the assignment is due (Dallas Time)
     */
    public Assignment(String channelId, String Name, LocalDate Date, LocalTime Time) {
        channelID = channelId; name = Name; // use input to define members

        // Determine the instant the assignment is due from the date
        boolean isDST = ZoneId.of("America/Dallas").getRules().isDaylightSavings(Instant.from(Date));
        instantDue = Date.toEpochSecond(Time, ZoneOffset.ofHours(-6 + (isDST ? 1 : 0)));
    }
    /*
    This constructor uses the instant the assignment is due instead of the date/time; useful for internal workings, not
    for input from other sources
     */
    public Assignment(String channelId, String Name, long instant) {
        channelID = channelId; name = Name; instantDue = instant;
    }
    /*
    This constructor creates the class based on a save string. This is fairly simple due to the format (defined under getSaveString)
     */
    public Assignment(String saveString) {
        Scanner s = new Scanner(saveString).useDelimiter("\\\\");
        s.next(); // The first value in the savestring just tells us that the string is an assignment. We already know this!
        channelID = s.next();
        name = s.next();
        instantDue = s.nextLong();

    }

    public String getChannelID() {
        return channelID;
    }
    public String getName() {
        return name;
    }

    public long timeUntilDue() {
        long now = Instant.now().getEpochSecond();
        return instantDue - now;
    }

    public short daysUntilDue() {
        short ret;
        ret = (short) ((double) timeUntilDue() / 60.0 / 60.0 / 24.0);
        return ret;
    }
    public int hoursUntilDue() {
        int ret;
        ret = (int) ((double) timeUntilDue() / 60.0 / 60.0);
        return ret;
    }

    public String getSaveString() {
        // Save string should first say the class, then the arguments to create it
        // Arguments delineated by backslashes (to allow for whitespace in assignment names)
        String saveString;
        saveString = "Assignment\\";
        saveString += channelID + "\\";
        saveString += name + "\\";
        saveString += instantDue + "\\";

        return saveString;
    }

    // Just some setters
    public void setSentWeekReminder() {
        sentWeekReminder = true;
    }
    public void setSentWeekReminder(boolean sent) {
        sentWeekReminder = sent;
    }
    public void setSentDayReminder() {
        sentDayReminder = true;
    }
    public void setSentDayReminder(boolean sent) {
        sentDayReminder = sent;
    }
    public void setSent4hrReminder() {
        sent4hrReminder = true;
    }
    public void setSent4hrReminder(boolean sent) {
        sent4hrReminder = sent;
    }

    // Determine if the bot should send a reminder based on how long until it's due and if it's already sent a reminder
    public boolean shouldSendWeekReminder() {
        return !sentWeekReminder && (daysUntilDue() <= 7);
    }
    public boolean shouldSendDayReminder() {
        return !sentDayReminder && (daysUntilDue() <= 1);
    }
    public boolean shouldSend4hrReminder() {
        return !sent4hrReminder && (hoursUntilDue() <= 4);
    }

}
