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
package uk.theretiredprogrammer.nbpcglibrary.icons;

import java.awt.Image;
import org.openide.util.ImageUtilities;

/**
 * Special Icon Management.
 * @author Richard Linsdale (richard at theretiredprogrammer.uk)
 */
public class SpecialIcons {

    /**
     * Get a Icon Image. Selected from icons set.
     * @param name the icon name
     * @return the image
     */
    public static Image get(String name) {
        return ImageUtilities.loadImage("uk/theretiredprogrammer/nbpcglibrary/icons/" + name + ".png");
    }
}
