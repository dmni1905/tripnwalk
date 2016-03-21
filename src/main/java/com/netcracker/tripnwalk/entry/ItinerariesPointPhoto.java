package com.netcracker.tripnwalk.entry;

import javax.persistence.*;

@Entity
@Table(name="photo")
public class ItinerariesPointPhoto {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @Column(name = "url", nullable = false)
    private String url;

    public ItinerariesPointPhoto(String url) {
        this.url = url;
    }

    public ItinerariesPointPhoto() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
