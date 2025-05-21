package tech.intellispaces.reflections.framework.system;

import java.util.ArrayList;
import java.util.List;

import tech.intellispaces.actions.Action;
import tech.intellispaces.commons.exception.NotImplementedExceptions;
import tech.intellispaces.commons.exception.UnexpectedExceptions;
import tech.intellispaces.commons.stream.Streams;
import tech.intellispaces.jstatements.method.MethodStatement;
import tech.intellispaces.reflections.framework.action.InvokeUnitMethodAction;
import tech.intellispaces.reflections.framework.annotation.Configuration;
import tech.intellispaces.reflections.framework.annotation.Guide;
import tech.intellispaces.reflections.framework.aop.AopFunctions;
import tech.intellispaces.reflections.framework.engine.Engine;
import tech.intellispaces.reflections.framework.system.empty.EmptyModule;
import tech.intellispaces.reflections.framework.system.empty.EmptyModuleWrapper;

public class ModuleFactory {

    /**
     * Creates module.
     *
     * @param unitClasses module unit classes.
     * @param engine the engine.
     * @return created module.
     */
    public static ModuleHandle createModule(List<Class<?>> unitClasses, Engine engine) {
        List<UnitHandle> units = createUnits(unitClasses);
        applyAdvises(units, engine);

        var system = new SystemHandleImpl(engine);
        ModuleHandle module = new ModuleHandleImpl(system, units);
        system.setCurrentModule(module);
        return module;
    }

    static List<UnitHandle> createUnits(List<Class<?>> unitClasses) {
        if (unitClasses == null || unitClasses.isEmpty()) {
            return List.of(createEmptyMainUnit());
        } else if (unitClasses.size() == 1) {
            Class<?> unitclass = unitClasses.get(0);
            if (unitclass.isAnnotationPresent(tech.intellispaces.reflections.framework.annotation.Module.class)) {
                return createModuleUnits(unitclass);
            } else if (unitclass.isAnnotationPresent(Configuration.class) || unitclass.isAnnotationPresent(Guide.class)) {
                return createEmptyMainUnitAndIncludedUnits(List.of(unitclass));
            } else {
                throw UnexpectedExceptions.withMessage("Expected module, configuration or guide class");
            }
        } else {
            return createEmptyMainUnitAndIncludedUnits(unitClasses);
        }
    }

    static List<UnitHandle> createModuleUnits(Class<?> moduleclass) {
        List<UnitHandle> units = new ArrayList<>();
        units.add(UnitFactory.createUnit(moduleclass, true));
        createIncludedUnits(moduleclass, units);
        return units;
    }

    static List<UnitHandle> createEmptyMainUnitAndIncludedUnits(List<Class<?>> unitClasses) {
        List<UnitHandle> units = new ArrayList<>();
        units.add(createEmptyMainUnit());
        unitClasses.stream()
            .map(ModuleFactory::createIncludedUnit)
            .forEach(units::add);
        return units;
    }

    static UnitHandle createEmptyMainUnit() {
        var unitHandle = new UnitHandleImpl();
        unitHandle.setUnitClass(EmptyModule.class);
        unitHandle.setMain(true);
        UnitWrapper unitInstance = UnitFactory.createUnitInstance(EmptyModule.class, EmptyModuleWrapper.class, unitHandle);
        unitHandle.setUnitInstance(unitInstance);
        return unitHandle;
    }

    static void createIncludedUnits(Class<?> moduleClass, List<UnitHandle> units) {
        Iterable<Class<?>> unitClasses = ModuleFunctions.getIncludedUnits(moduleClass);
        Streams.get(unitClasses)
            .map(ModuleFactory::createIncludedUnit)
            .forEach(units::add);
    }

    static UnitHandle createIncludedUnit(Class<?> unitClass) {
        if (unitClass != Void.class) {
            return UnitFactory.createUnit(unitClass, false);
        }
        throw NotImplementedExceptions.withCode("3fHY1g");
    }

    static void applyAdvises(List<UnitHandle> unitHandles, Engine engine) {
        unitHandles.forEach(u -> applyAdvises(u, engine));
    }

    static void applyAdvises(UnitHandle unitHandle, Engine engine) {
        applyStartupActionAdvises(unitHandle, engine);
        applyGuideActionAdvises(unitHandle, engine);
    }

    @SuppressWarnings("unchecked")
    static void applyStartupActionAdvises(UnitHandle unitHandle, Engine engine) {
        if (unitHandle.startupAction().isEmpty()) {
            return;
        }
        var startupAction = (InvokeUnitMethodAction<Void>) unitHandle.startupAction().get();
        MethodStatement startupMethod = startupAction.method();
        Action chainAction = AopFunctions.buildChainAction(startupMethod, startupAction, engine);
        if (chainAction != startupAction) {
            unitHandle.setStartupAction(chainAction);
        }
    }

    static void applyGuideActionAdvises(UnitHandle unitHandle, Engine engine) {
        List<UnitGuide<? ,?>> guides = unitHandle.guides();
        for (UnitGuide<?, ?> guide : guides) {
            MethodStatement method = guide.guideMethod();
            Action originalAction = unitHandle.guideAction(guide.guideOrdinal());
            Action chainAction = AopFunctions.buildChainAction(method, originalAction, engine);
            if (chainAction != originalAction) {
                unitHandle.setGuideAction(guide.guideOrdinal(), chainAction);
            }
        }
    }

    private ModuleFactory() {}
}
