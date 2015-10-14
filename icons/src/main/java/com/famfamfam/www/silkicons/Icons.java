/*
 * Copyright (C) 2014 Richard Linsdale (richard.linsdale at blueyonder.co.uk).
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301  USA
 */
package com.famfamfam.www.silkicons;

import java.awt.Image;
import java.io.File;
import java.io.FilenameFilter;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.openide.util.ImageUtilities;
import org.openide.util.Utilities;
import uk.org.rlinsdale.nbpcglibrary.api.LogicException;

/**
 * Standard Icon Management.
 *
 * @author Richard Linsdale (richard.linsdale at blueyonder.co.uk)
 */
public class Icons {

    private static List<String> listall;

    /**
     * Get a Icon Image. Selected from silkicons set.
     *
     * @param name the icon name
     * @return the image
     */
    public static Image get(String name) {
        return ImageUtilities.loadImage("com/famfamfam/www/silkicons/" + name + ".png");
    }

    /**
     * Create a list of all available icon names in this package.
     * 
     * @return the list of icon names
     */
    public static List<String> getAllNames() {
        if (listall == null) {
            listall = extractAllIconNames();
        }
        return listall;
    }

    private static List<String> extractAllIconNames() {
        List<String> names = new ArrayList<>();
        URL iconURL = Icons.class.getResource("/com/famfamfam/www/silkicons/");
        File dir;
        try {
            dir = Utilities.toFile(iconURL.toURI());
        } catch (URISyntaxException ex) {
            throw new LogicException(ex.getMessage());
        }
        if (!dir.isDirectory()) {
            throw new LogicException("failure to get icon directory - should never happen!");
        }
        for (File file : dir.listFiles(new IsPNG())) {
            String name = file.getName();
            names.add(name.substring(0, name.length() - 4));
        }
        Collections.sort(names);
        return names;
    }

    private static class IsPNG implements FilenameFilter {

        @Override
        public boolean accept(File dir, String name) {
            return name.endsWith(".png");
        }
    }
}
