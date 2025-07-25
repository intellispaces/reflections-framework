package tech.intellispaces.reflections.framework.engine.impl;

import java.util.Map;

import com.google.auto.service.AutoService;

import tech.intellispaces.core.OntologyRepository;
import tech.intellispaces.reflections.framework.engine.Engine;
import tech.intellispaces.reflections.framework.engine.EngineFactory;
import tech.intellispaces.reflections.framework.system.AutoGuideRegistry;
import tech.intellispaces.reflections.framework.system.ClassPathOntologyRepository;
import tech.intellispaces.reflections.framework.system.FactoryRegistry;
import tech.intellispaces.reflections.framework.system.GuideRegistry;
import tech.intellispaces.reflections.framework.system.LocalFactoryRegistry;
import tech.intellispaces.reflections.framework.system.LocalGuideRegistry;
import tech.intellispaces.reflections.framework.system.LocalProjectionRegistry;
import tech.intellispaces.reflections.framework.system.LocalTraverseExecutor;
import tech.intellispaces.reflections.framework.system.ProjectionRegistry;

@AutoService(EngineFactory.class)
public class DefaultEngineFactory implements EngineFactory {

    @Override
    public Engine create(String[] args, Map<String, Object> engineAttributes) {
        OntologyRepository ontologyRepository = getOntologyRepository(engineAttributes);

        FactoryRegistry factoryRegistry = new LocalFactoryRegistry();
        ProjectionRegistry projectionRegistry = new LocalProjectionRegistry();

        GuideRegistry guideRegistry = new LocalGuideRegistry();
        AutoGuideRegistry autoGuideRegistry = new AutoGuideRegistry();

        var traverseAnalyzer = new TraverseAnalyzerImpl(ontologyRepository, guideRegistry);
        var traverseExecutor = new LocalTraverseExecutor(traverseAnalyzer);
        var engine = new DefaultEngine(
            ontologyRepository,
            projectionRegistry,
            guideRegistry,
            autoGuideRegistry,
            traverseAnalyzer,
            traverseExecutor,
            factoryRegistry
        );
        traverseAnalyzer.setEngine(engine);
        return engine;
    }

    private OntologyRepository getOntologyRepository(Map<String, Object> engineAttributes) {
        OntologyRepository repository = (OntologyRepository) engineAttributes.get("space.repository");
        if (repository == null) {
            repository = new ClassPathOntologyRepository("default", "");
        }
        return repository;
    }
}
