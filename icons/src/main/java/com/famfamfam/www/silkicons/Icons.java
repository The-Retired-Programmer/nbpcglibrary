/*
 * Copyright 2014-2017 Richard Linsdale.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
import uk.theretiredprogrammer.nbpcglibrary.api.LogicException;

/**
 * Standard Icon Management.
 *
 * @author Richard Linsdale (richard at theretiredprogrammer.uk)
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
