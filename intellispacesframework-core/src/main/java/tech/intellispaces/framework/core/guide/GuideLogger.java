package tech.intellispaces.framework.core.guide;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

public final class GuideLogger {
  private static final Logger LOG = LoggerFactory.getLogger(GuideLogger.class);

  public static void logCallGuide(Method guideMethod) {
    if (LOG.isDebugEnabled()) {
      LOG.debug("Call guide method '{}' in class {}",
          guideMethod.getName(), guideMethod.getDeclaringClass().getCanonicalName());
    }
  }
  private GuideLogger() {}
}
