package tech.intellispaces.reflections.framework.system;

import java.util.List;

class UnitTypeImpl implements UnitType {
    private final Class<?> unitClass;
    private final List<UnitMethod> methods;

    UnitTypeImpl(Class<?> unitClass, List<UnitMethod> methods) {
        this.unitClass = unitClass;
        this.methods = methods;
    }

    @Override
    public Class<?> unitClass() {
        return unitClass;
    }

    @Override
    public List<UnitMethod> methods() {
        return methods;
    }
}
