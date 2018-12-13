package modele;

import java.util.HashSet;
import java.util.Objects;

public class Noeud {

    private long id;
    private double latitude;
    private double longitude;
    private HashSet<Troncon> tronconsAdjacents;

    public Noeud(long id, double latitude, double longitude){
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.tronconsAdjacents = new HashSet<>();
    }

    public long getId() {
        return id;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public boolean addTronconAdjacent(Troncon troncon){
        return this.tronconsAdjacents.add(troncon);
    }

    public HashSet<Troncon> getTronconsAdjacents(){
        return this.tronconsAdjacents;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Noeud noeud = (Noeud) o;
        return id == noeud.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
