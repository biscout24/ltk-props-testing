```scala

package scala
package collection

trait SeqLike[+A, +Repr] extends ...

  /** Sorts this $coll according to a comparison function.
   *  $willNotTerminateInf
   *
   *  The sort is stable. That is, elements that are equal (as determined by
   *  `lt`) appear in the same order in the sorted sequence as in the original.
   *
   *  @param  lt  the comparison function which tests whether
   *              its first argument precedes its second argument in
   *              the desired ordering.
   *  @return     a $coll consisting of the elements of this $coll
   *              sorted according to the comparison function `lt`.
   *  @example {{{
   *    List("Steve", "Tom", "John", "Bob").sortWith(_.compareTo(_) < 0) =
   *    List("Bob", "John", "Steve", "Tom")
   *  }}}
   */
  def sortWith(lt: (A, A) => Boolean): Repr = sorted(Ordering fromLessThan lt)

...
```


```scala
[info] SortWithTest:
[info] List.sortWith
[info] - should produce the same list while sort twice *** FAILED ***
[info]   IllegalArgumentException was thrown during property evaluation.
[info]     Message: Comparison method violates its general contract!
[info]     Occurred when passed generated values (
[info]       arg0 = List(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -2147483648, 
                         0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -2147483648, 0, 0, 0, 0, -1, -1) // 736 shrinks
[info]     )
```