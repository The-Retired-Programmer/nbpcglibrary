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
package uk.theretiredprogrammer.nbpcglibrary.node.nodes;

import java.awt.Image;
import java.awt.datatransfer.DataFlavor;
import java.io.File;
import java.io.IOException;
import java.util.function.Function;
import javax.imageio.ImageIO;
import uk.theretiredprogrammer.nbpcglibrary.node.ImageFileFinder;
import uk.theretiredprogrammer.nbpcglibrary.api.LogicException;
import org.openide.cookies.InstanceCookie;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.loaders.DataObject;
import org.openide.loaders.DataObjectNotFoundException;
import uk.theretiredprogrammer.nbpcglibrary.api.IdTimestampBaseEntity;
import uk.theretiredprogrammer.nbpcglibrary.api.Rest;
import uk.theretiredprogrammer.nbpcglibrary.data.entity.CoreEntity;
import uk.theretiredprogrammer.nbpcglibrary.data.entity.Entity;

/**
 * Read-Write Icon Node Abstract Class
 *
 * @author Richard Linsdale (richard at theretiredprogrammer.uk)
 * @param <R> the base entity class used in the rest transfer
 * @param <E> the Entity Class
 * @param <P> the parent Entity Class
 * @param <F> the Entity Field enum class
 */
public abstract class IconNode<R extends IdTimestampBaseEntity, E extends Entity, P extends CoreEntity, F> extends TreeNode<R, E, P, F> {

    private final ImageFileFinder<E> imagefilefinder;

    /**
     * Constructor
     *
     * @param nodename the node name
     * @param entitycreator a creator function for the Entity
     * @param e the entity
     * @param cf the childfactory
     * @param restclass class of the rest client for this entity
     * @param allowedDataFlavors allowed paste actions
     * @param iconName the name of the icon (from the Icons set)
     * @param operationsEnabled set for copy , cut and delete enabled
     */
    public IconNode(String nodename, Function<R,E> entitycreator, E e, BasicChildFactory<R, E, P> cf, Class<? extends Rest<R>> restclass, DataFlavor[] allowedDataFlavors, String iconName, int operationsEnabled) {
        super(entitycreator, e, cf, restclass, allowedDataFlavors, iconName, operationsEnabled);
        imagefilefinder = getImageFileFinder(nodename);
    }

    /**
     * Constructor.
     *
     * @param nodename the node name
     * @param entitycreator a creator function for the Entity
     * @param e the entity
     * @param restclass class of the rest client for this entity
     * @param iconName the name of the icon (from the Icons set)
     * @param operationsEnabled set for copy , cut and delete enabled
     */
    protected IconNode(String nodename, Function<R,E> entitycreator, E e, Class<? extends Rest<R>> restclass, String iconName, int operationsEnabled) {
        super(entitycreator, e, restclass, iconName, operationsEnabled);
        imagefilefinder = getImageFileFinder(nodename);
    }

    private ImageFileFinder<E> getImageFileFinder(String nodename) {
        FileObject ff = FileUtil.getConfigFile("nbpcglibrary/node/" + nodename + "/imagefilefinder"); // get folder
        if (ff == null) {
            throw new LogicException("no image file finders defined for node " + nodename);
        }
        FileObject[] ffc = ff.getChildren();
        switch (ffc.length) {
            case 0:
                throw new LogicException("no image file finders defined for node " + nodename);
            case 1:
                return getInstanceFromFileObject(ffc[0]);
            default:
                throw new LogicException("multiple image file finders defined for node " + nodename);
        }
    }

    private ImageFileFinder<E> getInstanceFromFileObject(FileObject fo) {
        try {
            DataObject dobj = DataObject.find(fo);
            InstanceCookie ic = dobj.getLookup().lookup(InstanceCookie.class);
            if (ic == null) {
                throw new LogicException("Bad image file finder - no instance cookie");
            }
            return (ImageFileFinder<E>) ic.instanceCreate();
        } catch (DataObjectNotFoundException ex) {
            throw new LogicException("Bad image file finder", ex);
        } catch (IOException | ClassNotFoundException ex) {
            throw new LogicException("Bad image file finder", ex);
        }
    }

    @Override
    public String getHtmlDisplayName() {
        return null;
    }

    @Override
    public Image getIcon(int type) {
        E entity = getEntity();
        File fi = imagefilefinder.getFile(entity);
        if (fi == null) {
            return entity.getIconWithError();
        }
        try {
            return entity.checkRules(new StringBuilder()) ? ImageIO.read(fi) : entity.addErrorToIcon(ImageIO.read(fi));
        } catch (IOException ex) {
            return entity.getIconWithError();
        }
    }
}
