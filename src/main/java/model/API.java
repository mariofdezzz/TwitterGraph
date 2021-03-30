package model;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

/**
 *
 * @author Mario
 */
public class API {

    private Twitter twitter;

    public API(
            String key, 
            String secret, 
            String token, 
            String tokenSecret
    ) {
        
        ConfigurationBuilder cb = new ConfigurationBuilder();

        cb.setDebugEnabled(true)
                .setOAuthConsumerKey(key)
                .setOAuthConsumerSecret(secret)
                .setOAuthAccessToken(token)
                .setOAuthAccessTokenSecret(tokenSecret);

        TwitterFactory tf = new TwitterFactory(cb.build());
        twitter = tf.getInstance();
    }

    public List<Status> search(String subject, int maxFeatures) {
        try {
            Query query = new Query(subject);
            query.count(maxFeatures);
            QueryResult result = twitter.search(query);
            
            return result.getTweets();
        } catch (TwitterException te) {
            te.printStackTrace();
            System.out.println("Failed to search tweets: " + te.getMessage());
            System.exit(-1);
        }
        return null;
    }

    public Status getStatus(long inReplyToStatusId) {
        try {
            return twitter.showStatus(inReplyToStatusId);
        } catch (TwitterException ex) {
            Logger.getLogger(API.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
