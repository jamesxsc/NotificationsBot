package DiscordBots.NotificationsOpt;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Properties;

public class GetConfigValues {
	
	String result = "";
 
	public void copyConfig() {
		File dest = new File("." + File.separator + "config.properties");
		File runDest = new File("." + File.separator + "runBot.bat");
		InputStream in = getClass().getResourceAsStream("/resources/config.properties"); 
		InputStream runIn = App.class.getClassLoader().getResourceAsStream("resources/runBot.bat");
		if (!(dest.exists())) {
			try {
				Files.copy(in, dest.toPath());
				System.out.println("Configuration file successfully copied.");
				System.out.println("Enter bot token in config file");
				System.exit(0);
			} catch (IOException e) {
				System.out.println("Error copying configuration file.");
				e.printStackTrace();
			}
		}
		else {
			System.out.println("Configuration file already exists! No need to copy.");
		}
		if (!(runDest.exists())) {
			try {
				Files.copy(runIn, runDest.toPath());
				System.out.println("Run file successfully copied.");
				System.exit(0);
			} catch (IOException e) {
				System.out.println("Error copying run file.");
				e.printStackTrace();
			}
		}
		else {
			System.out.println("Run file already exists! No need to copy.");
		}
	}
	
	public String getPropValues() {
		
		try {
			FileReader reader = new FileReader("." + File.separator + "config.properties");
			Properties properties = new Properties();
			properties.load(reader);
			
			String commandPrefix = properties.getProperty("commandprefix");
			String notificationsRoleName = properties.getProperty("notificationrolename");
			String commandReceivingChannelID = properties.getProperty("commandreceivingchannelID");
			String compatibility = properties.getProperty("compatibility");
			String botToken = properties.getProperty("botToken");
			result = commandPrefix + "," + notificationsRoleName + "," + commandReceivingChannelID + "," + compatibility + "," + botToken;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public void setID(String ID) {
 
		try {
			FileReader reader = new FileReader("." + File.separator + "config.properties");
			Properties properties = new Properties();
			properties.load(reader);
			properties.setProperty("commandreceivingchannelID", ID);
			properties.store(new FileOutputStream("." + File.separator + "config.properties"), null);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}

