
package net.rimoto.core.models;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Policy {

    @Expose
    private int id;

    @Expose
    private String name;

    @SerializedName("max_bandwidth")
    @Expose
    private int maxBandwidth;

    @SerializedName("expires_in")
    @Expose
    private int expiresIn;

    @SerializedName("start_time")
    @Expose
    private Date startTime;

    @SerializedName("end_time")
    @Expose
    private Date endTime;

    @Expose
    private List<SCEService> services = new ArrayList<>();

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
     * @return The maxBandwidth
     */
    @SuppressWarnings("unused")
    public int getMaxBandwidth() {
        return maxBandwidth;
    }

    /**
     *
     * @param maxBandwidth The max_bandwidth
     */
    @SuppressWarnings("unused")
    public void setMaxBandwidth(int maxBandwidth) {
        this.maxBandwidth = maxBandwidth;
    }

    /**
     *
     * @return The expiresIn
     */
    @SuppressWarnings("unused")
    public int getExpiresIn() {

        return expiresIn;
    }

    /**
     *
     * @param expiresIn The expires_in
     */
    @SuppressWarnings("unused")
    public void setExpiresIn(int expiresIn)  {
        this.expiresIn = expiresIn;
    }

    /**
     *
     * @return The startTime
     */
    @SuppressWarnings("unused")
    public Date getStartTime() {
        return startTime;
    }

    /**
     *
     * @param startTime The start_time
     */
    @SuppressWarnings("unused")
    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    /**
     *
     * @return The endTime
     */
    @SuppressWarnings("unused")
    public Date endTime() {
        return endTime;
    }

    /**
     *
     * @param endTime The endTime
     */
    @SuppressWarnings("unused")
    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    /**
     *
     * @return The services
     */
    public List<SCEService> getServices() {
        return services;
    }

    /**
     *
     * @param services The services
     */
    @SuppressWarnings("unused")
    public void setServices(List<SCEService> services) {
        this.services = services;
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
        return (o instanceof Policy) && (((Policy) o).getId() == this.getId());
    }

    /**
     * Get policy name
     * @return String
     */
    public String getName() {
        return name;
    }

    /**
     * Set policy name
     * @param name String
     */
    public void setName(String name) {
        this.name = name;
    }
}
