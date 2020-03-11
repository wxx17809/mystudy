/*
 * Copyright 2002-2019 Drew Noakes and contributors
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 *
 * More information about this project is available at:
 *
 *    https://drewnoakes.com/code/exif/
 *    https://github.com/drewnoakes/metadata-extractor
 */

package com.ghkj.gaqcommons.untils;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.imaging.jpeg.JpegMetadataReader;
import com.drew.lang.GeoLocation;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.GpsDirectory;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collection;

/**
 * A sample application that creates HTML that shows image positions on a Google Map according to the GPS data
 * contained within each {@link GpsDirectory}, if available.
 */
public class GeoTagMapBuilder {
    public static void main(String args[]) throws ImageProcessingException, IOException {
        File path = new File("E:\\soft\\tobai\\006_K50E024006_DOM_20191223\\K50E024006_DOM-20191223181231-1.jpg");
        //Collection<PhotoLocation> photoLocations = new ArrayList<PhotoLocation>();
        Metadata metadata = JpegMetadataReader.readMetadata(path);
        Collection<GpsDirectory> gpsDirectories = metadata.getDirectoriesOfType(GpsDirectory.class);
        for (GpsDirectory gpsDirectory : gpsDirectories) {
            GeoLocation geoLocation = gpsDirectory.getGeoLocation();
            System.out.println("qqq==="+geoLocation.toString());
            if (geoLocation != null && !geoLocation.isZero()) {
                //photoLocations.add(new PhotoLocation(geoLocation, path));
                //break;
                System.out.println("==="+geoLocation.getLongitude());
                System.out.println("==="+geoLocation.getLatitude());
            }
        }


        // Write output to the console.
        // You can pipe this to a file if you like, or alternatively modify the output stream here
        // to be a file or network stream.
        PrintStream ps = new PrintStream(System.out);

        //writeHtml(ps, photoLocations);

        // Make sure we flush the stream before exiting.  If you use a different type of stream, you
        // may need to close it here instead.
        ps.flush();
    }

    /**
     * Simple tuple type, which pairs an image file with its {@link GeoLocation}.
     */
    public static class PhotoLocation {
        public final GeoLocation location;
        public final File file;

        public PhotoLocation(final GeoLocation location, final File file) {
            this.location = location;
            this.file = file;
        }
    }

    private static void writeHtml(PrintStream ps, Iterable<PhotoLocation> photoLocations) {
        ps.println("<!DOCTYPE html>");
        ps.println("<html>");
        ps.println("<head>");
        ps.println("<meta name=\"viewport\" content=\"initial-scale=1.0, user-scalable=no\" />");
        ps.println("<meta http-equiv=\"content-type\" content=\"text/html; charset=UTF-8\"/>");
        ps.println("<style>html,body{height:100%;margin:0;padding:0;}#map_canvas{height:100%;}</style>");
        ps.println("<script type=\"text/javascript\" src=\"https://maps.googleapis.com/maps/api/js?sensor=false\"></script>");
        ps.println("<script type=\"text/javascript\">");
        ps.println("function initialise() {");
        ps.println("    var options = { zoom:2, mapTypeId:google.maps.MapTypeId.ROADMAP, center:new google.maps.LatLng(0.0, 0.0)};");
        ps.println("    var map = new google.maps.Map(document.getElementById('map_canvas'), options);");
        ps.println("    var marker;");

        for (PhotoLocation photoLocation : photoLocations) {
            final String fullPath = photoLocation.file.getAbsoluteFile().toString().trim().replace("\\", "\\\\");

            ps.println("    marker = new google.maps.Marker({");
            ps.println("        position:new google.maps.LatLng(" + photoLocation.location + "),");
            ps.println("        map:map,");
            ps.println("        title:\"" + fullPath + "\"});");
            ps.println("    google.maps.event.addListener(marker, 'click', function() { document.location = \"" + fullPath + "\"; });");
        }

        ps.println("}");
        ps.println("</script>");
        ps.println("</head>");
        ps.println("<body onload=\"initialise()\">");
        ps.println("<div id=\"map_canvas\"></div>");
        ps.println("</body>");
        ps.println("</html>");
    }
}
