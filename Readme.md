Code files:
src/main/java/com/evarlamova/RandomGen.java - implementation
src/test/java/com/evarlamova/RandomGenTest.java - tests

How to run tests:
1) Install Java 11 + 
For example using SDK man (sdk man installation https://sdkman.io/install)
Run `sdk install 19.0.2-open`
2) Execute command to run tests
`./mvnw test`





Algorithm:
1) In constructor, we check all exception cases, probability sum != 1. Probabilities array length != number array length.
2) In constructor we generate ranges array, for example using your test case:
`[-1, 0, 1, 2, 3] , [0.01, 0.3, 0.58, 0.1, 0.01]` gives us => `[0, 0.01, 0.31, 0.89, 0.99, 1]`
This representation means that, -1 in [0, 0.01), 0 in [0.01, 0.31), 1 in [0.31, 0.89), 2 in [0.89, 0.99), 3 in [0.99, 1).
3) During nextNum(), we generate pseudo random seed between [0, 1), using java random.
Which evenly distributed, according to doc.
4) During nextNum(), searching for interval there this num belongs, it can be done using linear scan, 
by I am using binary search since intervals sorted, gives me O(log(N)) complexity for nextNum() instead of O(N)
Pseudo run:
Random generate num 0.005
```[0, 0.01, 0.31, 0.89, 0.99, 1]``` - take idx 2 as mid, taking range [0.31, 0.89) 0.005 does not belong that interval
check if 0.005 >= 0.89 if not goes left subarray, if so goes right subarray, repeat after finding the interval.

Complexity of nextNum():

Space complexity, O(1) - we don't use any temp structures, or recursion calls
Time compexity, O(log(N)), where N - length of numbers. Using BS to find interval.
