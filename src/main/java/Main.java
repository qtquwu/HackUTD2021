import net.dv8tion.jda.api.AccountType;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.GenericGuildMessageEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import javax.security.auth.login.LoginException;

public class Main extends ListenerAdapter {
    public static void main(String[] args) throws LoginException {
        String token;
        if(args.length < 1) {
            System.out.println("First argument must be token!");
            System.exit(1);
        }
        token = args[0]; // args[0] should be token
        JDABuilder builder = JDABuilder.createDefault(token);
        builder.addEventListeners(new Main());
        builder.build();
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if(event.getAuthor().isBot()) return;
        System.out.println("Received message!");
        if(event.getMessage().getContentRaw().equals("!ping")) {
            event.getChannel().sendMessage("Pong!").queue();
        }
        if(event.getMessage().getContentRaw().equals("Help")){
            event.getChannel().sendMessage("To add reminder, Type: addReminder DD/MM 00:00pm").queue();
        }
        if(event.getMessage().getContentRaw().contains("addReminder")){
            String message = event.getMessage().getContentRaw();
            parse(message, event);
        }
    }

    static void parse(String message, MessageReceivedEvent event){
        message = message.substring(12);
        if(message.matches("[0-1][0-9]/[0-3][0-9]\s[0-5][0-9]:[0-5][0-9][ap]m"))
            event.getChannel().sendMessage("valid").queue();
        else event.getChannel().sendMessage("invalid" + message).queue();
    }

}
