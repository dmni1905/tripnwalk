package com.netcracker.tripnwalk.entry;

import javax.persistence.*;
import java.sql.Time;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "routes")
public class Route {
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
            joinColumns = @JoinColumn(name = "route_id"),
            inverseJoinColumns = @JoinColumn(name = "point_id"))
    private Set<RoutePoint> points = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "route_data",
            joinColumns = @JoinColumn(name = "route_id"),
            inverseJoinColumns = @JoinColumn(name = "data_id"))
    private Set<RouteData> data = new HashSet<>();

    public Route(String name, Time duration) {
        this.name = name;
        this.duration = duration;
    }

    public Route() {
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

    public void setDuration(Time duration) {
        this.duration = duration;
    }

    public Set<RoutePoint> getPoints() {
        return points;
    }

    public void setPoints(Set<RoutePoint> points) {
        this.points = points;
    }


    public Set<RouteData> getData() {
        return data;
    }

    public void setData(Set<RouteData> data) {
        this.data = data;
    }

    public void addData(RouteData data) {
        if (!getData().contains(data)) {
            getData().add(data);
        }
    }

    public void addPoint(RoutePoint point) {
        if (!getPoints().contains(point)) {
            getPoints().add(point);
        }
    }

    @Override
    public String toString() {
        return "Route{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", duration='" + duration + '\'' +
                '}';
    }
}
