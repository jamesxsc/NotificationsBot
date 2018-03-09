package DiscordBots.NotificationsOpt;

import java.awt.Color;

import javax.security.auth.login.LoginException;

import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class App extends ListenerAdapter {
	
	static GetConfigValues configGetter = new GetConfigValues();
	static GetConfigValues cfgLoader = new GetConfigValues();
	
	public static void main(String[] args) throws LoginException, InterruptedException {
		cfgLoader.copyConfig();
		String[] config = configGetter.getPropValues().split(",");
		System.out.println("NotificationsBot is now loading! Please keep an eye on the console for any errors.");
		JDA jdaBot = new JDABuilder(AccountType.BOT).setToken(config[4]).buildBlocking();
		jdaBot.addEventListener(new App());
		jdaBot.getPresence().setGame(Game.watching("for notifications"));
	}
	
	String[] config = configGetter.getPropValues().split(",");
	
	@Override
	public void onMessageReceived(MessageReceivedEvent e) {
		Message msg = e.getMessage();
		MessageChannel channel = e.getChannel();
		User user = e.getAuthor();
		String prefix = config[0];
		String notificationRoleName = config[1];
		if (msg.getContentRaw().startsWith(prefix) && (!(channel.getId().equals(config[2]))) && (config[3].equals("false"))) {
			EmbedBuilder channelErrorB = new EmbedBuilder();
			channelErrorB.clear();
			channelErrorB.setColor(new Color(218, 47, 34));
			channelErrorB.addField("**Configuration Error:**", "*You are attempting to run a command using the prefix for NotificationsBot in the wrong channel, if this is the channel you wish to use, then make sure you have correctly copied the ID of the channel into the configuration file. If you have several bots wishing to use the same prefix, please first check that they have no conflicting commands and then set the option `compatibility` in the config file to `true`, this will prevent this warning from showing up, by default it is set to false. If this issue persists, please join the 615283 Discord server at {s_url} and submit a support ticket in #support.*", true);
			channel.sendMessage(channelErrorB.build()).queue();
		}
		if (msg.getContentRaw().contains(prefix) && (channel.getId().equals(config[2]))) {
			MessageChannel configChannel = e.getJDA().getTextChannelById(config[2]);
			if (channel.equals(configChannel) && !(user.isBot())) {
				if (!(e.getGuild().getRoles().toString().contains(notificationRoleName))) {
					EmbedBuilder errorB = new EmbedBuilder();
					errorB.clear();
					errorB.setColor(new Color(218, 47, 34));
					errorB.addField("**Configuration Error:**", "*The role in the configuration file does not exist or has been configured incorrectly. Please check that the role exists before continuing use with this bot. If you think that you have configured it correctly, please join the 615283 Discord server at {s_url} and submit a support ticket in #support.*", true);
					channel.sendMessage(errorB.build()).queue();
					return;
				}
				if (msg.getContentRaw().equalsIgnoreCase(prefix + "optin")) {
					Member m = e.getGuild().getMembersByEffectiveName(user.getName().trim(), false).get(0);
					Role nr = e.getGuild().getRolesByName(notificationRoleName, true).get(0);
					if (m.getRoles().contains(nr)) {
						EmbedBuilder priorB = new EmbedBuilder();
						priorB.clear();
						priorB.setColor(new Color(218, 47, 34));
						priorB.addField("Error", user.getAsMention() + ", you are already opted into notifications.", false);
						channel.sendMessage(priorB.build()).queue();
						priorB.clear();
					}
					else {
						e.getGuild().getController().addSingleRoleToMember(m, nr).queue();
						EmbedBuilder aftB = new EmbedBuilder();
						aftB.clear();
						aftB.setColor(new Color(51, 198, 40));
						aftB.addField("Success", user.getAsMention() + ", you have successfully been opted into notifications", false);
						channel.sendMessage(aftB.build()).queue();
					}
				}	
				if (msg.getContentRaw().equalsIgnoreCase(prefix + "optout")) {
					Member m2 = e.getGuild().getMembersByEffectiveName(user.getName().trim(), false).get(0);
					Role nr2 = e.getGuild().getRolesByName(notificationRoleName, true).get(0);
					if (!(m2.getRoles().contains(nr2))) {
						EmbedBuilder priorB = new EmbedBuilder();
						priorB.clear();
						priorB.setColor(new Color(218, 47, 34));
						priorB.addField("Error", user.getAsMention() + ", you are already opted out of notifications.", false);
						channel.sendMessage(priorB.build()).queue();
						priorB.clear();
					}
					else {
						e.getGuild().getController().removeSingleRoleFromMember(m2, nr2).queue();
						EmbedBuilder aftB = new EmbedBuilder();
						aftB.clear();
						aftB.setColor(new Color(51, 198, 40));
						aftB.addField("Success", user.getAsMention() + ", you have successfully been opted out of notifications", false);
						channel.sendMessage(aftB.build()).queue();
					}
				}
				if (msg.getContentRaw().equalsIgnoreCase(prefix + "help") && config[3].equals("false")) {
					EmbedBuilder helpB = new EmbedBuilder();
					helpB.clear();
					helpB.setColor(new Color(0, 150, 255));
					helpB.addField("Help", "**Prefix:**\nThe prefix for notifications bot is currently `" + prefix + "`\n\n**Commands are as follows:**\n`optin` - Opts a user into notifications.\n`optout` - Opts a user out of notifications.\n`help` - Displays this message.", true);
					channel.sendMessage(helpB.build()).queue();
				}
			}
			return;
		}
	}

}
