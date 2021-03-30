package model;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Mario
 */
public class Keys {
    private String key;
    private String secret;
    private String token;
    private String tokenSecret;

    public Keys(String file) {
        Path path = Paths.get("src", "main", file);
        
        try {
            List<String> lines = Files.readAllLines(path);
            
            if (lines.size() != 4) 
                throw new Exception("Expected 4 lines at 'keys.txt', but got "+ lines.size());
            
            key = lines.get(0);
            secret = lines.get(1);
            token = lines.get(2);
            tokenSecret = lines.get(3);
            
        } catch (IOException ex) {
            Logger.getLogger(Keys.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(Keys.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String getKey() {
        return key;
    }

    public String getSecret() {
        return secret;
    }

    public String getToken() {
        return token;
    }

    public String getTokenSecret() {
        return tokenSecret;
    }
}
