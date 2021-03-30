package model;

import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.riot.RDFFormat;
import org.apache.jena.vocabulary.DC_11;
import org.apache.jena.vocabulary.VCARD;
import twitter4j.Status;
import twitter4j.User;

/**
 *
 * @author Mario
 */
public class Graph {

    private Model model;
    private HashMap<String, String> prefix = new HashMap<>();
    private HashMap<String, Property> property = new HashMap<>();

    public Graph() {
        model = ModelFactory.createDefaultModel();

        // Prefixes
        prefix.put("si2", "http://www.si2.com/si2#");
        prefix.put("tweet", "http://www.si2.com/tweet#");
        prefix.put("user", "http://www.si2.com/user#");
        prefix.put("lang", "http://www.si2.com/lang#");
        prefix.put("theme", "http://www.si2.com/theme#");
        prefix.put("dc11", "http://purl.org/dc/elements/1.1/");
        prefix.put("skos", "http://www.w3.org/2004/02/skos/core#");
        prefix.put("vcard", "http://www.w3.org/2001/vcard-rdf/3.0#");

        for (String key : prefix.keySet()) {
            model.setNsPrefix(key, prefix.get(key));
        }

        // Custom Properties
        property.put("prefLabel", model.createProperty(prefix.get("skos") + "prefLabel"));
        property.put("text", model.createProperty(prefix.get("si2") + "text"));
        property.put("replyTo", model.createProperty(prefix.get("si2") + "replyTo"));
    }

    public void add(Status tweet) {
        add(tweet, null);
    }

    public void add(Status tweet, Status response) {
        Resource tweetRes = model.createResource(
                prefix.get("tweet") + tweet.getId()
        );

        
        // Text
        tweetRes.addProperty(property.get("text"), tweet.getText());

        
        // ID
        tweetRes.addProperty(DC_11.identifier, tweet.getId() + "");

        
        // Date
        Date date = tweet.getCreatedAt();
        SimpleDateFormat pattern = new SimpleDateFormat("dd/MM/yyyy");
        tweetRes.addProperty(DC_11.date, pattern.format(date));

        
        // Answer to
        if (response != null) {
            Resource answer = model.getResource(
                    prefix.get("tweet") + tweet.getInReplyToStatusId()
            );
            if (!model.contains(answer, null)) {
                add(response);
            }
            tweetRes.addProperty(property.get("replyTo"), answer);
        }

        
        // Author
        User user = tweet.getUser();
        Resource author = model.createResource(
                prefix.get("user") + user.getScreenName()
        );
        author.addProperty(VCARD.FN, user.getName());
        author.addProperty(VCARD.Locality, user.getLocation());
        tweetRes.addProperty(DC_11.creator, author);

        
        // Language
        Resource lang = model.getResource(
                prefix.get("lang") + tweet.getLang()
        );
        if (!model.contains(lang, null)) {
            lang = model.createResource(
                    prefix.get("lang") + tweet.getLang()
            );
            lang.addProperty(DC_11.identifier, tweet.getLang());
            lang.addProperty(property.get("prefLabel"), "");
        }
        tweetRes.addProperty(DC_11.language, lang);

        
        // Theme
    }

    public void serialize(OutputStream os) {
        RDFDataMgr.write(os, model, RDFFormat.TURTLE_PRETTY);
    }
    
    public void serialize(OutputStream os, String format) {
        RDFDataMgr.write(
                os, 
                model, 
                format.equals("ttl") ? RDFFormat.TURTLE_PRETTY : RDFFormat.RDFXML
        );
    }
}
