/*
 * Copyright 2015-2017 Richard Linsdale.
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
package uk.theretiredprogrammer.nbpcglibrary.annotations.impl;

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
import uk.theretiredprogrammer.nbpcglibrary.annotations.RegisterImageFileFinder;
import uk.theretiredprogrammer.nbpcglibrary.annotations.RegisterNodeAction;
import uk.theretiredprogrammer.nbpcglibrary.annotations.UseCommonNodeAction;
import uk.theretiredprogrammer.nbpcglibrary.annotations.UseCommonNodeActions;
import uk.theretiredprogrammer.nbpcglibrary.annotations.RegisterCommonNodeAction;

/**
 * The Common Annotation processor for NBPCG annotations.
 *
 * @author Richard Linsdale (richard at theretiredprogrammer.uk)
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
