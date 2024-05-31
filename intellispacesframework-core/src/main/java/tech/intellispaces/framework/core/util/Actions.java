package tech.intellispaces.framework.core.util;

import tech.intellispaces.framework.commons.action.Action;
import tech.intellispaces.framework.commons.action.ActionBuilders;

public interface Actions {

  static Action addSeparatorAction(StringBuilder sb, String separator) {
    return ActionBuilders.action(sb::append, separator).notFirstTimeOnly();
  }
}
