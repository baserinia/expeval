//******************************************************************************
//
// Copyright (c) 2017, Amir Baserinia (www.baserinia.com)
//
// Permission to use, copy, modify, and/or distribute this software for any
// purpose with or without fee is hereby granted, provided that the above
// copyright notice and this permission notice appear in all copies.
//
// THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES WITH
// REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF MERCHANTABILITY
// AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY SPECIAL, DIRECT,
// INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES WHATSOEVER RESULTING FROM
// LOSS OF USE, DATA OR PROFITS, WHETHER IN AN ACTION OF CONTRACT, NEGLIGENCE
// OR OTHER TORTIOUS ACTION, ARISING OUT OF OR IN CONNECTION WITH THE USE OR
// PERFORMANCE OF THIS SOFTWARE.
//
//******************************************************************************
//
//  A stack-based arithmetic expression evaluator
//
//******************************************************************************

package expeval;

import java.lang.Math;

public class ArithmeticExpressionEvaluator {

    // original arithmetic expression entered by user
    private String originalExpression;
    
    // infix arthimetic expression after some cleanup
    private Queue<String> infixExpression;

    // postfix expression stored in a queue
    private Queue<String> postfixExpression;
    
     // map of operators, functions, and constants
    private Map<String, Operator> operators;
    private Map<String, Function> functions;
    private Map<String, Constant> constants;
    
    // Constructor to initialize an arithmetic expression
    public ArithmeticExpressionEvaluator(String expr) {
        originalExpression = expr;
        
        // map of arithmetic operators
        operators = new Map<>();
        functions = new Map<>();
        constants = new Map<>();
        
        // addition: register operator
        registerOperator(new Operator("+", 1, true) {
            @Override
            public double eval(double a, double b) { return a + b; }
        });

        // subtraction: register operator
        registerOperator(new Operator("-", 1, true) {
            @Override
            public double eval(double a, double b) { return a - b; }
        });

        // multiplication
        registerOperator(new Operator("*", 2, true) {
            @Override
            public double eval(double a, double b) { return a * b; }
        });

        // division
        registerOperator(new Operator("/", 2, true) {
            @Override
            public double eval(double a, double b) { return a / b; }
        });

        // exponentiation; also the only right associative operator
        registerOperator(new Operator("^", 3, false) {
            @Override
            public double eval(double a, double b) { return Math.pow(a, b); }
        });

        // pi ~= 3.14159
        registerConstant(new Constant("pi") {
            @Override
            public double eval() { return Math.PI; }
        });

        // e ~= 2.71828
        registerConstant(new Constant("e") {
            @Override
            public double eval() { return Math.E; }
        });

        // Absolute value
        registerFunction(new Function("abs", 1, 4) {
            @Override
            public double eval(double x) { return Math.abs(x); }
        });
        
        // sin
        registerFunction(new Function("sin", 1, 4) {
            @Override
            public double eval(double x) { return Math.sin(x); }
        });

        // cos
        registerFunction(new Function("cos", 1, 4) {
            @Override
            public double eval(double x) { return Math.cos(x); }
        });

        // tan
        registerFunction(new Function("tan", 1, 4) {
            @Override
            public double eval(double x) { return Math.tan(x); }
        });

        // log
        registerFunction(new Function("log", 1, 4) {
            @Override
            public double eval(double x) { return Math.log10(x); }
        });

        // log
        registerFunction(new Function("ln", 1, 4) {
            @Override
            public double eval(double x) { return Math.log(x); }
        });

        // exp
        registerFunction(new Function("exp", 1, 4) {
            @Override
            public double eval(double x) { return Math.exp(x); }
        });

        infixExpression = new Queue<>();
        Tokenizer tokenizer = new Tokenizer(originalExpression);
        String token  = tokenizer.getNextToken();
        while (token != null) {
            infixExpression.add(token);
            token = tokenizer.getNextToken();
        }
    }
    
