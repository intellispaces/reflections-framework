package tech.intellispacesframework.core.guide;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

public final class GuideLogger {
  private static final Logger LOG = LoggerFactory.getLogger(GuideLogger.class);

  public static void logCallGuide(Method guideMethod) {
    LOG.info("Call guide {}#{}", guideMethod.getDeclaringClass().getCanonicalName(), guideMethod.getName());
  }
  private GuideLogger() {}
}
