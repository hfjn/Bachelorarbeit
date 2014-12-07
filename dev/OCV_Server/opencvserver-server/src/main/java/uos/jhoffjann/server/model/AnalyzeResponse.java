package uos.jhoffjann.server.model;

import java.util.Date;

/**
 * Created by jhoffjann on 03.11.14.
 * Wrapper for JSON-Response from Server to Client
 */
public class AnalyzeResponse {

    private String message;
    private Date createdOn;
    private String name;

    public AnalyzeResponse() {
    }

    public AnalyzeResponse(String name, String message, Date createdOn) {
        this.message = message;
        this.createdOn = createdOn;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }
}
