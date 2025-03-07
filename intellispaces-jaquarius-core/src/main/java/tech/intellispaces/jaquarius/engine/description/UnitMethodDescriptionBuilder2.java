package tech.intellispaces.jaquarius.engine.description;

import tech.intellispaces.commons.action.functional.FunctionActions;
import tech.intellispaces.commons.function.TriConsumer;
import tech.intellispaces.commons.function.TriFunction;

import java.util.List;

public class UnitMethodDescriptionBuilder2<U, P1, P2> extends BaseUnitMethodDescriptionBuilder<UnitMethodDescriptionBuilder2<U, P1, P2>> {
  private final U unit;

  public UnitMethodDescriptionBuilder2(U unit, String name, Class<P1> paramClass1, Class<P2> paramClass2) {
    super(name, List.of(paramClass1, paramClass2));
    this.unit = unit;
  }

  public <R> UnitMethodDescriptionBuilder2<U, P1, P2> function(TriFunction<U, P1, P2, R> function) {
    this.action = FunctionActions.ofTriFunction(function)
        .convertToAction2(unit);
    return this;
  }

  public <R> UnitMethodDescriptionBuilder2<U, P1, P2> consumer(TriConsumer<U, P1, P2> consumer) {
    this.action = FunctionActions.ofTriConsumer(consumer)
        .convertToAction2(unit);
    return this;
  }
}
