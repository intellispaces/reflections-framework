package tech.intellispaces.reflections.framework.system.remote;

import java.util.List;

import tech.intellispaces.core.Rid;

public interface RemoteController {

  List<String> getSpaces();

  boolean checkContainReflection(Rid pid, String domainName);
}
