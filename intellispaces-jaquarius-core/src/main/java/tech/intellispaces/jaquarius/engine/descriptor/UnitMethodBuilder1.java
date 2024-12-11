package tech.intellispaces.jaquarius.engine.descriptor;

import tech.intellispaces.action.functional.FunctionActions;

import java.util.function.BiFunction;

public class UnitMethodBuilder1<U, P> extends BaseUnitMethodBuilder<UnitMethodBuilder1<U, P>> {
  private final U unit;

  public UnitMethodBuilder1(U unit, String name, Class<P> paramClass) {
    super(name);
    this.unit = unit;
  }

  public <R> UnitMethodBuilder1<U, P> function(BiFunction<U, P, R> function) {
    this.action = FunctionActions.ofBiFunction(function)
        .convertToAction1(unit);
    return this;
  }
}
