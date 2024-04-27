package tech.intellispacesframework.core.utils;

import tech.intellispacesframework.commons.exception.UnexpectedViolationException;
import tech.intellispacesframework.dynamicproxy.DynamicProxy;
import tech.intellispacesframework.dynamicproxy.tracker.TrackerFunctions;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

public class ProxyFunctions {

  @SuppressWarnings("unchecked")
  public static <T> T getTracker(Class<T> aClass) {
    TrackerInfo wd = TRACKERS_CACHE.computeIfAbsent(aClass, clz -> new TrackerInfo(DynamicProxy.createTrackerClass(clz), new ThreadLocal<>()));
    ThreadLocal<T> tl = (ThreadLocal<T>) wd.trackers();
    T tracker = tl.get();
    if (tracker == null) {
      tracker = createTracker((Class<T>) wd.trackerClass(), aClass);
      tl.set(tracker);
    }
    resetTracker(tracker);
    return tracker;
  }

  public static void resetTracker(Object tracker) {
    TrackerFunctions.resetTracker(tracker);
  }

  public static List<Method> getTrackedMethods(Object tracker) {
    return TrackerFunctions.getInvokedMethods(tracker);
  }

  private static <T> T createTracker(Class<T> trackerClass, Class<T> originClass) {
    try {
      return trackerClass.getConstructor().newInstance();
    } catch (Exception e) {
      throw UnexpectedViolationException.withCauseAndMessage(e, "Failed to create watcher for class {}", originClass.getCanonicalName());
    }
  }

  private static final Map<Class<?>, TrackerInfo> TRACKERS_CACHE = new WeakHashMap<>();

  private static record TrackerInfo(Class<?> trackerClass, ThreadLocal<?> trackers) {}
}
