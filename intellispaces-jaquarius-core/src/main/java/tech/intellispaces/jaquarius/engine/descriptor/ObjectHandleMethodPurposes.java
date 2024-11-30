package tech.intellispaces.jaquarius.engine.descriptor;

public enum ObjectHandleMethodPurposes {

  TraverseMethod,

  GuideMethod;

  public boolean is(String name) {
    return this.name().equals(name);
  }
}
