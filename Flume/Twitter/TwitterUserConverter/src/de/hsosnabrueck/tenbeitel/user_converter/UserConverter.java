package de.hsosnabrueck.tenbeitel.user_converter;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.LineIterator;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

public class UserConverter {
	private static final String CONSUMER_KEY = "consumerKey";
	private static final String CONSUMER_SECRET = "consumerSecret";
	private static final String ACCESS_TOKEN = "accessToken";
	private static final String ACCESS_TOKEN_SECRET = "accessTokenSecret";

	private Map<String, String> conf = new HashMap<String, String>();
	private Twitter twitter;
	
	String userFileContent;
	
	String fileName;

	public void loadConfiguration() throws IOException {
		ClassLoader loader = getClass().getClassLoader();
		InputStream stream = loader.getResourceAsStream("resources/config.conf");
		LineIterator it = IOUtils.lineIterator(stream, Charset.defaultCharset());
		while (it.hasNext()) {
			String line = it.nextLine();
			String[] content = line.split("=");
			if (content.length == 2) {
				conf.put(content[0].trim(), content[1].trim());
			}
		}

	}

	public void loadConfiguration(String filePath) throws IOException {
		File confFile = new File(filePath);
		LineIterator it = FileUtils.lineIterator(confFile);
		while (it.hasNext()) {
			String line = it.nextLine();
			String[] content = line.split("=");
			if (content.length == 2) {
				conf.put(content[0], content[1]);
			}
		}
	}

	public void initTwitter() {
		if (conf.containsKey(CONSUMER_KEY) && conf.containsKey(ACCESS_TOKEN)
				&& conf.containsKey(ACCESS_TOKEN_SECRET)
				&& conf.containsKey(CONSUMER_SECRET)) {
			ConfigurationBuilder confBuilder = new ConfigurationBuilder();
			confBuilder.setOAuthConsumerKey(conf.get(CONSUMER_KEY));
			confBuilder.setOAuthConsumerSecret(conf.get(CONSUMER_SECRET));
			confBuilder.setOAuthAccessToken(conf.get(ACCESS_TOKEN));
			confBuilder
					.setOAuthAccessTokenSecret(conf.get(ACCESS_TOKEN_SECRET));
			confBuilder.setJSONStoreEnabled(true);
			confBuilder.setIncludeEntitiesEnabled(true);

			Configuration conf = confBuilder.build();

			this.twitter = new TwitterFactory(conf).getInstance();

		}
	}

	public void loadUserFile(String filepath) throws IOException {
		File file = new File(filepath);
		this.userFileContent = FileUtils.readFileToString(file);
		this.fileName = FilenameUtils.removeExtension(filepath);
	}
	
	public void createIdAndRefFile() throws IOException{
		String cleanUserString = this.userFileContent.replaceAll("@", "").replaceAll("\r", "").replaceAll("\n", "");
		String[] userNames = cleanUserString.split(",");
		StringBuilder idList = new StringBuilder();
		StringBuilder refList = new StringBuilder();
		StringBuilder errorList = new StringBuilder();
				
		for(int i = 0; i < userNames.length; i++){
			String userName = userNames[i].trim();
			long twitterId;
			try {
				twitterId = getTwitterIdByScreenName(userName);
				idList.append(twitterId);
				System.out.print(".");
				refList.append(userName);
				refList.append(": ");
				refList.append(twitterId);
				
				if(i < userNames.length){
					idList.append(",");
					refList.append("\r\n");
				}
			} catch (TwitterException e) {
				// TODO Auto-generated catch block
				errorList.append(userName);
				errorList.append("\n");
			}
			
		}
		System.out.println("");
		FileUtils.writeStringToFile(new File(this.fileName + "_idlist.txt"), idList.toString());
		FileUtils.writeStringToFile(new File(this.fileName + "_reflist.txt"), refList.toString());
		if(errorList.length() > 0){
			FileUtils.writeStringToFile(new File("errors.txt"), errorList.toString());
		}
		
		
	}
	
	private long getTwitterIdByScreenName(String screenName)
			throws TwitterException {
		return twitter.showUser(screenName).getId();
	} 

	public static void main(String[] args) {
		if (args.length > 0) {
			UserConverter conv = new UserConverter();
			try {
				if (args.length < 2) {
					System.out.println("Loading standard conf");
					conv.loadConfiguration();
				}
				if (args.length == 2) {
					System.out.println("Loading conf file");
					conv.loadConfiguration(args[1]);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.exit(0);
			}

			conv.initTwitter();
			try {
				System.out.println("Loading usernames from " + args[0]);
				conv.loadUserFile(args[0]);
				System.out.println("Creating ID and Ref File:");
				conv.createIdAndRefFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.exit(0);
			}
			System.out.println("Finished");
			

		}
		else{
			System.out.println("User File needed");
			System.exit(0);
		}
	}

}
