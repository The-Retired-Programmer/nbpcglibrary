/*
 * Copyright (C) 2014-2015 Richard Linsdale (richard.linsdale at blueyonder.co.uk).
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

import java.util.List;
import uk.org.rlinsdale.nbpcglibrary.api.EventParams;
import uk.org.rlinsdale.nbpcglibrary.common.LogBuilder;
import uk.org.rlinsdale.nbpcglibrary.form.Form.FormSaveResult;

/**
 * The Parameter Class for a Dialog Completion listener.
 *
 * @author Richard Linsdale (richard.linsdale at blueyonder.co.uk)
 */
public class DialogCompletionEventParams implements EventParams {

    private final FormSaveResult result;
    private final List<String> parameters;

    /**
     * Constructor.
     *
     * @param result the form exit state
     * @param parameters an array of possible return values
     */
    public DialogCompletionEventParams(FormSaveResult result, List<String> parameters) {
        this.result = result;
        this.parameters=parameters;
    }

    /**
     * Get the field Id
     *
     * @return the field identifier
     */
    public FormSaveResult get() {
        return result;
    }
    
    /**
     * Get a parameter
     *
     * @param index the required parameter index
     * @return the parameter
     */
    public String getParameter(int index) {
        return parameters.get(index);
    }

    @Override
    public String instanceDescription() {
        return LogBuilder.instanceDescription(this, result.toString());
    }
}
