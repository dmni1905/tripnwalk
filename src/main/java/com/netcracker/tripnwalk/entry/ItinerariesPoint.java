package com.netcracker.tripnwalk.entry;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="point")
public class ItinerariesPoint {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @Column(name = "x", nullable = false)
    private float x;

    @Column(name = "y", nullable = false)
    private float y;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "point_photo",
            joinColumns = { @JoinColumn(name = "point_id") },
            inverseJoinColumns = { @JoinColumn(name = "photo_id") })
    private Set<ItinerariesPointPhoto> photos = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "point_description",
            joinColumns = { @JoinColumn(name = "point_id") },
            inverseJoinColumns = { @JoinColumn(name = "text_id") })
    private Set<ItinerariesPointDescription> notes = new HashSet<>();

    public ItinerariesPoint(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public ItinerariesPoint(){    }

    public Long getId() {
        return id;
    }

    public Set<ItinerariesPointPhoto> getPhotos() {
        return photos;
    }

    public void setPhotos(Set<ItinerariesPointPhoto> photos) {
        this.photos = photos;
    }

    public Set<ItinerariesPointDescription> getNotes() {
        return notes;
    }

    public void setNotes(Set<ItinerariesPointDescription> notes) {
        this.notes = notes;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public void addPhoto(ItinerariesPointPhoto photo) {
        if (!getPhotos().contains(photo)) {
            getPhotos().add(photo);
        }
    }

    public void addDescription(ItinerariesPointDescription text) {
        if (!getNotes().contains(text)) {
            getNotes().add(text);
        }
    }
}
