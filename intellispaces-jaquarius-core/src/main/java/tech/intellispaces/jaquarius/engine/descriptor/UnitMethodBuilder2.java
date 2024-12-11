package tech.intellispaces.jaquarius.engine.descriptor;

import tech.intellispaces.action.functional.FunctionActions;
import tech.intellispaces.general.function.TriFunction;

public class UnitMethodBuilder2<U, P1, P2> extends BaseUnitMethodBuilder<UnitMethodBuilder2<U, P1, P2>> {
  private final U unit;

  public UnitMethodBuilder2(U unit, String name, Class<P1> paramClass1, Class<P2> paramClass2) {
    super(name);
    this.unit = unit;
  }

  public <R> UnitMethodBuilder2<U, P1, P2> function(TriFunction<U, P1, P2, R> function) {
    this.action = FunctionActions.ofTriFunction(function)
        .convertToAction2(unit);
    return this;
  }
}
