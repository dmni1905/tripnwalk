package com.netcracker.tripnwalk.entry;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "data")
public class RouteData {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @NotNull
    @Column(name = "type", nullable = false)
    private String type;

    @NotNull
    @Column(name = "content", nullable = false)
    private String content;

    @NotNull
    @Column(name = "lat", nullable = false)
    private float lat;

    @NotNull
    @Column(name = "lng", nullable = false)
    private float lng;

    public RouteData(){}

    public RouteData(String type, String content, float lat, float lng) {
        this.type = type;
        this.content = content;
        this.lat = lat;
        this.lng = lng;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public float getLat() {
        return lat;
    }

    public void setLat(float lat) {
        this.lat = lat;
    }

    public float getLng() {
        return lng;
    }

    public void setLng(float lng) {
        this.lng = lng;
    }
}
