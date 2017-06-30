package sdc.assets.annotations.processors;

import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;

@SupportedSourceVersion(SourceVersion.RELEASE_8)
@SupportedAnnotationTypes("sdc.assets.annotations.Complexity")
public class ComplexityProcessor extends AbstractProcessor {

	public ComplexityProcessor() {
		super();
	}

	boolean run = true;

	@Override
	public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
		if (run) {
			processingEnv.getMessager().printMessage(Diagnostic.Kind.WARNING,
					"This function is run by the java compiler! I could generate code now if I wanted. But I'm too lazy  ¯\\_(ツ)_/¯");
			run = false;
		}
		return true;
	}
}
