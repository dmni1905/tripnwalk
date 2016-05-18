package com.netcracker.tripnwalk.entry;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "route")
public class Route {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "duration")
    private Integer duration;

    @Transient
    private Integer likes;

    @Transient
    private Boolean likeForCurrentUser;

    @NotNull
    @Valid
    @OneToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "route_point",
            joinColumns = @JoinColumn(name = "route_id"),
            inverseJoinColumns = @JoinColumn(name = "point_id"))
    private Set<RoutePoint> points = new HashSet<>();

    @NotNull
    @Valid
    @OneToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "route_data",
            joinColumns = @JoinColumn(name = "route_id"),
            inverseJoinColumns = @JoinColumn(name = "data_id"))
    private Set<RouteData> data = new HashSet<>();

    public Route(){}

    public Route(String name) {
        this.name = name;
    }

    public Route(String name, Integer duration) {
        this.name = name;
        this.duration = duration;
    }

    public Boolean getLikeForCurrentUser() {
        return likeForCurrentUser;
    }

    public void setLikeForCurrentUser(Boolean likeForCurrentUser) {
        this.likeForCurrentUser = likeForCurrentUser;
    }

    public Integer getLikes() {
        return likes;
    }

    public void setLikes(Integer likes) {
        this.likes = likes;
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

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
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
