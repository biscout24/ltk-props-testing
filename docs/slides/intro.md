### Introduction to properties
based on ["Choosing properties for property-based testing"](https://fsharpforfunandprofit.com/posts/property-based-testing-2/) from Scott Wlaschin blog
Note: if you generate the data, how could you check the results? Properties for the rescue.  
You think about your code as a set of laws, or algebra
Here is a list of patterns we could find


### "Different paths, same destination"
aka commutative property

![](./img/property_commutative.png "commutative property")
Note: 


### "There and back again"
aka symmetry

![](./img/property_inverse.png "symmetry")
Note: What is a typical example of it? Serialization/Desirialization 


### "Some things never change"
aka invariants

![](./img/property_invariant.png "invariants")
Note: e.g. size of a collection, max/min element etc.


### "The more things change, the more they stay the same"
aka idempotence

![](./img/property_idempotence.png "idempotence")
```scala
  "List.sortWith" should "produce the same list while sort twice" in forAll(minSuccessful(500)) { 
    l: List[Int] =>
      l.sortWith(mySortFun) should be(l.sortWith(mySortFun).sortWith(mySortFun))
  }
```
Note: 


### "Solve a smaller problem first"
aka induction

![](./img/property_induction.png "property_induction.png")
Note: some property is true for these smaller parts, then you can often prove that the property is true for a large thing as well


### "The test oracle"
compare to an alternate version 

![](./img/property_test_oracle.png "property_test_oracle.png")
Note: In DI we used it for testing some alternative implementations of an existing algorithm.  
For instance, you might have a high-performance algorithm with optimization tweaks that you want to test. 
In this case, you might compare it with a brute force algorithm that is much slower but is also much easier to write correctly.
Similarly, you might compare the result of a parallel or concurrent algorithm with the result of a linear, single thread version.


### "Consistency"
#### Property check configuration

| Configuration Parameter | Default Value | Meaning  
| ----------------------- | ------------- | ------- 
| workers                 | 1             | specifies the number of worker threads to use during property evaluation 

Note: you could write stateful tests and run it in parallel by means of configuration. 
Usual pattern here - you check shared state: file system, database, shared object for consistency
Check John Hughes presentation "Testing the Hard Stuff and Staying Sane", where he tested Erlang `dets` implementation  