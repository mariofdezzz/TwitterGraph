package controller;

/**
 *
 * @author Mario
 */
public class Main {
    public static void main(String[] args) {
        TwitterGraph twitterGraph = new TwitterGraph("resources/keys.txt");
        twitterGraph.execute();
    }
}
