
package net.rimoto.core.models;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.List;

public class SCEService {

    @Expose
    private int id;

    @Expose
    private String name;

    @Expose
    private String slug;

    @Expose
    private String description;

    @Expose
    private List<ServiceTag> tags = new ArrayList<>();

    /**
     * 
     * @return
     *     The id
     */
    public int getId() {
        return id;
    }

    /**
     * 
     * @param id
     *     The id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * 
     * @return
     *     The name
     */
    public String getName() {
        return name;
    }

    /**
     * 
     * @param name
     *     The name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 
     * @return
     *     The slug
     */
    public String getSlug() {
        return slug;
    }

    /**
     * 
     * @param slug
     *     The slug
     */
    public void setSlug(String slug) {
        this.slug = slug;
    }

    /**
     *
     * @return
     *     The description
     */
    public String getDescription() {
        return description;
    }

    /**
     *
     * @param description
     *     The description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     *
     * @return
     *     The tags
     */
    public List<ServiceTag> getTags() {
        return tags;
    }

    /**
     *
     * @param tags
     *     The tags
     */
    public void setTags(List<ServiceTag> tags) {
        this.tags = tags;
    }

    @Override
    public boolean equals(Object o) {
        return (o instanceof SCEService) && (((SCEService) o).getId() == this.getId());
    }
}
