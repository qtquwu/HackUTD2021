package utils;

import java.time.*;
import java.util.Scanner;

import static java.time.ZoneId.*;

/*
    The Assignment class is used to store information about assignments, remembering everything about them in order to determine
    When to remind students about them
 */
public class Assignment {
    protected String guild;
    protected String name;
    //protected LocalDate date;
    //protected LocalTime time;
    protected long instantDue;
    /*
    Constructor Arguments:
    guild: the guild (server) the assignment storage is made in
    name: the title of the assignment
    LocalDate: the day the assignment is due
    Time: the time the assignment is due (Dallas Time)
     */
    public Assignment(String Guild, String Name, LocalDate Date, LocalTime Time) {
        guild = Guild; name = Name; //date = Date; time = Time; // use input to define members

        // Determine the instant the assignment is due from the date
        boolean isDST = ZoneId.of("America/Dallas").getRules().isDaylightSavings(Instant.from(Date));
        instantDue = Date.toEpochSecond(Time, ZoneOffset.ofHours(-6 + (isDST ? 1 : 0)));
    }
    /*
    This constructor uses the instant the assignment is due instead of the date/time; useful for internal workings, not
    for input from other sources
     */
    public Assignment(String Guild, String Name, long instant) {
        guild = Guild; name = Name; instantDue = instant;
    }
    /*
    This constructor creates the class based on a save string. This is fairly simple due to the format (defined under getSaveString)
     */
    public Assignment(String saveString) {
        Scanner s = new Scanner(saveString).useDelimiter("\\\\");
        s.next(); // The first value in the savestring just tells us that the string is an assignment. We already know this!
        guild = s.next();
        name = s.next();
        instantDue = s.nextLong();

    }

    public long timeUntilDue() {
        long ret;
        long now = Instant.now().getEpochSecond();
        return instantDue - now;
    }

    public short daysUntilDue() {
        short ret;
        ret = (short) ((double) timeUntilDue() / 60.0 / 60.0 / 24.0);
        return ret;
    }

    public String getSaveString() {
        // Save string should first say the class, then the arguments to create it
        String saveString;
        saveString = "Assignment\\";
        saveString += guild + "\\";
        saveString += name + "\\";
        saveString += instantDue + "\\";

        return saveString;
    }


}
