
package net.rimoto.core.models;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.List;

public class ServiceTag {

    @Expose
    private int id;

    @Expose
    private String name;

    @Expose
    private String slug;

    private List<SCEService> services = new ArrayList<>();

    /**
     * 
     * @return The id
     */
    public int getId() {
        return id;
    }

    /**
     * 
     * @param id The id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * 
     * @return The name
     */
    public String getName() {
        return name;
    }

    /**
     * 
     * @param name The name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 
     * @return The slug
     */
    @SuppressWarnings("unused")
    public String getSlug() {
        return slug;
    }

    /**
     * 
     * @param slug The slug
     */
    @SuppressWarnings("unused")
    public void setSlug(String slug) {
        this.slug = slug;
    }


    @Override
    public boolean equals(Object o) {
        return (o instanceof ServiceTag) && (((ServiceTag) o).getId() == this.getId());
    }

    /**
     * Add SCEService
     * @param service SCEService
     */
    public void addService(SCEService service) {
        if(!services.contains(service)) {
            services.add(service);
        }
    }

    /**
     *
     * @return SCEServices
     */
    public List<SCEService> getServices() {
        return services;
    }
}
