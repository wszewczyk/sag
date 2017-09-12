package edu.sag.services;

import edu.sag.models.Location;

/**
 * Created by wmsze on 15.08.2017.
 */
public class LocationService {
    public static double getDistance(Location l1, Location l2)
    {
        if(l1 == null || l2 == null)
            return -1;
        double x1 = Math.pow((l1.getLat() - l2.getLat()),2);
        double x2 = Math.pow((l1.getLon() - l2.getLon()),2);

        return Math.sqrt(x1 + x2);
    }
}

