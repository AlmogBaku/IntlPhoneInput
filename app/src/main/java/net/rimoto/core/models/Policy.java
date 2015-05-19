
package net.rimoto.core.models;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import net.rimoto.core.utils.ISO8601;

public class Policy {

    @Expose
    private int id;

    @SerializedName("max_bandwidth")
    @Expose
    private int maxBandwidth;

    @SerializedName("expires_in")
    @Expose
    private Date expiresIn;

    @SerializedName("start_time")
    @Expose
    private Date startTime;

    @Expose
    private List<Service> services = new ArrayList<>();

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
    public Date getExpiresIn() {

        return expiresIn;
    }

    /**
     *
     * @param expiresIn
     *     The expires_in
     */
    public void setExpiresIn(String expiresIn) throws ParseException {
        this.expiresIn = ISO8601.toCalendar(expiresIn).getTime();
    }
    /**
     *
     * @param expiresIn
     *     The expires_in
     */
    public void setExpiresIn(int expiresIn){
        if(expiresIn==-1) {
            this.expiresIn = null;
        }
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
    public void setStartTime(String startTime) throws ParseException {
        this.startTime = ISO8601.toCalendar(startTime).getTime();
    }

    /**
     *
     * @return
     *     The services
     */
    public List<Service> getServices() {
        return services;
    }

    /**
     *
     * @param services
     *     The services
     */
    public void setServices(List<Service> services) {
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
    public void setCreated(String created) throws ParseException {
        this.created = ISO8601.toCalendar(created).getTime();
    }

}
