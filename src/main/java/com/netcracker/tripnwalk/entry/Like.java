package com.netcracker.tripnwalk.entry;

import com.netcracker.tripnwalk.entry.components.BillingLike;

import javax.persistence.*;

@Entity
@Table(name = "like_route")
public class Like {
    public Like() {
    }

    public Like(BillingLike key) {
        this.key = key;
    }

    public Like(Long idUser, Long idRoute) {
        this.key = new BillingLike(idUser, idRoute);
    }

    @EmbeddedId
    private BillingLike key;

    public BillingLike getKey() {
        return key;
    }

    public void setKey(BillingLike key) {
        this.key = key;
    }


}
