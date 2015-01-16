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

    /**
     * The default Constructor
     */
    public AnalyzeResponse() {
    }

    /**
     * The Constructor with parameters
     * @param name The name
     * @param message The Message
     * @param createdOn The Date
     */
    public AnalyzeResponse(String name, String message, Date createdOn) {
        this.message = message;
        this.createdOn = createdOn;
        this.name = name;
    }

    /**
     * Returns the Name
     * @return string
     */

    public String getName() {
        return name;
    }

    /**
     * sets a new name
     * @param name string
     */

    public void setName(String name) {
        this.name = name;
    }

    /**
     * returns the message
     * @return string
     */
    public String getMessage() {
        return message;
    }

    /**
     * sets a new message
     * @param message string
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * gets the creation timestamp
     * @return the date
     */
    public Date getCreatedOn() {
        return createdOn;
    }

}
