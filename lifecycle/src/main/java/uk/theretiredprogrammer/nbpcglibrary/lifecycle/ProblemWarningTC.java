/*
 * Copyright 2017 richard linsdale.
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
package uk.theretiredprogrammer.nbpcglibrary.lifecycle;

import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JEditorPane;
import javax.swing.JScrollPane;
import org.openide.util.NbBundle;
import org.openide.windows.TopComponent;

/**
 *
 * @author richard linsdale (richard at theretiredprogrammer.uk)
 */
@TopComponent.Description(preferredID = "ProblemWarningTC",
        iconBase = "com/famfamfam/www/silkicons/error.png",
        persistenceType = TopComponent.PERSISTENCE_NEVER)
@TopComponent.Registration(mode = "editor", openAtStartup = true, roles = {"PROBLEMS", "WARNINGS"})
public final class ProblemWarningTC extends TopComponent {

    private JEditorPane editorPane;

    @Override
    public void componentOpened() {
        setName(getTabname());
        setToolTipText("");
        setLayout(new BorderLayout());
        editorPane = new JEditorPane("text/html", assemblecontent());
        editorPane.setEditable(false);
        editorPane.setPreferredSize(new Dimension(250, 145));
        editorPane.setMinimumSize(new Dimension(10, 10));
        //
        JScrollPane editorScrollPane = new JScrollPane(editorPane);
        editorScrollPane.setVerticalScrollBarPolicy(
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        editorScrollPane.setPreferredSize(new Dimension(250, 145));
        editorScrollPane.setMinimumSize(new Dimension(10, 10));
        add(editorScrollPane, BorderLayout.CENTER);
    }

    @Override
    public void componentClosed() {
        remove(editorPane);
        editorPane = null;
    }

    private String getTabname() {
        return LifeCycle.isProblem() ? "Application Startup Problems"
                : LifeCycle.isWarning() ? "Application Startup Warnings" : "????";
    }

    private String assemblecontent() {
        StringBuilder sb = new StringBuilder();
        if (LifeCycle.isProblem() || LifeCycle.isWarning()) {
            sb.append("<html>");
            sb.append(LifeCycle.isProblem()
                    ? NbBundle.getMessage(ProblemWarningTC.class, "problem_heading")
                    : NbBundle.getMessage(ProblemWarningTC.class, "warning_heading"));
            if (LifeCycle.isAuthorisationProblem()) {
                sb.append("<br/><hr>");
                switch (LifeCycle.authenticationResult()) {
                    case 401:
                    case 601:
                        sb.append(NbBundle.getMessage(ProblemWarningTC.class, "problem_authen"));
                        break;
                    case 602:
                        sb.append(NbBundle.getMessage(ProblemWarningTC.class, "problem_auth"));
                        break;
                    case 600:
                        sb.append(NbBundle.getMessage(ProblemWarningTC.class, "problem_authurl"));
                        break;
                    default:
                        sb.append(NbBundle.getMessage(ProblemWarningTC.class, "problem_authconnect"));
                }
            }
            if (!LifeCycle.areSettingsSaved()) {
                sb.append("<br/><hr>");
                sb.append(NbBundle.getMessage(ProblemWarningTC.class, "warning_settings"));
            }
            sb.append("</html>");
        }
        return sb.toString();
    }
}
