package intellispaces.jaquarius.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
public @interface Generated {

  String source();

  String library();

  String generator();

  String date();
}
