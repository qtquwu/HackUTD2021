import net.dv8tion.jda.api.AccountType;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.GenericGuildMessageEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import javax.security.auth.login.LoginException;

public class Main extends ListenerAdapter {
    public static void main(String[] args) throws LoginException {
        JDABuilder builder = new JDABuilder(AccountType.BOT);
        String token = "(INSERT BOT TOKEN HERE!)";
        builder.setToken(token);
        builder.addEventListeners(new Main());
        builder.build();
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        System.out.println("Received message!");
        if(event.getMessage().getContentRaw().equals("!ping")) {
            event.getChannel().sendMessage("Pong!").queue();
        }
    }


    // Not working with Guilds yet - figure out how
    @Override
    public void onGenericGuildMessage(@NotNull GenericGuildMessageEvent event) {
        super.onGenericGuildMessage(event);
        System.out.println(event.getMessageId());
        if(event.getMessageId().equals("!ping")) {
            event.getChannel().sendMessage("Pong!").queue();
        }
    }
}
