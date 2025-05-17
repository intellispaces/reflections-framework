package tech.intellispaces.reflections.framework.engine.impl;

import com.google.auto.service.AutoService;

import tech.intellispaces.reflections.framework.engine.Engine;
import tech.intellispaces.reflections.framework.engine.EngineFactory;
import tech.intellispaces.reflections.framework.system.FactoryRegistry;
import tech.intellispaces.reflections.framework.system.GuideRegistry;
import tech.intellispaces.reflections.framework.system.LocalGuideRegistry;
import tech.intellispaces.reflections.framework.system.LocalProjectionRegistry;
import tech.intellispaces.reflections.framework.system.LocalTraverseExecutor;
import tech.intellispaces.reflections.framework.system.ProjectionRegistry;
import tech.intellispaces.reflections.framework.traverse.plan.TraverseAnalyzer;
import tech.intellispaces.reflections.framework.traverse.plan.TraverseExecutor;

@AutoService(EngineFactory.class)
public class EngineFactoryImpl implements EngineFactory {

    @Override
    public Engine create(String[] args) {
        GuideRegistry guideRegistry = new LocalGuideRegistry();
        FactoryRegistry factoryRegistry = new LocalFactoryRegistry();
        ProjectionRegistry projectionRegistry = new LocalProjectionRegistry();
        TraverseAnalyzer traverseAnalyzer = new TraverseAnalyzerImpl(guideRegistry);
        TraverseExecutor traverseExecutor = new LocalTraverseExecutor(traverseAnalyzer);
        return new EngineImpl(
            traverseAnalyzer,
            traverseExecutor,
            factoryRegistry
        );
    }
}
