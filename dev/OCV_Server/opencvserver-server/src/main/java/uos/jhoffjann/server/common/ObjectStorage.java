package uos.jhoffjann.server.common;

import java.util.Date;

/**
 * Created by Jannik on 28.11.14.
 */
public class ObjectStorage {
    private String name;
    private String descriptorPath;
    private Date creationDate;
    private String description;

    public ObjectStorage(String name, String descriptorPath, Date creationDate, String description) {
        this.name = name;
        this.descriptorPath = descriptorPath;
        this.creationDate = creationDate;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescriptorPath() {
        return descriptorPath;
    }

    public void setDescriptorPath(String descriptorPath) {
        this.descriptorPath = descriptorPath;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
