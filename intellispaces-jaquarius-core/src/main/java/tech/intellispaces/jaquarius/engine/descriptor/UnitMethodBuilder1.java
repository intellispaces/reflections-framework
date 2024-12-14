package tech.intellispaces.jaquarius.engine.descriptor;

import tech.intellispaces.action.functional.FunctionActions;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

public class UnitMethodBuilder1<U, P> extends BaseUnitMethodBuilder<UnitMethodBuilder1<U, P>> {
  private final U unit;

  public UnitMethodBuilder1(U unit, String name, Class<P> paramClass) {
    super(name, List.of(paramClass));
    this.unit = unit;
  }

  public <R> UnitMethodBuilder1<U, P> function(BiFunction<U, P, R> function) {
    this.action = FunctionActions.ofBiFunction(function)
        .convertToAction1(unit);
    return this;
  }

  public <R> UnitMethodBuilder1<U, P> consumer(BiConsumer<U, P> consumer) {
    this.action = FunctionActions.ofBiConsumer(consumer)
        .convertToAction1(unit);
    return this;
  }
}
