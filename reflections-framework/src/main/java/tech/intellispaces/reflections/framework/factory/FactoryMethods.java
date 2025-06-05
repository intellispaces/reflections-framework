package tech.intellispaces.reflections.framework.factory;

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
import tech.intellispaces.reflections.framework.space.domain.DomainFunctions;

public interface FactoryMethods {

  static <P, R> FactoryMethod factoryMethod(
      P factoryInstance,
      String contractType,
      Type<R> returnType,
      Class<?> returnedDomainClass,
      Function<P, R> function
  ) {
    return new FactoryMethodImpl(
        factoryInstance,
        contractType,
        returnType,
        DomainFunctions.getDomain(returnedDomainClass),
        List.of(),
        List.of(),
        FunctionActions.ofFunction(function)
    );
  }

  static <P, R, Q> FactoryMethod factoryMethod(
      P factoryInstance,
      String contractType,
      Type<R> returnType,
      Class<?> returnedDomainClass,
      String qualifierName,
      Type<Q> qualifierType,
      BiFunction<P, Q, R> function
  ) {
    return new FactoryMethodImpl(
        factoryInstance,
        contractType,
        returnType,
        DomainFunctions.getDomain(returnedDomainClass),
        List.of(qualifierName),
        List.of(qualifierType),
        FunctionActions.ofBiFunction(function)
    );
  }

  static <P, R, Q1, Q2> FactoryMethod factoryMethod(
      P factoryInstance,
      String contractType,
      Type<R> returnType,
      Class<?> returnedDomainClass,
      String qualifierName1, Type<Q1> qualifierType1,
      String qualifierName2, Type<Q2> qualifierType2,
      Function3<P, Q1, Q2, R> function
  ) {
    return new FactoryMethodImpl(
        factoryInstance,
        contractType,
        returnType,
        DomainFunctions.getDomain(returnedDomainClass),
        List.of(qualifierName1, qualifierName2),
        List.of(qualifierType1, qualifierType2),
        FunctionActions.ofFunction3(function)
    );
  }

  static <P, R, Q1, Q2, Q3> FactoryMethod factoryMethod(
      P factoryInstance,
      String contractType,
      Type<R> returnType,
      Class<?> returnedDomainClass,
      String qualifierName1, Type<Q1> qualifierType1,
      String qualifierName2, Type<Q2> qualifierType2,
      String qualifierName3, Type<Q3> qualifierType3,
      Function4<P, Q1, Q2, Q3, R> function
  ) {
    return new FactoryMethodImpl(
        factoryInstance,
        contractType,
        returnType,
        DomainFunctions.getDomain(returnedDomainClass),
        List.of(qualifierName1, qualifierName2, qualifierName3),
        List.of(qualifierType1, qualifierType2, qualifierType3),
        FunctionActions.ofFunction4(function)
    );
  }

  static <P, R, Q1, Q2, Q3, Q4> FactoryMethod factoryMethod(
      P factoryInstance,
      String contractType,
      Type<R> returnType,
      Class<?> returnedDomainClass,
      String qualifierName1, Type<Q1> qualifierType1,
      String qualifierName2, Type<Q2> qualifierType2,
      String qualifierName3, Type<Q3> qualifierType3,
      String qualifierName4, Type<Q4> qualifierType4,
      Function5<P, Q1, Q2, Q3, Q4, R> function
  ) {
    return new FactoryMethodImpl(
        factoryInstance,
        contractType,
        returnType,
        DomainFunctions.getDomain(returnedDomainClass),
        List.of(qualifierName1, qualifierName2, qualifierName3, qualifierName4),
        List.of(qualifierType1, qualifierType2, qualifierType3, qualifierType4),
        FunctionActions.ofFunction5(function)
    );
  }

  static <P, R, Q1, Q2, Q3, Q4, Q5> FactoryMethod factoryMethod(
      P factoryInstance,
      String contractType,
      Type<R> returnType,
      Class<?> returnedDomainClass,
      String qualifierName1, Type<Q1> qualifierType1,
      String qualifierName2, Type<Q2> qualifierType2,
      String qualifierName3, Type<Q3> qualifierType3,
      String qualifierName4, Type<Q4> qualifierType4,
      String qualifierName5, Type<Q5> qualifierType5,
      Function6<P, Q1, Q2, Q3, Q4, Q5, R> function
  ) {
    return new FactoryMethodImpl(
        factoryInstance,
        contractType,
        returnType,
        DomainFunctions.getDomain(returnedDomainClass),
        List.of(qualifierName1, qualifierName2, qualifierName3, qualifierName4, qualifierName5),
        List.of(qualifierType1, qualifierType2, qualifierType3, qualifierType4, qualifierType5),
        FunctionActions.ofFunction6(function)
    );
  }

