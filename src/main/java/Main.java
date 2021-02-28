import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import utils.AnnouncementKey;

import javax.security.auth.login.LoginException;
import java.util.ArrayList;
import java.util.Scanner;

public class Main extends ListenerAdapter {
    ArrayList<AnnouncementKey> announcementKeys = new ArrayList<>();
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
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if(event.getAuthor().isBot()) return;
        if(event.getMessage().getContentRaw().contains("addReminder")){
            String message = event.getMessage().getContentRaw();
            parse(message, event);
        }

        Scanner s = new Scanner(event.getMessage().getContentRaw()).useDelimiter(" ");
        String command = s.next();
        if(command.charAt(0) != '!') {
            return;
        }

        if(command.equals("!ping")) {
            event.getChannel().sendMessage("Pong!").queue();
        }

        //main menu
        if (event.getMessage().getContentRaw().equals("!menu")) {
            event.getChannel().sendMessage("Welcome to the UTD Chat Bot! \n" +
                    "To get help with assignment reminders, use !help \n" +
                    "To receive announcements, use !announcement").queue();
        }

        if(command.equals("!help")){
            event.getChannel().sendMessage("To add reminder, Type: addReminder DD/MM 00:00(am/pm)").queue();
        }
        if(command.equals("!announcement")) {
            String announcementRequest;
            announcementRequest = s.next();
            if (announcementRequest.equals("register")) {
                String keycode = s.next();
                announcementKeys.add(new AnnouncementKey(event.getChannel().getId(), keycode));
                event.getChannel().sendMessage("Channel ID: " + event.getChannel().getId()).queue();
            }
            if (announcementRequest.equals("send")) {
                String keycode = s.next();
                s.useDelimiter("\\A");
                String message = "" + s.next();
                for (AnnouncementKey announcementKey : announcementKeys) {
                    if (announcementKey.listeningFor(keycode)) {
                        jda.getTextChannelById(announcementKey.getChannelID()).sendMessage(message).queue();
                    }
                }
            }
        }
    }

    static void parse(String message, MessageReceivedEvent event){
        message = message.substring(12);
        if(message.matches("[0-1][0-9]/[0-3][0-9]\\s[0-5][0-9]:[0-5][0-9][ap]m"))
            event.getChannel().sendMessage("valid").queue();
        else event.getChannel().sendMessage("invalid" + message).queue();
    }

}
