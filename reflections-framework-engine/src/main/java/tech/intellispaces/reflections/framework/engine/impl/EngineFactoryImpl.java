package tech.intellispaces.reflections.framework.engine.impl;

import com.google.auto.service.AutoService;

import tech.intellispaces.reflections.framework.engine.Engine;
import tech.intellispaces.reflections.framework.engine.EngineFactory;
import tech.intellispaces.reflections.framework.engine.FactoryRegistry;
import tech.intellispaces.reflections.framework.engine.GuideRegistry;
import tech.intellispaces.reflections.framework.engine.ProjectionRegistry;
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
