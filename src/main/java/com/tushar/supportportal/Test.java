package com.tushar.supportportal;

public class Test {
	public static void main(String[] args) {

		for (int i = 0; ; i++) {
			new Thread(() -> {
				System.out.println("Thread: " + Thread.currentThread().getName());
			}).start();
		}

//		List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);
//		List<Integer> numbers2 = Arrays.asList(1, 3, 4, 5, 2, 5, 6);
//		Set<Integer> numbersSet = new HashSet<>(numbers2);
//		List<List<String>> listOfLists = Arrays.asList(
//				Arrays.asList("Apple", "Banana"),
//				Arrays.asList("Cricket", "Football"),
//				Arrays.asList("Java", "Python")
//		);
//
//		List<String> list1 = listOfLists.stream().flatMap(Collection::stream).toList(); //[Apple, Banana, Cricket, Football, Java, Python]
//		System.out.println(list1);
//		Set<Integer> sorted = numbersSet.stream().distinct().collect(Collectors.toSet());
//		System.out.println(sorted);

//		List<Integer> list = numbers.stream().map(i -> i * 10).filter(num -> num > 30).toList(); // [40, 50]
//
//		System.out.println(list);
	}
}


/*
*List<Integer> numbers2 = Arrays.asList(1, 3, 4, 5, 2, 5);
*
*
*
* List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);
multiple each num by 10
after that print only num greater than 30
*
* */
