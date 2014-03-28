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
package linsdale.nbpcg.annotationimpl;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import linsdale.nbpcg.annotations.RegisterImageFileFinder;
import linsdale.nbpcg.annotations.RegisterLog;
import linsdale.nbpcg.annotations.RegisterNodeAction;
import linsdale.nbpcg.annotations.RegisterNodeSavedAction;
import linsdale.nbpcg.annotations.RegisterNodeSavedActions;
import linsdale.nbpcg.annotations.SaveNodeAction;
import org.openide.filesystems.annotations.LayerBuilder;
import org.openide.filesystems.annotations.LayerGeneratingProcessor;
import org.openide.filesystems.annotations.LayerGenerationException;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author Richard Linsdale <richard.linsdale at blueyonder.co.uk>
 */
@ServiceProvider(service = Processor.class)
@SupportedSourceVersion(SourceVersion.RELEASE_7)
public class AnnotationProcessor extends LayerGeneratingProcessor {

    @Override
    public final Set<String> getSupportedAnnotationTypes() {
        return new HashSet<>(Arrays.asList(
                RegisterNodeSavedAction.class.getCanonicalName(),
                RegisterNodeSavedActions.class.getCanonicalName(),
                SaveNodeAction.class.getCanonicalName(),
                RegisterNodeAction.class.getCanonicalName(),
                RegisterLog.class.getCanonicalName(),
                RegisterImageFileFinder.class.getCanonicalName()));
    }

    @Override
    protected boolean handleProcess(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) throws LayerGenerationException {
        if (roundEnv.processingOver()) {
            return false;
        }
        for (Element e : roundEnv.getElementsAnnotatedWith(RegisterLog.class)) {
            RegisterLog r = e.getAnnotation(RegisterLog.class);
            if (r != null) {
                addLog(e, r);
            }
        }
        for (Element e : roundEnv.getElementsAnnotatedWith(RegisterImageFileFinder.class)) {
            RegisterImageFileFinder r = e.getAnnotation(RegisterImageFileFinder.class);
            if (r != null) {
                addImageFileFinder(e, r);
            }
        }
        for (Element e : roundEnv.getElementsAnnotatedWith(RegisterNodeSavedAction.class)) {
            RegisterNodeSavedAction r = e.getAnnotation(RegisterNodeSavedAction.class);
            if (r != null) {
                addShadow(e, r);
            }
        }
        for (Element e : roundEnv.getElementsAnnotatedWith(RegisterNodeSavedActions.class)) {
            RegisterNodeSavedActions rs = e.getAnnotation(RegisterNodeSavedActions.class);
            if (rs != null) {
                for (RegisterNodeSavedAction r : rs.value()) {
                    addShadow(e, r);
                }
            }
        }
        for (Element e : roundEnv.getElementsAnnotatedWith(RegisterNodeAction.class)) {
            RegisterNodeAction r = e.getAnnotation(RegisterNodeAction.class);
            if (r != null) {
                addActionAndShadow(e, r);
            }
        }
        for (Element e : roundEnv.getElementsAnnotatedWith(SaveNodeAction.class)) {
            SaveNodeAction r = e.getAnnotation(SaveNodeAction.class);
            if (r != null) {
                saveAction(e, r);
            }
        }
        return true;
    }

    private void addLog(Element e, RegisterLog r) throws LayerGenerationException {
        layer(e).file("nbpcg/logs/" + r.value().replace(".", "-")).write();
    }

    private void addImageFileFinder(Element e, RegisterImageFileFinder r) throws LayerGenerationException {
        layer(e).instanceFile("nbpcg/node/" + r.node() + "/imagefilefinder", null)
                .newvalue("instanceCreate", e.toString()).write();
    }

    private void addShadow(Element e, RegisterNodeSavedAction r) throws LayerGenerationException {
        String rid = r.id().replace('.', '-');
        String target = "nbpcg/node/actions/original/" + rid + ".instance";
        layer(e).shadowFile(target, "nbpcg/node/" + r.node() + "/actions/all", rid)
                .position(r.position()).write();
        if (r.isDefaultAction()) {
            layer(e).shadowFile(target, "nbpcg/node/" + r.node() + "/actions/default", rid)
                    .position(r.position()).write();
        }
        if (r.separator() != Integer.MAX_VALUE) {
            layer(e).instanceFile("nbpcg/node/" + r.node() + "/actions/all", rid + "-separator")
                    .position(r.separator())
                    .newvalue("instanceCreate", "javax.swing.JSeparator")
                    .write();
        }
    }

    private void addActionAndShadow(Element e, RegisterNodeAction r) throws LayerGenerationException {
        LayerBuilder.File f = layer(e).instanceFile("nbpcg/node/" + r.node() + "/actions/original", null);
        f.write();
        String target = f.getPath();
        layer(e).shadowFile(target, "nbpcg/node/" + r.node() + "/actions/all", null)
                .position(r.position()).write();
        if (r.isDefaultAction()) {
            layer(e).shadowFile(target, "nbpcg/node/" + r.node() + "/actions/default", null)
                    .position(r.position()).write();
        }
        if (r.separator() != Integer.MAX_VALUE) {
            layer(e).instanceFile("nbpcg/node/" + r.node() + "/actions/all", e.toString() + "-separator")
                    .newvalue("instanceCreate", "javax.swing.JSeparator").position(r.separator()).write();
        }
    }

    private void saveAction(Element e, SaveNodeAction r) throws LayerGenerationException {
        layer(e).instanceFile("nbpcg/node/actions/original", null).write();
    }
}
