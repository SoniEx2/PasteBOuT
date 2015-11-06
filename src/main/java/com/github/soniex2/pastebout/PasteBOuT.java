package com.github.soniex2.pastebout;

import org.pircbotx.Configuration;
import org.pircbotx.PircBotX;
import org.pircbotx.exception.IrcException;

import javax.net.ssl.SSLSocketFactory;
import java.io.IOException;

/**
 * @author soniex2
 */
public class PasteBOuT {

    public static void main(String[] args) {
        Configuration config = new Configuration.Builder()
                // bot info
                .setName("PasteBOuT")
                .setLogin("PasteBOuT")
                .setAutoNickChange(true)
                // channel info TODO refactor
                .addAutoJoinChannel("#soni")
                // force SSL
                .setSocketFactory(SSLSocketFactory.getDefault())
                // servers
                .addServer("irc.esper.net", 6697)
                // listeners
                .addListener(new PBListener())
                .buildConfiguration();

        // esper bot TODO refactor
        PircBotX esperBot = new PircBotX(config);
        try {
            esperBot.startBot();
        } catch (IrcException | IOException e) {
            e.printStackTrace();
        }
    }
}
