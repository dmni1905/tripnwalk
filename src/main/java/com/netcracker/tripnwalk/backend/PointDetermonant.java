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
        if ((!isPointIn(start.getX(), end.getX(), point.getX()))
                && (!isPointIn(start.getY(), end.getY(), point.getY())))
            return Math.min(b, c);

        float s = getAreaBySides(a, b, c);
        float h = 2*s/a;
        return h;

        //RoutePoint closest;
        /*float dx = end.getX() - start.getX();
        float dy = end.getY() - start.getY();
        if ((dx == 0) && (dy == 0))
        {
            // It's a point not a line segment.
            //closest = start;
            dx = point.getX() - start.getX();
            dy = point.getY() - start.getY();
            return (float)Math.sqrt(dx * dx + dy * dy);
        }

        // Calculate the t that minimizes the distance.
        float t = ((point.getX() - start.getX()) * dx + (point.getY() - start.getY()) * dy) /
                (dx * dx + dy * dy);

        // See if this represents one of the segment's
        // end points or a point in the middle.
        if (t < 0)
        {
            //closest = new RoutePoint(start.getX(), start.getY());
            dx = point.getX() - start.getX();
            dy = point.getY() - start.getY();
        }
        else if (t > 1)
        {
            //closest = new RoutePoint(end.getY(), end.getY());
            dx = point.getX() - end.getX();
            dy = point.getY() - end.getY();
        }
        else
        {
            RoutePoint closest = new RoutePoint(start.getY() + t * dx, start.getY() + t * dy);
            dx = point.getX() - closest.getX();
            dy = point.getY() - closest.getY();
        }

        return (float)Math.sqrt(dx * dx + dy * dy);*/
    }

    private float getLength(RoutePoint a, RoutePoint b) {
        return (float)Math.sqrt(Math.pow((a.getX() - b.getX()), 2) + Math.pow((a.getY() - b.getY()), 2));
    }

    private float getAreaBySides(double a, double b, double c) {
        double p = (a + b + c)/2;
        return (float)Math.sqrt(p*(p - a)*(p - b)*(p - c));
    }

    public static void main (String[] args) {

        PointDetermonant pointDetermonant = new PointDetermonant(10);
        RoutePoint a = new RoutePoint(0, 5),
                b = new RoutePoint(1, 0),
                point = new RoutePoint(5, 8);

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
    }
}
