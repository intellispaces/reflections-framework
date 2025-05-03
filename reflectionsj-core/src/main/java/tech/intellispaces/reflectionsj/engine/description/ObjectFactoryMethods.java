package tech.intellispaces.reflectionsj.engine.description;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

import tech.intellispaces.actions.functional.FunctionActions;
import tech.intellispaces.commons.function.Function10;
import tech.intellispaces.commons.function.Function3;
import tech.intellispaces.commons.function.Function4;
import tech.intellispaces.commons.function.Function5;
import tech.intellispaces.commons.function.Function6;
import tech.intellispaces.commons.function.Function7;
import tech.intellispaces.commons.function.Function8;
import tech.intellispaces.commons.function.Function9;
import tech.intellispaces.commons.type.Type;

public interface ObjectFactoryMethods {

  static <P, R> ObjectFactoryMethodDescription objectFactoryMethod(
      P objectFactory,
      String methodName,
      Type<R> returnType,
      Class<?> returnedDomainClass,
      Function<P, R> function
  ) {
    return new ObjectFactoryMethodDescriptionImpl(
        objectFactory,
        methodName,
        returnType,
        returnedDomainClass,
        List.of(),
        FunctionActions.ofFunction(function)
    );
  }

  static <P, R, Q> ObjectFactoryMethodDescription objectFactoryMethod(
      P objectFactory,
      String methodName,
      Type<R> returnType,
      Class<?> returnedDomainClass,
      Type<Q> paramType,
      BiFunction<P, Q, R> function
  ) {
    return new ObjectFactoryMethodDescriptionImpl(
        objectFactory,
        methodName,
        returnType,
        returnedDomainClass,
        List.of(paramType),
        FunctionActions.ofBiFunction(function)
    );
  }

  static <P, R, Q1, Q2> ObjectFactoryMethodDescription objectFactoryMethod(
      P objectFactory,
      String methodName,
      Type<R> returnType,
      Class<?> returnedDomainClass,
      Type<Q1> paramType1,
      Type<Q2> paramType2,
      Function3<P, Q1, Q2, R> function
  ) {
    return new ObjectFactoryMethodDescriptionImpl(
        objectFactory,
        methodName,
        returnType,
        returnedDomainClass,
        List.of(paramType1, paramType2),
        FunctionActions.ofFunction3(function)
    );
  }

  static <P, R, Q1, Q2, Q3> ObjectFactoryMethodDescription objectFactoryMethod(
      P objectFactory,
      String methodName,
      Type<R> returnType,
      Class<?> returnedDomainClass,
      Type<Q1> paramType1,
      Type<Q2> paramType2,
      Type<Q3> paramType3,
      Function4<P, Q1, Q2, Q3, R> function
  ) {
    return new ObjectFactoryMethodDescriptionImpl(
        objectFactory,
        methodName,
        returnType,
        returnedDomainClass,
        List.of(paramType1, paramType2, paramType3),
        FunctionActions.ofFunction4(function)
    );
  }

  static <P, R, Q1, Q2, Q3, Q4> ObjectFactoryMethodDescription objectFactoryMethod(
      P objectFactory,
      String methodName,
      Type<R> returnType,
      Class<?> returnedDomainClass,
      Type<Q1> paramType1,
      Type<Q2> paramType2,
      Type<Q3> paramType3,
      Type<Q4> paramType4,
      Function5<P, Q1, Q2, Q3, Q4, R> function
  ) {
    return new ObjectFactoryMethodDescriptionImpl(
        objectFactory,
        methodName,
        returnType,
        returnedDomainClass,
        List.of(paramType1, paramType2, paramType3, paramType4),
        FunctionActions.ofFunction5(function)
    );
  }

  static <P, R, Q1, Q2, Q3, Q4, Q5> ObjectFactoryMethodDescription objectFactoryMethod(
      P objectFactory,
      String methodName,
      Type<R> returnType,
      Class<?> returnedDomainClass,
      Type<Q1> paramType1,
      Type<Q2> paramType2,
      Type<Q3> paramType3,
      Type<Q4> paramType4,
      Type<Q5> paramType5,
      Function6<P, Q1, Q2, Q3, Q4, Q5, R> function
  ) {
    return new ObjectFactoryMethodDescriptionImpl(
        objectFactory,
        methodName,
        returnType,
        returnedDomainClass,
        List.of(paramType1, paramType2, paramType3, paramType4, paramType5),
        FunctionActions.ofFunction6(function)
    );
  }

