package tech.intellispaces.framework.core.common;

import tech.intellispaces.framework.commons.action.Action;
import tech.intellispaces.framework.commons.action.ActionBuilders;

public interface ActionFunctions {

  static Action buildAppendSeparatorAction(StringBuilder sb, String separator) {
    return ActionBuilders.action(sb::append, separator).notFirstTimeOnly();
  }
}
