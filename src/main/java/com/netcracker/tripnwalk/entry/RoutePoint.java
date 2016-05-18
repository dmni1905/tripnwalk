package com.netcracker.tripnwalk.entry;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "point")
public class RoutePoint implements Cloneable{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id",unique = true)
    private Long id;

    @NotNull
    @Column(name = "position", nullable = false)
    private Integer position;

    @NotNull
    @Column(name = "lat", nullable = false)
    private float lat;

    @NotNull
    @Column(name = "lng", nullable = false)
    private float lng;

    @Column(name = "name")
    private String name;

    public RoutePoint(){}

    public RoutePoint(Integer position, float lat, float lng) {
        this.position = position;
        this.lat = lat;
        this.lng = lng;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public RoutePoint clone() throws CloneNotSupportedException {
        return (RoutePoint)super.clone();
    }
}