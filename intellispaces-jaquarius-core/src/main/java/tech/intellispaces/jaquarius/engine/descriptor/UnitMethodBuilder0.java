package tech.intellispaces.jaquarius.engine.descriptor;

import tech.intellispaces.action.functional.FunctionActions;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class UnitMethodBuilder0<U> extends BaseUnitMethodBuilder<UnitMethodBuilder0<U>> {
  private final U unit;

  public UnitMethodBuilder0(U unit, String name) {
    super(name, List.of());
    this.unit = unit;
  }

  public <R> UnitMethodBuilder0<U> function(Function<U, R> function) {
    this.action = FunctionActions.ofFunction(function)
        .convertToAction0(unit);
    return this;
  }

  public <R> UnitMethodBuilder0<U> consumer(Consumer<U> consumer) {
    this.action = FunctionActions.ofConsumer(consumer)
        .convertToAction0(unit);
    return this;
  }
}