  static <P, R, Q1, Q2, Q3, Q4, Q5, Q6> ObjectFactoryMethodDescription objectFactoryMethod(
      P objectFactory,
      String methodName,
      Type<R> returnType,
      Class<?> returnedDomainClass,
      Type<Q1> paramType1,
      Type<Q2> paramType2,
      Type<Q3> paramType3,
      Type<Q4> paramType4,
      Type<Q5> paramType5,
      Type<Q6> paramType6,
      Function7<P, Q1, Q2, Q3, Q4, Q5, Q6, R> function
  ) {
    return new ObjectFactoryMethodDescriptionImpl(
        objectFactory,
        methodName,
        returnType,
        returnedDomainClass,
        List.of(paramType1, paramType2, paramType3, paramType4, paramType5, paramType6),
        FunctionActions.ofFunction7(function)
    );
  }

  static <P, R, Q1, Q2, Q3, Q4, Q5, Q6, Q7> ObjectFactoryMethodDescription objectFactoryMethod(
      P objectFactory,
      String methodName,
      Type<R> returnType,
      Class<?> returnedDomainClass,
      Type<Q1> paramType1,
      Type<Q2> paramType2,
      Type<Q3> paramType3,
      Type<Q4> paramType4,
      Type<Q5> paramType5,
      Type<Q6> paramType6,
      Type<Q7> paramType7,
      Function8<P, Q1, Q2, Q3, Q4, Q5, Q6, Q7, R> function
  ) {
    return new ObjectFactoryMethodDescriptionImpl(
        objectFactory,
        methodName,
        returnType,
        returnedDomainClass,
        List.of(paramType1, paramType2, paramType3, paramType4, paramType5, paramType6, paramType7),
        FunctionActions.ofFunction8(function)
    );
  }

  static <P, R, Q1, Q2, Q3, Q4, Q5, Q6, Q7, Q8> ObjectFactoryMethodDescription objectFactoryMethod(
      P objectFactory,
      String methodName,
      Type<R> returnType,
      Class<?> returnedDomainClass,
      Type<Q1> paramType1,
      Type<Q2> paramType2,
      Type<Q3> paramType3,
      Type<Q4> paramType4,
      Type<Q5> paramType5,
      Type<Q6> paramType6,
      Type<Q7> paramType7,
      Type<Q8> paramType8,
      Function9<P, Q1, Q2, Q3, Q4, Q5, Q6, Q7, Q8, R> function
  ) {
    return new ObjectFactoryMethodDescriptionImpl(
        objectFactory,
        methodName,
        returnType,
        returnedDomainClass,
        List.of(paramType1, paramType2, paramType3, paramType4, paramType5, paramType6, paramType7, paramType8),
        FunctionActions.ofFunction9(function)
    );
  }

  static <P, R, Q1, Q2, Q3, Q4, Q5, Q6, Q7, Q8, Q9> ObjectFactoryMethodDescription objectFactoryMethod(
      P objectFactory,
      String methodName,
      Type<R> returnType,
      Class<?> returnedDomainClass,
      Type<Q1> paramType1,
      Type<Q2> paramType2,
      Type<Q3> paramType3,
      Type<Q4> paramType4,
      Type<Q5> paramType5,
      Type<Q6> paramType6,
      Type<Q7> paramType7,
      Type<Q8> paramType8,
      Type<Q9> paramType9,
      Function10<P, Q1, Q2, Q3, Q4, Q5, Q6, Q7, Q8, Q9, R> function
  ) {
    return new ObjectFactoryMethodDescriptionImpl(
        objectFactory,
        methodName,
        returnType,
        returnedDomainClass,
        List.of(paramType1, paramType2, paramType3, paramType4, paramType5, paramType6, paramType7, paramType8, paramType9),
        FunctionActions.ofFunction10(function)
    );
  }
}
