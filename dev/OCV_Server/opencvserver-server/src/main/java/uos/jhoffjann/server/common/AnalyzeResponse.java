package uos.jhoffjann.server.common;

import java.util.Date;

/**
 * Created by jhoffjann on 03.11.14.
 * Wrapper for JSON-Response from Server to Client
 */
public class AnalyzeResponse {

    private String message;
    private Date createdOn;

    public AnalyzeResponse() {
    }

    public AnalyzeResponse(String message, Date createdOn) {
        this.message = message;
        this.createdOn = createdOn;
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
