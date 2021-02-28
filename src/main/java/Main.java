import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import utils.AnnouncementKey;
import utils.Assignment;
import utils.Reminder;

import javax.security.auth.login.LoginException;
import java.io.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

public class Main extends ListenerAdapter {
    static ArrayList<AnnouncementKey> announcementKeys = new ArrayList<>();
    static ArrayList<Assignment> assignments = new ArrayList<>();
    static JDA jda;

    public static void main(String[] args) throws LoginException {
        String token;
        if(args.length < 1) {
            System.out.println("First argument must be token!");
            System.exit(1);
        }
        token = args[0]; // args[0] should be token
        JDABuilder builder = JDABuilder.createDefault(token);
        builder.addEventListeners(new Main());
        jda = builder.build();
        // now jda is our jda instance, which we can use to get channels

        // now let's load data!
        File file = new File("data.txt");
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            Scanner s;
            String type;
            do {
                line = reader.readLine();
                if (line == null) break;
                s = new Scanner(line).useDelimiter("\\\\");
                type = s.next();

                if (type.equals("Assignment")) {
                    assignments.add(new Assignment(line));
                } else if (type.equals("AnnouncementKey")) {
                    announcementKeys.add(new AnnouncementKey(line));
                }

            } while (true); // This isn't a forever loop; it will always break at the end of the file

        } catch (Exception e) {
            System.out.println("An Exception occurred");
            e.printStackTrace();
        }
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if(event.getAuthor().isBot()) return;

        Scanner s = new Scanner(event.getMessage().getContentRaw()).useDelimiter(" ");
        String command = s.next();
        if(command.charAt(0) != '!') {
            return;
        }

        if(command.equals("!reminder")){
            String message = event.getMessage().getContentRaw();
            String date = (s.next() + "/" + (new Date().getYear()+1900) + " " + s.next());
            System.out.println(date);
            s.useDelimiter("\\A");
            String remind = s.next();
            DateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mma");
            try {
                Date dt = sdf.parse(date);
                System.out.println(dt.toString());
                Date now = new Date();
                System.out.println(now.toString());
                long milliseconds = (dt.getTime()-now.getTime());
                System.out.println(milliseconds);
                Reminder rem = new Reminder(event.getGuild().toString(), event.getChannel().toString(), remind, milliseconds);
                //rem.sendReminder();
                try {
                    java.util.concurrent.TimeUnit.SECONDS.sleep(milliseconds/1000);
                } catch (InterruptedException e) {
                    System.out.println("Oops again");
                    e.printStackTrace();
                }
                event.getChannel().sendMessage(remind).queue();
            } catch (ParseException e) {
                System.out.println("Oops");
                e.printStackTrace();
            }
            return;
        }

        //assignment command; identical to reminder but more specific use case
        if(command.equals("!assignment")){
            String message = event.getMessage().getContentRaw();
            String date = (s.next() + "/" + (new Date().getYear()+1900) + " " + s.next());
            System.out.println(date);
            s.useDelimiter("\\A");
            String name = s.next();
            DateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mma");
            try {
                Date dt = sdf.parse(date);
                System.out.println(dt.toString());
                Date now = new Date();
                System.out.println(now.toString());
                long milliseconds = (dt.getTime()-now.getTime());
                System.out.println(milliseconds);
                Assignment assignment = new Assignment(event.getChannel().getId(), name, dt.getTime());
                assignments.add(assignment);
                //rem.sendReminder();
                try {
                    java.util.concurrent.TimeUnit.SECONDS.sleep(milliseconds/1000);
                } catch (InterruptedException e) {
                    System.out.printf("Oops again");
                    e.printStackTrace();
                }
                event.getChannel().sendMessage(name + " is due!").queue();
                assignments.remove(assignment);
            } catch (ParseException e) {
                System.out.println("Oops");
                e.printStackTrace();
            }
            return;
        }

        //main menu
        if (event.getMessage().getContentRaw().equals("!help")) {
            event.getChannel().sendMessage("Welcome to the UTD Chat Bot! \n" +
                    "To receive reminders, type: !reminder MM/dd 00:00(AM/PM) Message" +"\n" +
                    "To receive announcements, use !announcement" + "\n" +
                    "Need health or study resources? Use !resources for options" + "\n" +
                    "Thank you for using our chat bot!").queue();
        }

        //resources
        if (event.getMessage().getContentRaw().equals("!resources")) {
            event.getChannel().sendMessage("Listed below are student resources provided by UTD: \n" +
                    "Student Counseling Center- If you are experiencing a life-threatening emergency, please call 911.\n" +
                    "If you are in crisis and need to speak to a mental health professional, call UTD TALK (972)-883-8255." +"\n" +
                    "Need a consultation or an appointment? Visit https://www.utdallas.edu/counseling/ to set one up" + "\n\n\n" +
                    "Student Health Center - Need to visit a doctor? Appointments are available 8am - 5pm, Monday to Friday. See " +
                    "https://www.utdallas.edu/healthcenter/ for setting up appointments and more health resources" + "\n\n\n" +
                    "Student Success Center - Want individual or group tutoring to help with classes? Check out the " +
                    "available options at https://studentsuccess.utdallas.edu/programs/" + "\n\n" +
                    "Need other resources? Visit https://www.utdallas.edu/diversity/data-and-resources/campus-resources.html").queue();
        }

        if(command.equals("!announcement")) {
            String announcementRequest;
            announcementRequest = s.next();
            if (announcementRequest.equals("register")) {
                String keycode = s.next();
                announcementKeys.add(new AnnouncementKey(event.getChannel().getId(), keycode));
                try {
                    saveHeap("data.txt");
                } catch (IOException e) {
                    System.out.println("An error occurred");
                    e.printStackTrace();
                }
                event.getChannel().sendMessage("You have registered for announcements from " + keycode + "."
                                + "\n Further announcements will be sent to this channel.").queue();
            }
            if (announcementRequest.equals("send")) {
                String keycode = s.next();
                s.useDelimiter("\\A");
                String message = "" + s.next();
                for (AnnouncementKey announcementKey : announcementKeys) {
                    if (announcementKey.listeningFor(keycode)) {
                        jda.getTextChannelById(announcementKey.getChannelID()).sendMessage(
                                "**------------Announcement from " + keycode + "------------**\n" +
                                message +
                                "\n**---------------------------------------------------------**\n"
                        ).queue();
                    }
                }
            }
        }
    }


    public void saveHeap(String filename) throws IOException {
        File file = new File(filename);
        file.createNewFile();
        FileWriter fileWriter = new FileWriter(file, false);
        for (Assignment assignment : assignments) {
            fileWriter.write(assignment.getSaveString() + "\n"); // Hopefully it puts in newlines after each write but we'll see
        }
        for (AnnouncementKey announcementKey : announcementKeys) {
            fileWriter.write(announcementKey.getSaveString() + "\n");
        }
        fileWriter.close();
    }

}
