package tech.intellispaces.framework.core.common;

import tech.intellispaces.framework.commons.action.ActionBuilders;
import tech.intellispaces.framework.commons.action.Executor;

public interface Actions {

  static Executor buildCommaAppender(StringBuilder sb) {
    return ActionBuilders.runner(() -> sb.append(", "))
        .wrap(ActionBuilders.notFirstTimeOnlyAction());
  }
}
