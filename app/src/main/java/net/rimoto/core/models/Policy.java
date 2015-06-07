
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
     *     The maxBandwidth
     */
    public int getMaxBandwidth() {
        return maxBandwidth;
    }

    /**
     *
     * @param maxBandwidth
     *     The max_bandwidth
     */
    public void setMaxBandwidth(int maxBandwidth) {
        this.maxBandwidth = maxBandwidth;
    }

    /**
     *
     * @return
     *     The expiresIn
     */
    public int getExpiresIn() {

        return expiresIn;
    }

    /**
     *
     * @param expiresIn
     *     The expires_in
     */
    public void setExpiresIn(int expiresIn)  {
        this.expiresIn = expiresIn;
    }

    /**
     *
     * @return
     *     The startTime
     */
    public Date getStartTime() {
        return startTime;
    }

    /**
     *
     * @param startTime
     *     The start_time
     */
    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    /**
     *
     * @return
     *     The endTime
     */
    public Date endTime() {
        return endTime;
    }

    /**
     *
     * @param endTime
     *     The endTime
     */
    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    /**
     *
     * @return
     *     The services
     */
    public List<SCEService> getServices() {
        return services;
    }

    /**
     *
     * @param services
     *     The services
     */
    public void setServices(List<SCEService> services) {
        this.services = services;
    }

    /**
     * 
     * @return
     *     The created
     */
    public Date getCreated() {
        return created;
    }

    /**
     * 
     * @param created
     *     The created
     */
    public void setCreated(Date created) throws ParseException {
        this.created = created;
    }

}