    // return the string representation of the infix expression
    public String getInfixString() {
        // cached result for improved performace
        if (infixExpression == null) {
            infixExpression = new Queue<>();
            Tokenizer tokenizer = new Tokenizer(originalExpression);
            String token  = tokenizer.getNextToken();
            while (token != null) {
                infixExpression.add(token);
                token = tokenizer.getNextToken();
            }
        }
        return infixExpression.toString(); 
    }
    
    // convert infix to postfix
    public String convInfixToPostfix() {
        postfixExpression = new Queue<>();
        Stack<String> operStack = new Stack<>(); // operator stack
        String token = infixExpression.remove();
        while (token != null) {
            if (token.equals("(")) {
                operStack.push(token);
            } else if (token.equals(")")) {
                String oper = operStack.pop();
                while ((oper != null) && !oper.equals("(")) {
                    postfixExpression.add(oper);
                    oper = operStack.pop();
                }
            } else if (operators.containsKey(token)) {
                // handle operators
                if (operStack.isEmpty() || operStack.peek().equals("(")) {
                    operStack.push(token);
                } else {
                    Operator tokenOper = operators.get(token);
                    String stackOper = operStack.pop();
                    while ((stackOper != null) && !stackOper.equals("(")) {
                        if (operators.containsKey(stackOper)) {
                            // check precedence
                            Operator op = operators.get(stackOper);
                            if (tokenOper.precedence() > op.precedence()) {
                                operStack.push(stackOper);
                                break;
                            } else if (tokenOper.precedence() == 
                                    op.precedence()) {
                                // equal precedence? check association
                                if (tokenOper.isLeftAssociated()) {
                                    postfixExpression.add(stackOper);
                                } else {
                                    operStack.push(stackOper);
                                }
                                break;
                            } else {
                                postfixExpression.add(stackOper);
                            }
                        } else if (functions.containsKey(stackOper)) {
                            postfixExpression.add(stackOper);
                        }
                        stackOper = operStack.pop();
                    }
                    operStack.push(token);
                }
            } else if (functions.containsKey(token)) {
                // handle functions
                operStack.push(token);
            } else {
                // handle constants and numerical values
                postfixExpression.add(token);
            }
            token = infixExpression.remove();
        }
        
        token = operStack.pop();
        while ((token != null) && (!token.equals("("))) {
            postfixExpression.add(token);
            token = operStack.pop();
        }
        return postfixExpression.toString();
    }

    // evaluate postfix expression
    public double evalPostfix() {
        Stack<Double> resultStack = new Stack<>();
        String token = postfixExpression.remove();
        while (token != null) {
            if (operators.containsKey(token)) {
                // token is an operator
                Operator op = operators.get(token);
                double b = resultStack.pop();
                double a = resultStack.pop();
                resultStack.push(op.eval(a, b));
            } else if (functions.containsKey(token)) {
                // token is a function
                Function func = functions.get(token);
                double x = resultStack.pop();
                resultStack.push(func.eval(x));
            } else if (constants.containsKey(token)) {
                // token is a symbolic constant
                Constant con = constants.get(token);
                resultStack.push(con.eval());
            } else {
                // token must a number; otherwise, return error and terminate
                try {
                    resultStack.push(Double.parseDouble(token));
                } 
                catch (NumberFormatException e) {
                    System.out.println("Error: Invalid identifier \"" + 
                        token + "\"");
                    System.exit(1);
                }
            }
            token = postfixExpression.remove();
        }
        return resultStack.pop();
    }
    
    // abstract base class for operators, functions, and constants
    private abstract class Token {
        protected String name; // string representation        
        protected int precedence;
        public String toString() { return name; }
        public int precedence() { return precedence; }
    }

    // Implements arithmetic operators (+, -, *, /, ^)
    private abstract class Operator extends Token {
        private boolean leftAssociated; // true for leftAssociated
        
        // constructor
        public Operator(String name, int precedence, boolean assoc) {
            this.name = name;
            this.precedence = precedence;
            this.leftAssociated = assoc;
        }

        // return associativity
        public boolean isLeftAssociated() { return leftAssociated; }
        
        // evaluate operator; implement at construction
        public abstract double eval(double a, double b);
    }

