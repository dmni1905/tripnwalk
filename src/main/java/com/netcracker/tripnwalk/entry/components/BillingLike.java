package com.netcracker.tripnwalk.entry.components;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class BillingLike implements Serializable {
    @Column(name="id_user", nullable=false)
    private Long idUser;
    @Column(name="id_route", nullable=false)
    private Long idRoute;

    public BillingLike() {
    }

    public BillingLike(Long idUser, Long idRoute) {
        this.idUser = idUser;
        this.idRoute = idRoute;
    }

    public Long getIdUser() {
        return idUser;
    }

    public void setIdUser(Long idUser) {
        this.idUser = idUser;
    }

    public Long getIdRoute() {
        return idRoute;
    }

    public void setIdRoute(Long idRoute) {
        this.idRoute = idRoute;
    }
}
