package tech.intellispaces.jaquarius.engine.descriptor;

import tech.intellispaces.action.Action;
import tech.intellispaces.action.functional.FunctionActions;
import tech.intellispaces.entity.function.TriFunction;
import tech.intellispaces.jaquarius.traverse.TraverseType;

import java.util.List;

public class ObjectHandleMethodBuilder3<H, P1, P2> {
  private final String name;
  private final Class<P1> paramClass1;
  private final Class<P2> paramClass2;

  private Action action;
  private String purpose;
  private int ordinal;
  private Class<?> channelClass;
  private TraverseType traverseType;

  public ObjectHandleMethodBuilder3(
      Class<H> objectHandleClass, String name, Class<P1> paramClass1, Class<P2> paramClass2
  ) {
    this.name = name;
    this.paramClass1 = paramClass1;
    this.paramClass2 = paramClass2;
  }

  public ObjectHandleMethodBuilder3<H, P1, P2> purpose(String purpose) {
    this.purpose = purpose;
    return this;
  }

  public ObjectHandleMethodBuilder3<H, P1, P2> ordinal(int ordinal) {
    this.ordinal = ordinal;
    return this;
  }

  public ObjectHandleMethodBuilder3<H, P1, P2> channelClass(Class<?> channelClass) {
    this.channelClass = channelClass;
    return this;
  }

  public ObjectHandleMethodBuilder3<H, P1, P2> traverseType(TraverseType traverseType) {
    this.traverseType = traverseType;
    return this;
  }

  public <R> ObjectHandleMethodBuilder3<H, P1, P2> function(TriFunction<H, P1, P2, R> function) {
    this.action = FunctionActions.ofTriFunction(function);
    return this;
  }

  public ObjectHandleMethod get() {
    return new ObjectHandleMethodImpl(
        name,
        List.of(paramClass1, paramClass2),
        purpose,
        ordinal,
        action,
        channelClass,
        traverseType
    );
  }
}
