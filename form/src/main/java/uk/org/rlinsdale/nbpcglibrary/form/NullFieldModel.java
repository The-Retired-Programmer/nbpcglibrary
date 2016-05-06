/*
 * Copyright (C) 2016 Richard Linsdale.
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
package uk.org.rlinsdale.nbpcglibrary.form;

import java.util.ArrayList;
import java.util.List;

/**
 * The Null Field Model
 *
 * @author Richard Linsdale (richard.linsdale at blueyonder.co.uk)
 */
public class NullFieldModel extends FieldModel<String> {

    @Override
    public String get() {
        return "";
    }

    @Override
    public void set(String value) {
    }

    @Override
    public boolean test(StringBuilder sb) {
        return true;
    }

    @Override
    public boolean isNullSelectionAllowed() {
        return false;
    }

    @Override
    public List<String> getChoices() {
        return new ArrayList<>();
    }
}
