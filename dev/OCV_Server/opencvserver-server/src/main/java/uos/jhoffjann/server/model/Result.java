package uos.jhoffjann.server.model;

import java.util.ArrayList;

/**
 * Created by jhoffjann on 05.11.14.
 */
public class Result {

    private String name;

    private ArrayList<Double> matches;

    private String path;

    /**
     * the constructor
     * @param name thename
     * @param matches the matches
     * @param path the path of the result
     */
    public Result(String name, ArrayList<Double> matches, String path){
        this.name = name;
        this.matches = matches;
        this.path = path;
    }

    /**
     * returns the matches
     * @return the matches
     */
    public ArrayList<Double> getMatches() {
        return matches;
    }

    /**
     * gets the name
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * returns the path
     * @return path
     */
    public String getPath() {
        return path;
    }
}

