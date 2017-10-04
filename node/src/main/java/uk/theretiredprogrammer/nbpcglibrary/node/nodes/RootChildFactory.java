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

import java.awt.EventQueue;
import uk.theretiredprogrammer.nbpcglibrary.data.entity.CoreEntity;
import org.openide.util.Lookup.Result;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;
import uk.theretiredprogrammer.nbpcglibrary.api.ApplicationLookup;
import uk.theretiredprogrammer.nbpcglibrary.api.InhibitExplorerRefresh;

/**
 * Root ChildFactory support
 *
 * @author Richard Linsdale (richard at theretiredprogrammer.uk)
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
        applkprefreshresult = ApplicationLookup.getDefault().lookupResult(InhibitExplorerRefresh.class);
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
