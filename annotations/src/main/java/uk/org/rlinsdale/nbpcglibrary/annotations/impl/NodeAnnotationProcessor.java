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
package uk.org.rlinsdale.nbpcglibrary.annotations.impl;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import org.openide.filesystems.annotations.LayerBuilder;
import org.openide.filesystems.annotations.LayerGeneratingProcessor;
import org.openide.filesystems.annotations.LayerGenerationException;
import org.openide.util.lookup.ServiceProvider;
import uk.org.rlinsdale.nbpcglibrary.annotations.RegisterImageFileFinder;
import uk.org.rlinsdale.nbpcglibrary.annotations.RegisterNodeAction;
import uk.org.rlinsdale.nbpcglibrary.annotations.UseCommonNodeAction;
import uk.org.rlinsdale.nbpcglibrary.annotations.UseCommonNodeActions;
import uk.org.rlinsdale.nbpcglibrary.annotations.RegisterCommonNodeAction;

/**
 * The Common Annotation processor for NBPCG annotations.
 *
 * @author Richard Linsdale (richard.linsdale at blueyonder.co.uk)
 */
@ServiceProvider(service = Processor.class)
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class NodeAnnotationProcessor extends LayerGeneratingProcessor {

    @Override
    public final Set<String> getSupportedAnnotationTypes() {
        return new HashSet<>(Arrays.asList(
                UseCommonNodeAction.class.getCanonicalName(),
                UseCommonNodeActions.class.getCanonicalName(),
                RegisterCommonNodeAction.class.getCanonicalName(),
                RegisterNodeAction.class.getCanonicalName(),
                RegisterImageFileFinder.class.getCanonicalName()
        ));
    }

    @Override
    protected boolean handleProcess(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) throws LayerGenerationException {
        if (roundEnv.processingOver()) {
            return false;
        }
        for (Element e : roundEnv.getElementsAnnotatedWith(RegisterImageFileFinder.class)) {
            RegisterImageFileFinder r = e.getAnnotation(RegisterImageFileFinder.class);
            if (r != null) {
                addImageFileFinder(e, r);
            }
        }
        for (Element e : roundEnv.getElementsAnnotatedWith(UseCommonNodeAction.class)) {
            UseCommonNodeAction r = e.getAnnotation(UseCommonNodeAction.class);
            if (r != null) {
                addShadow(e, r);
            }
        }
        for (Element e : roundEnv.getElementsAnnotatedWith(UseCommonNodeActions.class)) {
            UseCommonNodeActions rs = e.getAnnotation(UseCommonNodeActions.class);
            if (rs != null) {
                for (UseCommonNodeAction r : rs.value()) {
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
        for (Element e : roundEnv.getElementsAnnotatedWith(RegisterCommonNodeAction.class)) {
            RegisterCommonNodeAction r = e.getAnnotation(RegisterCommonNodeAction.class);
            if (r != null) {
                saveAction(e, r);
            }
        }
        return true;
    }

    private void addImageFileFinder(Element e, RegisterImageFileFinder r) throws LayerGenerationException {
        layer(e).instanceFile("nbpcglibrary/node/" + r.node() + "/imagefilefinder", null)
                .newvalue("instanceCreate", e.toString()).write();
    }

    private void addShadow(Element e, UseCommonNodeAction r) throws LayerGenerationException {
        String rid = r.id().replace('.', '-');
        String target = "nbpcglibrary/node/actions/original/" + rid + ".instance";
        layer(e).shadowFile(target, "nbpcglibrary/node/" + r.node() + "/actions/all", rid)
                .position(r.position()).write();
        if (r.isDefaultAction()) {
            layer(e).shadowFile(target, "nbpcglibrary/node/" + r.node() + "/actions/default", rid)
                    .position(r.position()).write();
        }
        if (r.separator() != Integer.MAX_VALUE) {
            layer(e).instanceFile("nbpcglibrary/node/" + r.node() + "/actions/all", rid + "-separator")
                    .position(r.separator())
                    .newvalue("instanceCreate", "javax.swing.JSeparator")
                    .write();
        }
    }

    private void addActionAndShadow(Element e, RegisterNodeAction r) throws LayerGenerationException {
        LayerBuilder.File f = layer(e).instanceFile("nbpcglibrary/node/" + r.node() + "/actions/original", null);
        f.write();
        String target = f.getPath();
        layer(e).shadowFile(target, "nbpcglibrary/node/" + r.node() + "/actions/all", null)
                .position(r.position()).write();
        if (r.isDefaultAction()) {
            layer(e).shadowFile(target, "nbpcglibrary/node/" + r.node() + "/actions/default", null)
                    .position(r.position()).write();
        }
        if (r.separator() != Integer.MAX_VALUE) {
            layer(e).instanceFile("nbpcglibrary/node/" + r.node() + "/actions/all", e.toString() + "-separator")
                    .newvalue("instanceCreate", "javax.swing.JSeparator").position(r.separator()).write();
        }
    }

    private void saveAction(Element e, RegisterCommonNodeAction r) throws LayerGenerationException {
        layer(e).instanceFile("nbpcglibrary/node/actions/original", null).write();
    }
}
