package intellispaces.jaquarius.guide;

import intellispaces.common.javastatement.method.MethodStatement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

public final class GuideLogger {
  private static final Logger LOG = LoggerFactory.getLogger(GuideLogger.class);

  private GuideLogger() {}

  public static void logCallGuide(MethodStatement guideMethod) {
    if (LOG.isDebugEnabled()) {
      LOG.debug("Call guide method '{}' in class {}",
          guideMethod.name(), guideMethod.owner().canonicalName());
    }
  }

  public static void logCallGuide(Method guideMethod) {
    if (LOG.isDebugEnabled()) {
      LOG.debug("Call guide method '{}' in class {}",
          guideMethod.getName(), guideMethod.getDeclaringClass().getCanonicalName());
    }
  }
}
