package com.github.soniex2.pastebout;

import org.pircbotx.PircBotX;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.InviteEvent;
import org.pircbotx.hooks.events.MessageEvent;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author soniex2
 */
public class PBListener extends ListenerAdapter {

    Pattern pastebin = Pattern.compile("(^| )http://pastebin.com/([a-zA-Z0-9]+)($| )");

    @Override
    public void onMessage(MessageEvent event) throws Exception {
        Matcher m = pastebin.matcher(event.getMessage());
        PircBotX bot = event.getBot();
        HashSet<String> seen = new HashSet<String>();
        while (m.find()) {
            String id = m.group(2);

            if (seen.contains(id)) continue;
            seen.add(id);

            String target = "http://pastebin.com/raw.php?i=" + id;

            String batch = "\001BATCH " + id + "-" + event.getId() + "\001";
            String batchend = "\001BATCHEND " + id + "-" + event.getId() + "\001";

            URL url = new URL(target);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            String lastLine = null;
            while ((line = rd.readLine()) != null) {
                if (lastLine != null) event.respondChannel(batch + lastLine);
                lastLine = line;
            }
            event.respondChannel(batchend + lastLine);
            rd.close();
        }
    }

    @Override
    public void onInvite(InviteEvent event) throws Exception {
        event.getBot().sendIRC().joinChannel(event.getChannel());
    }
}
