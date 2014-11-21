package uos.jhoffjann.server.common;

import java.util.ArrayList;

/**
 * Created by jhoffjann on 05.11.14.
 */
public class Result {

    private String name;

    private ArrayList<Double> matches;

    public Result(String name, ArrayList<Double> matches){
        this.name = name;
        this.matches = matches;
    }

    public ArrayList<Double> getMatches() {
        return matches;
    }

    public String getName() {
        return name;
    }


}

