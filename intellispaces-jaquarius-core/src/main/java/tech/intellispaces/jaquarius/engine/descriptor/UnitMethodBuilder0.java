package tech.intellispaces.jaquarius.engine.descriptor;

import tech.intellispaces.action.functional.FunctionActions;

import java.util.function.Function;

public class UnitMethodBuilder0<U> extends BaseUnitMethodBuilder<UnitMethodBuilder0<U>> {
  private final U unit;

  public UnitMethodBuilder0(U unit, String name) {
    super(name);
    this.unit = unit;
  }

  public <R> UnitMethodBuilder0<U> function(Function<U, R> function) {
    this.action = FunctionActions.ofFunction(function)
        .convertToAction0(unit);
    return this;
  }
}
