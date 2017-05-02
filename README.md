# expeval
This is a simple arithmetic expression evaluator wirtten in Java. It is 
self-contained and doesn't depend on the Java libraries.

To compile the code, use the following commands in the terminal:
```
javac -d . Stack.java
javac -d . Queue.java
javac -d . Map.java
javac -d . ArithmeticExpressionEvaluator.java
```

To use the code, run it and pass an arithmetic expression as the first
argument. For examples:
```
java expeval.ArithmeticExpressionEvaluator "log((2+3*5)ˆ-2/sin(pi/3))"
```

And the output will be:
```
Original: log((2+3*5)ˆ-2/sin(pi/3))
Infix: log ( ( 2 + 3 * 5 ) ˆ -2 / sin ( pi / 3) )
Postfix : 2 3 5 * + -2 ˆ pi 3 / sin / log
Result : -2.398428474452398
```

