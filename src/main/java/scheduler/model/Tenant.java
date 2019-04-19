package scheduler.model;


import java.util.List;

public class Tenant {
    private String name;
    private int numberOfMarkersGuarantedPerHour;
    private List<MarkerList> markerLists;

    public Tenant(){

    }

    public Tenant(String name, int numberOfMarkersGuarantedPerHour) {
        this.name = name;
        this.numberOfMarkersGuarantedPerHour = numberOfMarkersGuarantedPerHour;
    }

    public Tenant addMarkerList(MarkerList markerList) {
        markerList.setOwner(this);
        markerLists.add(markerList);
        return this;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNumberOfMarkersGuarantedPerHour() {
        return numberOfMarkersGuarantedPerHour;
    }

    public void setNumberOfMarkersGuarantedPerHour(int numberOfMarkersGuarantedPerHour) {
        this.numberOfMarkersGuarantedPerHour = numberOfMarkersGuarantedPerHour;
    }

    public List<MarkerList> getMarkerLists() {
        return markerLists;
    }

    public void setMarkerLists(List<MarkerList> markerLists) {
        this.markerLists = markerLists;
    }
}
