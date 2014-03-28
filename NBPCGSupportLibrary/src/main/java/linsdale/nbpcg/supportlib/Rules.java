/*
 * Copyright (C) 2014 Richard Linsdale <richard.linsdale at blueyonder.co.uk>.
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
package linsdale.nbpcg.supportlib;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Richard Linsdale <richard.linsdale at blueyonder.co.uk>
 */
public class Rules {

    private List<Rule> rules = new ArrayList<Rule>();

    public final void addRule(Rule r) {
        rules.add(r);
    }

    public final void addFailureMessages(StringBuilder sb) {
        for (Rule r : rules) {
            r.addFailureMessage(sb);
        }
    }
    
    public final boolean checkRules() {
        boolean valid = true;
        for (Rule r : rules) {
            boolean checkresult = r.check();
            valid = valid && checkresult;
        }
        return valid;
    }
    
    public final boolean checkRulesAtLoad() {
        boolean valid = true;
        for (Rule r : rules) {
            if (!(r instanceof UniqueRule)) {
                boolean checkresult = r.check();
                valid = valid && checkresult;
            }
        }
        return valid;
    }
}