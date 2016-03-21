package com.netcracker.tripnwalk.entry;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="users")
public class AppUser {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @Column(name = "username", unique = true, nullable = false)
    private String username;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "friends",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "friend_id", referencedColumnName = "id")
    )
    private Set<AppUser> friends = new HashSet<>();

    @ManyToMany(cascade=CascadeType.ALL)
    @JoinTable(name="user_routs",
            joinColumns=@JoinColumn(name="itineraries_id"),
            inverseJoinColumns=@JoinColumn(name="user_id"))
    private Set<Itineraries> itineraries = new HashSet<>();



    public void addFriend(AppUser user){
        if(!getFriends().contains(user)){
            getFriends().add(user);
            user.getFriends().add(this);
        }
    }


    public void addItineraries(Itineraries it){
        if(!getItineraries().contains(it)){
            getItineraries().add(it);
        }
    }

    public Set<AppUser> getFriends() {
        return friends;
    }

    public void setFriends(Set<AppUser> friends) {
        this.friends = friends;
    }



    public AppUser() {
    }

    public AppUser(String username) {
        this.username = username;
    }

    public AppUser(long id, String username) {
        this.id = id;
        this.username = username;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public Set<Itineraries> getItineraries() {
        return itineraries;
    }

    public void setItineraries(Set<Itineraries> itineraries) {
        this.itineraries = itineraries;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                '}';
    }
}
