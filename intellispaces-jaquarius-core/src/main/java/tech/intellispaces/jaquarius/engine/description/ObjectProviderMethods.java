package tech.intellispaces.jaquarius.engine.description;

import tech.intellispaces.commons.action.functional.FunctionActions;
import tech.intellispaces.commons.function.QuadriFunction;
import tech.intellispaces.commons.function.QuintiFunction;
import tech.intellispaces.commons.function.SexiConsumer;
import tech.intellispaces.commons.function.TriFunction;
import tech.intellispaces.commons.type.Type;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

public interface ObjectProviderMethods {

  static <P, R> ObjectProviderMethodDescription objectProviderMethod(
      P objectProvider,
      String methodName,
      Type<R> returnType,
      Class<?> returnedDomainClass,
      Function<P, R> function
  ) {
    return new ObjectProviderMethodDescriptionImpl(
        objectProvider,
        methodName,
        returnType,
        returnedDomainClass,
        List.of(),
        FunctionActions.ofFunction(function)
    );
  }

  static <P, R, Q> ObjectProviderMethodDescription objectProviderMethod(
      P objectProvider,
      String methodName,
      Type<R> returnType,
      Class<?> returnedDomainClass,
      Type<Q> paramType,
      BiFunction<P, Q, R> function
  ) {
    return new ObjectProviderMethodDescriptionImpl(
        objectProvider,
        methodName,
        returnType,
        returnedDomainClass,
        List.of(paramType),
        FunctionActions.ofBiFunction(function)
    );
  }

  static <P, R, Q1, Q2> ObjectProviderMethodDescription objectProviderMethod(
      P objectProvider,
      String methodName,
      Type<R> returnType,
      Class<?> returnedDomainClass,
      Type<Q1> paramType1,
      Type<Q2> paramType2,
      TriFunction<P, Q1, Q2, R> function
  ) {
    return new ObjectProviderMethodDescriptionImpl(
        objectProvider,
        methodName,
        returnType,
        returnedDomainClass,
        List.of(paramType1, paramType2),
        FunctionActions.ofTriFunction(function)
    );
  }

  static <P, R, Q1, Q2, Q3> ObjectProviderMethodDescription objectProviderMethod(
      P objectProvider,
      String methodName,
      Type<R> returnType,
      Class<?> returnedDomainClass,
      Type<Q1> paramType1,
      Type<Q2> paramType2,
      Type<Q3> paramType3,
      QuadriFunction<P, Q1, Q2, Q3, R> function
  ) {
    return new ObjectProviderMethodDescriptionImpl(
        objectProvider,
        methodName,
        returnType,
        returnedDomainClass,
        List.of(paramType1, paramType2, paramType3),
        FunctionActions.ofQuadriFunction(function)
    );
  }

  static <P, R, Q1, Q2, Q3, Q4> ObjectProviderMethodDescription objectProviderMethod(
      P objectProvider,
      String methodName,
      Type<R> returnType,
      Class<?> returnedDomainClass,
      Type<Q1> paramType1,
      Type<Q2> paramType2,
      Type<Q3> paramType3,
      Type<Q4> paramType4,
      QuintiFunction<P, Q1, Q2, Q3, Q4, R> function
  ) {
    return new ObjectProviderMethodDescriptionImpl(
        objectProvider,
        methodName,
        returnType,
        returnedDomainClass,
        List.of(paramType1, paramType2, paramType3, paramType4),
        FunctionActions.ofQuintiFunction(function)
    );
  }

  static <P, R, Q1, Q2, Q3, Q4, Q5> ObjectProviderMethodDescription objectProviderMethod(
      P objectProvider,
      String methodName,
      Type<R> returnType,
      Class<?> returnedDomainClass,
      Type<Q1> paramType1,
      Type<Q2> paramType2,
      Type<Q3> paramType3,
      Type<Q4> paramType4,
      Type<Q5> paramType5,
      SexiConsumer<P, Q1, Q2, Q3, Q4, Q5, R> function
  ) {
    return new ObjectProviderMethodDescriptionImpl(
        objectProvider,
        methodName,
        returnType,
        returnedDomainClass,
        List.of(paramType1, paramType2, paramType3, paramType4),
        FunctionActions.ofQuintiFunction(function)
    );
  }
}
