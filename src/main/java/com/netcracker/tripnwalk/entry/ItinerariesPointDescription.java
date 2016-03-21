package com.netcracker.tripnwalk.entry;

import javax.persistence.*;

@Entity
@Table(name="description")
public class ItinerariesPointDescription {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @Column(name = "text", nullable = false)
    private String text;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public ItinerariesPointDescription(String text) {

        this.text = text;
    }

    public ItinerariesPointDescription() {
    }
}
