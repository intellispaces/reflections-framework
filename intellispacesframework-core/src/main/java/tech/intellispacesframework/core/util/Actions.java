package tech.intellispacesframework.core.util;

import tech.intellispacesframework.commons.action.Action;
import tech.intellispacesframework.commons.action.ActionBuilders;

public interface Actions {

  static Action addSeparatorAction(StringBuilder sb, String separator) {
    return ActionBuilders.action(sb::append, separator).notFirstTimeOnly();
  }
}
