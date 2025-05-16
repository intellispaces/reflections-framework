package tech.intellispaces.reflections.framework.system;

import java.util.List;

import tech.intellispaces.actions.functional.FunctionActions;
import tech.intellispaces.commons.function.Consumer3;
import tech.intellispaces.commons.function.Function3;

public class UnitMethodBuilder2<U, P1, P2> extends BaseUnitMethodBuilder<UnitMethodBuilder2<U, P1, P2>> {
  private final U unit;

  public UnitMethodBuilder2(Class<U> unitClass, U unit, String name, Class<P1> paramClass1, Class<P2> paramClass2) {
    super(name, List.of(paramClass1, paramClass2));
    this.unit = unit;
  }

  public <R> UnitMethodBuilder2<U, P1, P2> function(Function3<U, P1, P2, R> function) {
    this.action = FunctionActions.ofFunction3(function)
        .convertToAction2(unit);
    return this;
  }

  public <R> UnitMethodBuilder2<U, P1, P2> consumer(Consumer3<U, P1, P2> consumer) {
    this.action = FunctionActions.ofConsumer3(consumer)
        .convertToAction2(unit);
    return this;
  }
}
