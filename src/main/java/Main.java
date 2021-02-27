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
        System.out.println("Received message!");
        if(event.getMessage().getContentRaw().equals("!ping")) {
            event.getChannel().sendMessage("Pong!").queue();
        }
    }
}
