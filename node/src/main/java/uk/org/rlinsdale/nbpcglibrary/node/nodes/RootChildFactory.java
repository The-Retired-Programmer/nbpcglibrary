/*
 * Copyright (C) 2015 Richard Linsdale (richard.linsdale at blueyonder.co.uk).
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
package uk.org.rlinsdale.nbpcglibrary.node.nodes;

import java.awt.EventQueue;
import uk.org.rlinsdale.nbpcglibrary.data.entity.CoreEntity;
import org.openide.util.Lookup;
import org.openide.util.Lookup.Result;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;
import uk.org.rlinsdale.nbpcglibrary.api.ApplicationLookup;
import uk.org.rlinsdale.nbpcglibrary.api.InhibitExplorerRefresh;

/**
 * Root ChildFactory support
 *
 * @author Richard Linsdale (richard.linsdale at blueyonder.co.uk)
 * @param <E> the Parent CoreEntity Class
 */
public abstract class RootChildFactory<E extends CoreEntity> extends CoreChildFactory<E> implements LookupListener, Runnable {

    private final Result<InhibitExplorerRefresh> applkprefreshresult;

    /**
     * Constructor
     *
     * @param parentEntity the parent entity
     */
    @SuppressWarnings("LeakingThisInConstructor")
    public RootChildFactory(E parentEntity) {
        super(parentEntity);
        Lookup applkp = ApplicationLookup.getDefault();
        applkprefreshresult = applkp.lookupResult(InhibitExplorerRefresh.class);
        applkprefreshresult.addLookupListener(this);
    }

    @Override
    public void resultChanged(LookupEvent e) {
        if (applkprefreshresult.allClasses().isEmpty()) {
            EventQueue.invokeLater(this);
        }
    }

    @Override
    public void run() {
        refresh(true);
    }
}
