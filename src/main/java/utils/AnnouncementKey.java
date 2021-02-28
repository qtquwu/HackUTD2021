package utils;

import java.util.Scanner;

public class AnnouncementKey {
    protected String channelID;
    protected String keycode;

    public AnnouncementKey(String channelId, String keyCode) {
        channelID = channelId; keycode = keyCode;
    }

    //Build AnnouncementKey from savestring
    public AnnouncementKey(String savestring) {
        Scanner s = new Scanner(savestring).useDelimiter("\\\\");
        s.next(); //Discard first part - we already know this is an announcement key!

        channelID = s.next();
        keycode = s.next();
    }

    // Create a string to save data.
    // Formatted as [Class]\[Arg1]\[Arg2]\...\[ArgN]
    public String getSaveString() {
        String savestring = "AnnouncementKey\\";
        savestring += channelID + "\\";
        savestring += keycode + "\\";
        return savestring;
    }

    public boolean listeningFor(String key) {
        return key.equals(keycode);
    }
}
