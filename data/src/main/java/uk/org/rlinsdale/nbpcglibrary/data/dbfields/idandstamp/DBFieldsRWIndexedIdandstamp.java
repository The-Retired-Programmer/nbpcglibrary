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
package uk.org.rlinsdale.nbpcglibrary.data.dbfields.idandstamp;

import java.io.IOException;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import uk.org.rlinsdale.nbpcglibrary.data.dbfields.DBFieldsRWIndexed;
import uk.org.rlinsdale.nbpcglibrary.data.entity.EntityRWIndexed;
import uk.org.rlinsdale.nbpcglibrary.json.JsonUtil;

/**
 * Handles Read-Write entity field management, for a entity which includes a Id,
 * ordering index field and standardised timestamp (created and updated).
 *
 * @author Richard Linsdale (richard.linsdale at blueyonder.co.uk)
 * @param <E> the entity class
 */
public class DBFieldsRWIndexedIdandstamp<E extends EntityRWIndexed> extends DBFieldsRWIdandstamp<E> implements DBFieldsRWIndexed<E> {

    private int idx = Integer.MAX_VALUE;
    private int idxOriginal;

    @Override
    public void restoreState() {
        super.restoreState();
        idx = idxOriginal;
    }

    @Override
    public void saveState() {
        super.saveState();
        idxOriginal = idx;
    }

    @Override
    public void load(JsonObject data) throws IOException {
        super.load(data);
        idx = JsonUtil.getObjectKeyIntegerValue(data, "idx");
    }

    @Override
    public void setIndex(int idx) {
        this.idx = idx;
    }

    @Override
    public int getIndex() {
        return idx;
    }

    @Override
    public final void diffs(JsonObjectBuilder job) throws IOException {
        if (idx != idxOriginal) {
            job.add("idx", idx);
        }
        super.diffs(job);
    }

    @Override
    public final void values(JsonObjectBuilder job) throws IOException {
        job.add("idx", idx);
        super.values(job);
    }
}
