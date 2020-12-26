package space.sadnov.alenov.com.models;

public class registerDTP {
    public String vidDTP, date, time, location, pavement, lighting, inform;

    public registerDTP() {
    }

    public registerDTP(String vidDTP, String date, String time, String location, String pavement, String lighting, String inform) {
        this.vidDTP = vidDTP;
        this.date = date;
        this.time = time;
        this.location = location;
        this.pavement = pavement;
        this.lighting = lighting;
        this.inform = inform;
    }

    public String getVidDTP() {
        return vidDTP;
    }

    public void setVidDTP(String vidDTP) {
        this.vidDTP = vidDTP;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPavement() {
        return pavement;
    }

    public void setPavement(String pavement) {
        this.pavement = pavement;
    }

    public String getLighting() {
        return lighting;
    }

    public void setLighting(String lighting) {
        this.lighting = lighting;
    }

    public String getInform() {
        return inform;
    }

    public void setInform(String inform) {
        this.inform = inform;
    }
}
