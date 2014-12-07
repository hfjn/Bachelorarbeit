package uos.jhoffjann.server.model;

import java.util.Date;

/**
 * Created by Jannik on 28.11.14.
 */
public class ObjectStorage {
    private String name;
    private String descriptorPath;
    private Date creationDate;
    private String description;

    /**
     * The constructor
     * @param name name of the Object
     * @param descriptorPath absolute Path to it's descriptors
     * @param creationDate Creation Date
     * @param description Wikipedia Description
     */
    public ObjectStorage(String name, String descriptorPath, Date creationDate, String description) {
        this.name = name;
        this.descriptorPath = descriptorPath;
        this.creationDate = creationDate;
        this.description = description;
    }

    /**
     * Get the name
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Set the name
     * @param name the name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * get the absolute Path to the Descriptors
     * @return the path
     */
    public String getDescriptorPath() {
        return descriptorPath;
    }

    /**
     * set the absolute Path
     * @param descriptorPath the Path
     */
    public void setDescriptorPath(String descriptorPath) {
        this.descriptorPath = descriptorPath;
    }

    /**
     * get the creation Date
     * @return the date
     */
    public Date getCreationDate() {
        return creationDate;
    }

    /**
     * get the description
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * set the description
     * @param description the description
     */
    public void setDescription(String description) {
        this.description = description;
    }
}
