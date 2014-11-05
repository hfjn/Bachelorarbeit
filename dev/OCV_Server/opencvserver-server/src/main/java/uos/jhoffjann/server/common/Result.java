package uos.jhoffjann.server.common;

/**
 * Created by jhoffjann on 05.11.14.
 */
public class Result {

    private String name;

    private int matches;

    public Result(String name, int matches){
        this.name = name;
        this.matches = matches;
    }

    public int getMatches() {
        return matches;
    }

    public String getName() {
        return name;
    }


}

