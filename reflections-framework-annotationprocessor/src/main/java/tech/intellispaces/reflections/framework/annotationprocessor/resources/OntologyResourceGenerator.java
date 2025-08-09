package tech.intellispaces.reflections.framework.annotationprocessor.resources;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import javax.tools.FileObject;
import javax.tools.StandardLocation;

import org.jetbrains.annotations.Nullable;

import tech.intellispaces.annotationprocessor.Artifact;
import tech.intellispaces.annotationprocessor.ArtifactGenerator;
import tech.intellispaces.annotationprocessor.ArtifactGeneratorContext;
import tech.intellispaces.annotationprocessor.ArtifactImpl;
import tech.intellispaces.annotationprocessor.ArtifactKinds;
import tech.intellispaces.commons.exception.UnexpectedExceptions;

public class OntologyResourceGenerator implements ArtifactGenerator {

  @Override
  public boolean isRelevant(ArtifactGeneratorContext context) {
    return context.isOverRound();
  }

  @Override
  public String generatedArtifactName() {
    return "generated-resources/reflections/???.ontology.yaml";
  }

  @Override
  public Optional<Artifact> generate(ArtifactGeneratorContext context) {
    Optional<FileObject> fileObject = context.getFile(StandardLocation.SOURCE_OUTPUT, "tmp");
    if (fileObject.isEmpty()) {
      return Optional.empty();
    }

    @Nullable Path generatedSourcesPath = tryToTraverseToGeneratedSources(Paths.get(fileObject.get().toUri()));
    if (generatedSourcesPath == null) {
      return Optional.empty();
    }

    Path generatedResourcesPath = generatedSourcesPath.getParent()
        .resolve("generated-resources")
        .resolve("reflections");
    if (!Files.exists(generatedResourcesPath) || !Files.isDirectory(generatedResourcesPath)) {
      return Optional.empty();
    }

    try (DirectoryStream<Path> paths = Files.newDirectoryStream(generatedResourcesPath)) {
      for (Path path : paths) {
        if (Files.isRegularFile(path) && path.getFileName().toString().endsWith(".ontology.yaml")) {
          // Copy original ontology specification file to META-INF folder
          return Optional.of(new ArtifactImpl(
              ArtifactKinds.ResourceFile,
              "META-INF/reflections/" + path.getFileName(),
              Files.readString(path, StandardCharsets.UTF_8).toCharArray()
          ));
        }
      }
    } catch (IOException e) {
      throw UnexpectedExceptions.withCauseAndMessage(e,
          "Error reading the resource folder generated-resources/reflections");
    }
    return Optional.empty();
  }

  private @Nullable Path tryToTraverseToGeneratedSources(Path path) {
    while (path != null && path.getNameCount() > 0) {
      if ("generated-sources".equals(path.getFileName().toString())) {
        return path;
      }
      path = path.getParent();
    }
    return null;
  }
}
