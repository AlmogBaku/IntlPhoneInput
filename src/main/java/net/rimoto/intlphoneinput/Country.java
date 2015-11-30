package net.rimoto.intlphoneinput;

public class Country{
    public Country(String name, String iso, int dialCode) {
        setName(name);
        setIso(iso);
        setDialCode(dialCode);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIso() {
        return iso;
    }

    public void setIso(String iso) {
        this.iso = iso.toUpperCase();
    }

    public int getDialCode() {
        return dialCode;
    }

    public void setDialCode(int dialCode) {
        this.dialCode = dialCode;
    }

    private String name;
    private String iso;
    private int dialCode;

    /**
     * Check if equals
     * @param o Object to compare
     * @return boolean
     */
    @Override
    public boolean equals(Object o) {
        return (o instanceof Country) && (((Country) o).getIso().toUpperCase().equals(this.getIso().toUpperCase()));
    }
}
