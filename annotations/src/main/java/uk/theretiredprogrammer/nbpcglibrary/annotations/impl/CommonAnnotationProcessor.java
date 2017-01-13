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
import javax.lang.model.element.TypeElement;
import org.openide.filesystems.annotations.LayerGeneratingProcessor;
import org.openide.filesystems.annotations.LayerGenerationException;
import org.openide.util.lookup.ServiceProvider;
import uk.theretiredprogrammer.nbpcglibrary.annotations.RegisterLog;

/**
 * The Common Annotation processor for NBPCG annotations.
 *
 * @author Richard Linsdale (richard at theretiredprogrammer.uk)
 */
@ServiceProvider(service = Processor.class)
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class CommonAnnotationProcessor extends LayerGeneratingProcessor {

    @Override
    public final Set<String> getSupportedAnnotationTypes() {
        return new HashSet<>(Arrays.asList(RegisterLog.class.getCanonicalName()));
    }

    @Override
    protected boolean handleProcess(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) throws LayerGenerationException {
        if (roundEnv.processingOver()) {
            return false;
        }
        roundEnv.getElementsAnnotatedWith(RegisterLog.class).stream().forEach((e) -> {
            RegisterLog r = e.getAnnotation(RegisterLog.class);
            if (r != null) {
                layer(e).file("nbpcglibrary/common/LogBuilder/" + r.value().replace(".", "-")).write();
            }
        });
        return true;
    }

}
