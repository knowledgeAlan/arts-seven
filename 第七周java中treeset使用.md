### java中treeset使用

环境:jdk8

### 1:简介

这篇文章讲解java集合框架中实现set接口-`TreeSet` 

### 2:介绍TreeSet

`TreeSet` 是继承`AbstractSet` 有序集合并且实现`NavigableSet` 接口

下面是一些重要总结:

- 不能存放相同元素
- 不保存插入元素顺序
- 默认排序是升序(treeSet#add()增加元素小在左边 大在右边 迭代从左边读取)
- 非线程安全

`TreeSet`实现中，对象是按照自然序升序存储，`TreeSet` 使用平衡二叉搜索树，简单说平衡二叉搜索树，每个节点有额外字节做特殊标记，标记红色和黑色，在删除和插入时候，标记字节确保剩下数还是平衡树，因此让我们创建一个`TreeSet`实例

```java
Set<String> treeSet = new TreeSet();
```



### 2.1:TreeSet带参数构造方法

带参数构造方法可以自定义排序方法

```java
Set<String> treeSet = new TreeSet(Comparator.comparing(String::length));
```

**即使`TreeSet`  线程不安全 ，在外部可以使用Collections.synchronizedSet()包装起来** 

```java
  Set<String> syncTreeSet = Collections.synchronizedSet(treeSet);
```

现在已经知道如何创建`TreeSet` 实例，下面看下常用操作

## 3: TreeSet 增加元素 add()

`TreeSet#add()`可以增加元素，元素增加成功放回true否则false

**add()方法增加只有TreeSet不存相同元素** ，下面例子:

```java
@Test
public void whenAddingElement_shouldAddElement() {
    Set<String> treeSet = new TreeSet();
 
    assertTrue(treeSet.add("String Added"));
 }
```



**add方法是非常重要下面是实现细节** 

```java
public boolean add(E e) {
        return m.put(e, PRESENT)==null;
  }
```



m变量是`TreeMap`(注意`Treemap`实现`NavigableMap` 接口)

```java
/**
  * The backing map.
  */
private transient NavigableMap<E,Object> m;
```

因此，`TreeSet` 内部依赖`NavigableMap` 实例化`TreeMap`当`TreeSet`被创建时

### 4: TreeSet contains()

**contains()方法用于检查给定元素是否存在TreeSet中**,如果元素存在返回true，否则false，下面例子:

```java
@Test
public void whenCheckingForElement_shouldSearchForElement() {
    Set<String> treeSetContains = new TreeSet();
    treeSetContains.add("String Added");
 
    assertTrue(treeSetContains.contains("String Added"));
}
```

## 5: TreeSet remove()

**remove()方法删除指定元素如果存在** ，如果存在元素方法返回true，下面例子:

```java
@Test
public void whenRemovingElement_shouldRemoveElement() {
    Set<String> removeFromTreeSet = new TreeSet();
    removeFromTreeSet.add("String Added");
 
    assertTrue(removeFromTreeSet.remove("String Added"));
}
```

### 6: TreeSet clear()

如果删除所有元素,可以使用clear()方法:

```java
@Test
public void whenClearingTreeSet_shouldClearTreeSet() {
    Set<String> clearTreeSet = new TreeSet();
    clearTreeSet.add("String Added");
    clearTreeSet.clear();
  
    assertTrue(clearTreeSet.isEmpty());
}
```





### 7: TreeSet size()

size()方法用来计算`TreeSet`中大小，这个是基本方法在接口中:

```java
Test
public void whenCheckingTheSizeOfTreeSet_shouldReturnThesize() {
    Set<String> treeSetSize = new TreeSet();
    treeSetSize.add("String Added");
  
    assertEquals(1, treeSetSize.size());
}
```

### 8:  TreeSet isEmpty()

`isEmpty()` 方法判断`TreeSet` 是否为空

```java
@Test
public void whenCheckingForEmptyTreeSet_shouldCheckForEmpty() {
    Set<String> emptyTreeSet = new TreeSet();
     
    assertTrue(emptyTreeSet.isEmpty());
}
```

### 9: TreeSet iterator()

`iterator()` 方法返回升序迭代器,迭代器实现fail-fast失败，下面观察升序迭代器:

```java
@Test
public void whenIteratingTreeSet_shouldIterateTreeSetInAscendingOrder() {
    Set<String> treeSet = new TreeSet();
    treeSet.add("First");
    treeSet.add("Second");
    treeSet.add("Third");
    Iterator<String> itr = treeSet.iterator();
    while (itr.hasNext()) {
        System.out.println(itr.next());
    }
}
```

另外，`TreeSet`可以降序排序输出，下面例子:

```java
@Test
public void whenIteratingTreeSet_shouldIterateTreeSetInDescendingOrder() {
    TreeSet<String> treeSet = new TreeSet();
    treeSet.add("First");
    treeSet.add("Second");
    treeSet.add("Third");
    Iterator<String> itr = treeSet.descendingIterator();
    while (itr.hasNext()) {
        System.out.println(itr.next());
    }
}
```

**迭代器抛出ConcurrentModificationException如果set被修改在迭代创建之后，除非使用迭代器删除remove()方法**，下面例子:

```java
@Test(expected = ConcurrentModificationException.class)
public void whenModifyingTreeSetWhileIterating_shouldThrowException() {
    Set<String> treeSet = new TreeSe();
    treeSet.add("First");
    treeSet.add("Second");
    treeSet.add("Third");
    Iterator<String> itr = treeSet.iterator();
    while (itr.hasNext()) {
        itr.next();
        treeSet.remove("Second");
    }
}
```



如果使用迭代器删除不会抛出异常

```java
@Test
public void whenRemovingElementUsingIterator_shouldRemoveElement() {
  
    Set<String> treeSet = new TreeSet();
    treeSet.add("First");
    treeSet.add("Second");
    treeSet.add("Third");
    Iterator<String> itr = treeSet.iterator();
    while (itr.hasNext()) {
        String element = itr.next();
        if (element.equals("Second"))
           itr.remove();
    }
  
    assertEquals(2, treeSet.size());
}
```

**在并发时候很难保证不被修改在迭代时候**

### 10: TreeSet first()

方法返回第一个元素如果`TreeSet` 不为空,否则抛出异常*NoSuchElementException*，下面例子：

```java
@Test
public void whenCheckingFirstElement_shouldReturnFirstElement() {
    TreeSet<String> treeSet = new TreeSet();
    treeSet.add("First");
    
    assertEquals("First", treeSet.first());
}
```



### 11: TreeSet last()

和上面例子相似，方法返回最后一个元素如果set不为空

```java
@Test
public void whenCheckingLastElement_shouldReturnLastElement() {
    TreeSet<String> treeSet = new TreeSet();
    treeSet.add("First");
    treeSet.add("Last");
     
    assertEquals("Last", treeSet.last());
}
```

### 12: TreeSet subSet()

该方法返回从*fromElement*到*toElement* 范围值，注意fromElement包含toElement不包含

```java
@Test
public void whenUsingSubSet_shouldReturnSubSetElements() {
    SortedSet<Integer> treeSet = new TreeSet();
    treeSet.add(1);
    treeSet.add(2);
    treeSet.add(3);
    treeSet.add(4);
    treeSet.add(5);
    treeSet.add(6);
     
    Set<Integer> expectedSet = new TreeSet<>();
    expectedSet.add(2);
    expectedSet.add(3);
    expectedSet.add(4);
    expectedSet.add(5);
 
    Set<Integer> subSet = treeSet.subSet(2, 6);
  
    assertEquals(expectedSet, subSet);
}
```

### 13: TreeSet headSet()

方法返回TreeSet小于指定元素set：

```java
@Test
public void whenUsingHeadSet_shouldReturnHeadSetElements() {
    SortedSet<Integer> treeSet = new TreeSet();
    treeSet.add(1);
    treeSet.add(2);
    treeSet.add(3);
    treeSet.add(4);
    treeSet.add(5);
    treeSet.add(6);
 
    Set<Integer> subSet = treeSet.headSet(6);
  
    assertEquals(subSet, treeSet.subSet(1, 6));
}
```

### 14： TreeSet tailSet()

方法返回大于或等于元素后面set

```java
@Test
public void whenUsingTailSet_shouldReturnTailSetElements() {
    NavigableSet<Integer> treeSet = new TreeSet();
    treeSet.add(1);
    treeSet.add(2);
    treeSet.add(3);
    treeSet.add(4);
    treeSet.add(5);
    treeSet.add(6);
 
    Set<Integer> subSet = treeSet.tailSet(3);
  
    assertEquals(subSet, treeSet.subSet(3, true, 6, true));
}
```

### 15:null元素排序

java7之前，增加null元素到空TreeSet是可以的，然而这样会造成一次bug，因此`TreeSet`不在支持添加空元素，当添加元素到`TreeSet`时，元素按照自然排序或者按照自定义`comparator` ,因此增加null，和存在元素比较，会抛出*NullPointerException*

因为null不能比交和任何值:

```java
@Test(expected = NullPointerException.class)
public void whenAddingNullToNonEmptyTreeSet_shouldThrowException() {
    Set<String> treeSet = new TreeSet();
    treeSet.add("First");
    treeSet.add(null);
}
```



元素插入`TreeSet`一定实现*Comparable* 接口或者至少能比较，**所有元素必须支持比较**

例子：**e1.compareTo(e2) or comparator.compare(e1, e2)** **不能抛出异常**

```java
class Element {
    private Integer id;
 
    // Other methods...
}
 
Comparator<Element> comparator = (ele1, ele2) -> {
    return ele1.getId().compareTo(ele2.getId());
};
 
@Test
public void whenUsingComparator_shouldSortAndInsertElements() {
    Set<Element> treeSet = new TreeSet(comparator);
    Element ele1 = new Element();
    ele1.setId(100);
    Element ele2 = new Element();
    ele2.setId(200);
     
    treeSet.add(ele1);
    treeSet.add(ele2);
     
    System.out.println(treeSet);
}
```



### 16:TreeSet性能比较

当`HashSet`和`TreeSet`性能进行比较，`TreeSet`性能低，像增加 删除 和搜时间复杂度O(logn)  当打印n个元素时间复杂度是O(n),`HashSet`操作 新增 删除和contains方法都是O(1)

`TreeSet`应该是我们主要选择如果想要保持元素有序 并且升序和降序访问遍历，升序操作比降序要快

以下有一些规则-如果相同值现象或者和本地存储 频繁访问依靠于内存模式

- 相似数据经常访问被应用程序一样快

- If two entries are nearby given an ordering, a *TreeSet* places them near each other in the data structure, and hence in memory

  

  

  A *TreeSet* being a data-structure with greater locality we can, therefore, conclude in accordance to the Principle of Locality, that we should give preference to a *TreeSet* if we're short on memory and if we want to access elements that are relatively close to each other according to their natural ordering.

  In case data needs to be read from the hard drive (which has greater latency than data read from the cache or memory) then prefer *TreeSet* as it has greater locality（**英文中不能理解，希望有大神知道，请告知下，谢谢**）

  ### 17：总结

  本文重点理解如何使用标准`TreeSet` 实现，理解如何高效使用避免重复和排序元素
[原文链接](https://www.baeldung.com/java-tree-set)
  