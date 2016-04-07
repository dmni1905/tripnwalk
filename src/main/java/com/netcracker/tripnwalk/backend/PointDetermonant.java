package com.netcracker.tripnwalk.backend;

import com.netcracker.tripnwalk.entry.RoutePoint;

import java.util.List;

/**
 * Created by DesiresDesigner on 3/29/16.
 */
public class PointDetermonant {
    private float allowableDistance;

    PointDetermonant(float PointDetermonant) {
        this.allowableDistance = PointDetermonant;
    }

    public Boolean isPointOnRoute(List<RoutePoint> route, RoutePoint point) {
        float minDistance = Float.MAX_VALUE;
        for (int i = 0; i < route.size() - 1; ++i) {
            float currentDistance = getDistance(route.get(i), route.get(i+1), point);
            if (currentDistance < minDistance)
                minDistance = currentDistance;
        }
        if (minDistance > allowableDistance)
            return false;
        return true;
    }

    private boolean isPointIn(float a, float b, float x) {
        return b > a ? (x > a && x < b) : (x > b && x < a);
    }

    private float getDistance(RoutePoint start, RoutePoint end, RoutePoint point) {
        float a = getLength(start, end),
                b = getLength(start, point),
                c = getLength(point, end);
        if ((!isPointIn(start.getLat(), end.getLat(), point.getLat()))
                && (!isPointIn(start.getLng(), end.getLng(), point.getLng())))
            return Math.min(b, c);

        float s = getAreaBySides(a, b, c);
        float h = 2*s/a;
        return h;

        //RoutePoint closest;
        /*float dx = end.getLat() - start.getLat();
        float dy = end.getLng() - start.getLng();
        if ((dx == 0) && (dy == 0))
        {
            // It's a point not a line segment.
            //closest = start;
            dx = point.getLat() - start.getLat();
            dy = point.getLng() - start.getLng();
            return (float)Math.sqrt(dx * dx + dy * dy);
        }

        // Calculate the t that minimizes the distance.
        float t = ((point.getLat() - start.getLat()) * dx + (point.getLng() - start.getLng()) * dy) /
                (dx * dx + dy * dy);

        // See if this represents one of the segment's
        // end points or a point in the middle.
        if (t < 0)
        {
            //closest = new RoutePoint(start.getLat(), start.getLng());
            dx = point.getLat() - start.getLat();
            dy = point.getLng() - start.getLng();
        }
        else if (t > 1)
        {
            //closest = new RoutePoint(end.getLng(), end.getLng());
            dx = point.getLat() - end.getLat();
            dy = point.getLng() - end.getLng();
        }
        else
        {
            RoutePoint closest = new RoutePoint(start.getLng() + t * dx, start.getLng() + t * dy);
            dx = point.getLat() - closest.getLat();
            dy = point.getLng() - closest.getLng();
        }

        return (float)Math.sqrt(dx * dx + dy * dy);*/
    }

    private float getLength(RoutePoint a, RoutePoint b) {
        return (float)Math.sqrt(Math.pow((a.getLat() - b.getLat()), 2) + Math.pow((a.getLng() - b.getLng()), 2));
    }

    private float getAreaBySides(double a, double b, double c) {
        double p = (a + b + c)/2;
        return (float)Math.sqrt(p*(p - a)*(p - b)*(p - c));
    }

    /*public static void main (String[] args) {

        PointDetermonant pointDetermonant = new PointDetermonant(10);
        RoutePoint a = new RoutePoint(0, 0, 5),
                b = new RoutePoint(1, 1, 0),
                point = new RoutePoint(2, 5, 8);

        double ab = pointDetermonant.getLength(a, b),
                ap = pointDetermonant.getLength(a, point),
                bp = pointDetermonant.getLength(point, b);

        System.out.println(ab);
        System.out.println(ap);
        System.out.println(bp);
        System.out.println();
        System.out.println(pointDetermonant.getAreaBySides(ab, ap, bp));
        System.out.println();
        System.out.println(pointDetermonant.getDistance(a, b, point));
    }*/
}
