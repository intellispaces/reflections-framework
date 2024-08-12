package tech.intellispaces.core.system.action;

import tech.intellispaces.actions.Action0;

import java.lang.reflect.Method;

public interface ShutdownAction extends Action0<Void> {

  Method method();
}
