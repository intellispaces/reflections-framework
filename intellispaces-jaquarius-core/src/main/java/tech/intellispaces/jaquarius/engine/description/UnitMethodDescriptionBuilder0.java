package tech.intellispaces.jaquarius.engine.description;

import tech.intellispaces.action.functional.FunctionActions;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class UnitMethodDescriptionBuilder0<U> extends BaseUnitMethodDescriptionBuilder<UnitMethodDescriptionBuilder0<U>> {
  private final U unit;

  public UnitMethodDescriptionBuilder0(U unit, String name) {
    super(name, List.of());
    this.unit = unit;
  }

  public <R> UnitMethodDescriptionBuilder0<U> function(Function<U, R> function) {
    this.action = FunctionActions.ofFunction(function)
        .convertToAction0(unit);
    return this;
  }

  public <R> UnitMethodDescriptionBuilder0<U> consumer(Consumer<U> consumer) {
    this.action = FunctionActions.ofConsumer(consumer)
        .convertToAction0(unit);
    return this;
  }
}
