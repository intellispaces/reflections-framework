package samples;

import java.io.File;

import tech.intellispaces.reflectionsj.annotation.Module;
import tech.intellispaces.reflectionsj.annotation.Startup;
import tech.intellispaces.reflectionsj.annotationprocessor.Sample;

@Sample
@Module
public class ModuleWithStartupMethodAndFileParameter {

  @Startup
  public void startup(File value) {
  }
}
