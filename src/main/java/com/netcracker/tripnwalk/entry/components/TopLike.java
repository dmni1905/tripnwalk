package com.netcracker.tripnwalk.entry.components;

public class TopLike {
    private Long count;
    private Long idRoute;

    public TopLike(Long count, Long idRoute) {
        this.count = count;
        this.idRoute = idRoute;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public Long getIdRoute() {
        return idRoute;
    }

    public void setIdRoute(Long idRoute) {
        this.idRoute = idRoute;
    }
}