    // add an operator to 
    private void registerOperator(Operator operator) {
        operators.put(operator.toString(), operator);
    }

    // Implements arithmetic functions (abs, sin, cos, etc.)
    private abstract class Function extends Token {
        private int numOfArgs; // num of arguments

        // constructor
        public Function(String name, int nArg, int prec) {
            this.name = name;
            this.numOfArgs = nArg;
            this.precedence = prec;
        }

        // return precendence
        public int numOfArgs() { return numOfArgs; }

        // evaluate operator; implement at construction
        public abstract double eval(double x);
    }

    // add an operator to 
    private void registerFunction(Function function) {
        functions.put(function.toString(), function);
    }
    
    // Implements arithmetic constants (pi, e)
     private abstract class Constant extends Token {
        // constructor
        public Constant(String name) { this.name = name; }

        // evaluate operator; implement at construction
        public abstract double eval();
    }
   
    // add a constant to the list of known constants
    private void registerConstant(Constant constant) {
        constants.put(constant.toString(), constant);
    }

    // private type to turn an expression into a list of tokens
    private class Tokenizer {
        private int pos = 0; // position in expression string
        private String expression; // expression expression
        private String lastToken; // last token

        // Constructor
        private Tokenizer(String expr) {
            expression = expr.trim(); // remove leading and trailing spaces
        }

        // reaturn the next token
        private String getNextToken() {
            StringBuilder token = new StringBuilder();
            if (pos >= expression.length()) return null;

            // read one char at a time
            char ch = expression.charAt(pos);
            
            // ignore whitespaces and jump to next non-whitespace character
            while (Character.isWhitespace(ch)) {
                if (++pos == expression.length()) break;
                ch = expression.charAt(pos);
            }
            
            // start analysing each character
            if (Character.isDigit(ch) || (ch == '.')) {
                // handle numerical values
                while (!Character.isWhitespace(ch)) {
                    if (Character.isDigit(ch)) {
                        token.append(ch);
                        pos++;
                    } else if ((ch == '.') || (ch == 'e') || (ch == 'E')) {
                        token.append(ch); // decimal point or exponent
                        pos++;
                    } else if ((ch == '+') || (ch == '-')) {
                        char lastChar = token.charAt(token.length()-1);
                        if ((lastChar == 'e') || (lastChar == 'E')) {
                            token.append(ch); // sign of exponent
                            pos++;
                        } else {
                            break;
                        }
                    } else {
                        break;
                    }
                    if (pos == expression.length()) break;
                    ch = expression.charAt(pos);
                }
            } else if (((ch == '-') || (ch == '+')) &&
                // check for sign
                    ((lastToken == null) || 
                     (lastToken.equals("(")) ||
                     (operators.containsKey(lastToken)))) {
                token.append(ch); // last token was an operator
                pos++;
                token.append(getNextToken());
            } else if ((ch == '(') || (ch == ')')) {
                // handle brackets
                token.append(ch);
                pos++;
            } else if (operators.containsKey(String.valueOf(ch))) {
                // handle arithmetic operators
                token.append(ch);
                pos++;
            } else if (Character.isLetter(ch)) {
                // handle functions and identifiers
                while (Character.isLetterOrDigit(ch)) {
                    token.append(ch);
                    pos++;
                    if (pos == expression.length()) break;
                    ch = expression.charAt(pos);
                }
            }
            
            return (lastToken = token.toString());
        }
    }

    // Unit test
    public static void main(String[] args) {
        if (args.length != 1 ) {
            System.out.println(
                "Usage: java ArithmeticExpressionEvaluator \"<expression>\"");
                return;
        }

        ArithmeticExpressionEvaluator evaluator =
            new ArithmeticExpressionEvaluator(args[0]);
        System.out.println("* Original: " + evaluator.originalExpression);
        System.out.println("  Infix   : " + evaluator.getInfixString());
        System.out.println("  Postfix : " + evaluator.convInfixToPostfix());
        System.out.println("  Result  : " + evaluator.evalPostfix());
    }

}