  static <P, R, Q1, Q2, Q3, Q4, Q5, Q6> FactoryMethod factoryMethod(
      P factoryInstance,
      String contractType,
      Type<R> returnType,
      Class<?> returnedDomainClass,
      String qualifierName1, Type<Q1> qualifierType1,
      String qualifierName2, Type<Q2> qualifierType2,
      String qualifierName3, Type<Q3> qualifierType3,
      String qualifierName4, Type<Q4> qualifierType4,
      String qualifierName5, Type<Q5> qualifierType5,
      String qualifierName6, Type<Q6> qualifierType6,
      Function7<P, Q1, Q2, Q3, Q4, Q5, Q6, R> function
  ) {
    return new FactoryMethodImpl(
        factoryInstance,
        contractType,
        returnType,
        DomainFunctions.getDomain(returnedDomainClass),
        List.of(qualifierName1, qualifierName2, qualifierName3, qualifierName4, qualifierName5, qualifierName6),
        List.of(qualifierType1, qualifierType2, qualifierType3, qualifierType4, qualifierType5, qualifierType6),
        FunctionActions.ofFunction7(function)
    );
  }

  static <P, R, Q1, Q2, Q3, Q4, Q5, Q6, Q7> FactoryMethod factoryMethod(
      P factoryInstance,
      String contractType,
      Type<R> returnType,
      Class<?> returnedDomainClass,
      String qualifierName1, Type<Q1> qualifierType1,
      String qualifierName2, Type<Q2> qualifierType2,
      String qualifierName3, Type<Q3> qualifierType3,
      String qualifierName4, Type<Q4> qualifierType4,
      String qualifierName5, Type<Q5> qualifierType5,
      String qualifierName6, Type<Q6> qualifierType6,
      String qualifierName7, Type<Q7> qualifierType7,
      Function8<P, Q1, Q2, Q3, Q4, Q5, Q6, Q7, R> function
  ) {
    return new FactoryMethodImpl(
        factoryInstance,
        contractType,
        returnType,
        DomainFunctions.getDomain(returnedDomainClass),
        List.of(qualifierName1, qualifierName2, qualifierName3, qualifierName4, qualifierName5, qualifierName6, qualifierName7),
        List.of(qualifierType1, qualifierType2, qualifierType3, qualifierType4, qualifierType5, qualifierType6, qualifierType7),
        FunctionActions.ofFunction8(function)
    );
  }

  static <P, R, Q1, Q2, Q3, Q4, Q5, Q6, Q7, Q8> FactoryMethod factoryMethod(
      P factoryInstance,
      String contractType,
      Type<R> returnType,
      Class<?> returnedDomainClass,
      String qualifierName1, Type<Q1> qualifierType1,
      String qualifierName2, Type<Q2> qualifierType2,
      String qualifierName3, Type<Q3> qualifierType3,
      String qualifierName4, Type<Q4> qualifierType4,
      String qualifierName5, Type<Q5> qualifierType5,
      String qualifierName6, Type<Q6> qualifierType6,
      String qualifierName7, Type<Q7> qualifierType7,
      String qualifierName8, Type<Q8> qualifierType8,
      Function9<P, Q1, Q2, Q3, Q4, Q5, Q6, Q7, Q8, R> function
  ) {
    return new FactoryMethodImpl(
        factoryInstance,
        contractType,
        returnType,
        DomainFunctions.getDomain(returnedDomainClass),
        List.of(qualifierName1, qualifierName2, qualifierName3, qualifierName4, qualifierName5, qualifierName6, qualifierName7, qualifierName8),
        List.of(qualifierType1, qualifierType2, qualifierType3, qualifierType4, qualifierType5, qualifierType6, qualifierType7, qualifierType8),
        FunctionActions.ofFunction9(function)
    );
  }

  static <P, R, Q1, Q2, Q3, Q4, Q5, Q6, Q7, Q8, Q9> FactoryMethod factoryMethod(
      P factoryInstance,
      String contractType,
      Type<R> returnType,
      Class<?> returnedDomainClass,
      String qualifierName1, Type<Q1> qualifierType1,
      String qualifierName2, Type<Q2> qualifierType2,
      String qualifierName3, Type<Q3> qualifierType3,
      String qualifierName4, Type<Q4> qualifierType4,
      String qualifierName5, Type<Q5> qualifierType5,
      String qualifierName6, Type<Q6> qualifierType6,
      String qualifierName7, Type<Q7> qualifierType7,
      String qualifierName8, Type<Q8> qualifierType8,
      String qualifierName9, Type<Q9> qualifierType9,
      Function10<P, Q1, Q2, Q3, Q4, Q5, Q6, Q7, Q8, Q9, R> function
  ) {
    return new FactoryMethodImpl(
        factoryInstance,
        contractType,
        returnType,
        DomainFunctions.getDomain(returnedDomainClass),
        List.of(qualifierName1, qualifierName2, qualifierName3, qualifierName4, qualifierName5, qualifierName6, qualifierName7, qualifierName8, qualifierName9),
        List.of(qualifierType1, qualifierType2, qualifierType3, qualifierType4, qualifierType5, qualifierType6, qualifierType7, qualifierType8, qualifierType9),
        FunctionActions.ofFunction10(function)
    );
  }
}
