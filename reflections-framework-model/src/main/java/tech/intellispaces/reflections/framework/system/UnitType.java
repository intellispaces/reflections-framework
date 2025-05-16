package tech.intellispaces.reflections.framework.system;

import java.util.List;

/**
 * The unit type description.
 */
public interface UnitType {

    Class<?> unitClass();

    List<UnitMethod> methods();
}
