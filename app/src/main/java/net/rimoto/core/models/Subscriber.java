
package net.rimoto.core.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Subscriber {

    @Expose
    private int id;

    @Expose
    private List<String> roles;

    @Expose
    private Date created;

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
     * @return The services
     */
    @SuppressWarnings("unused")

    public List<String> getRoles() {
        return roles;
    }

    /**
     *
     * @param roles The roles
     */
    @SuppressWarnings("unused")
    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    /**
     * 
     * @return The created
     */
    @SuppressWarnings("unused")
    public Date getCreated() {
        return created;
    }

    /**
     * 
     * @param created The created
     */
    @SuppressWarnings("unused")
    public void setCreated(Date created) throws ParseException {
        this.created = created;
    }

    /**
     * Check if equals
     * @param o Object to compare
     * @return boolean
     */
    @Override
    public boolean equals(Object o) {
        return (o instanceof Subscriber) && (((Subscriber) o).getId() == this.getId());
    }

    @Override
    public String toString() {
        return "Subscriber{" +
                "id=" + id +
                ", roles=" + roles +
                ", created=" + created +
                '}';
    }
}
