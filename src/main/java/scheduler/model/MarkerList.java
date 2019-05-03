package scheduler.model;

import java.util.ArrayList;
import java.util.List;

public class MarkerList {
    private String name;
    private List<Marker> markers = new ArrayList<>();
    private Tenant owner;

    public MarkerList() {
    }

    public MarkerList(String name, Tenant owner) {
        this.name = name;
        this.owner = owner;
    }

    public MarkerList addMarker(Marker marker) {
        marker.setBelongsTo(this);
        markers.add(marker);
        return this;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Marker> getMarkers() {
        return markers;
    }

    public void setMarkers(List<Marker> markers) {
        this.markers = markers;
    }

    public Tenant getOwner() {
        return owner;
    }

    public void setOwner(Tenant owner) {
        this.owner = owner;
    }

    @Override
    public String toString() {
        return "MarkerList " + name + "/" + owner.getName() + " (" + markers.size() + " markers)";
    }

    public long size() {
        return markers.size();
    }
}
