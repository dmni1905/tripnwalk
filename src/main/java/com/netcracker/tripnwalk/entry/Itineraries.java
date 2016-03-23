package com.netcracker.tripnwalk.entry;

import javax.persistence.*;
import java.sql.Time;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "routs")
public class Itineraries {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @Column(name = "name", unique = true, nullable = false)
    private String name;

    @Column(name = "duration", nullable = false)
    private Time duration;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "route_point",
            joinColumns = @JoinColumn(name = "itineraries_id"),
            inverseJoinColumns = @JoinColumn(name = "point_id"))
    private Set<ItinerariesPoint> points = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "route_photo",
            joinColumns = {@JoinColumn(name = "route_id")},
            inverseJoinColumns = {@JoinColumn(name = "photo_id")})
    private Set<ItinerariesPhoto> photos = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "route_description",
            joinColumns = {@JoinColumn(name = "route_id")},
            inverseJoinColumns = {@JoinColumn(name = "text_id")})
    private Set<ItinerariesDescription> notes = new HashSet<>();

    public Itineraries(String name, Time duration) {
        this.name = name;
        this.duration = duration;
    }

    public Itineraries() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Time getDuration() {
        return duration;
    }

    public Set<ItinerariesPoint> getPoints() {
        return points;
    }

    public void setPoints(Set<ItinerariesPoint> points) {
        this.points = points;
    }

    public void setDuration(Time duration) {
        this.duration = duration;
    }

    public Set<ItinerariesPhoto> getPhotos() {
        return photos;
    }

    public void setPhotos(Set<ItinerariesPhoto> photos) {
        this.photos = photos;
    }

    public Set<ItinerariesDescription> getNotes() {
        return notes;
    }

    public void setNotes(Set<ItinerariesDescription> notes) {
        this.notes = notes;
    }

    public void addPhoto(ItinerariesPhoto photo) {
        if (!getPhotos().contains(photo)) {
            getPhotos().add(photo);
        }
    }

    public void addDescription(ItinerariesDescription text) {
        if (!getNotes().contains(text)) {
            getNotes().add(text);
        }
    }

    public void addPoint(ItinerariesPoint point) {
        if (!getPoints().contains(point)) {
            getPoints().add(point);
        }
    }

    @Override
    public String toString() {
        return "Itineraries{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", duration='" + duration + '\'' +
                '}';
    }
}
