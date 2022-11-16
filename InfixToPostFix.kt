package calculator

import java.lang.Exception
import kotlin.math.pow
import java.math.BigInteger

class InfixToPostFix {
    private fun notNumeric(ch: Char): Boolean = when(ch) {
        '+', '-', '*', '/', '(', ')', '^' ->  true
        else -> false
    }

    private fun operatorPrecedence(ch: Char): Int = when(ch) {
        '+', '-' -> 1
        '*', '/' -> 2
        '^' -> 3
        else -> -1
    }

    fun postFixConversion(string: String): String {
        var result = ""
        val operatorStack = ArrayDeque<Char>()

        for (ch in string) {
            if (!notNumeric(ch)) {
                result += ch
            } else if (ch == '(') {
                operatorStack.push(ch)
            } else if (ch == ')') {
                while (!operatorStack.isEmpty() && operatorStack.peek() != '(') {
                    result += " " + operatorStack.pop()
                }
                operatorStack.pop()
            } else {
                while (
                    !operatorStack.isEmpty()
                    && operatorPrecedence(ch) <= operatorPrecedence(operatorStack.peek()!!)
                ){
                    result += " ${operatorStack.pop()} "
                }
                operatorStack.push(ch)
                result += " "

            }
        }
        result += " "

        while (!operatorStack.isEmpty()) {
            if (operatorStack.peek() == '(') return "Invalid expression"
            result += operatorStack.pop()!! + " "
        }
        return result.trim()
    }
}

class Model {
    private fun replaceN(string: String): String {
        val array = StringBuffer(string)

        if (array[0] == '-') {
            array.setCharAt(0, 'n')
        }
        var i = 0
        while (i < array.length) {
            if (array[i] == '-') {
                if (
                    array[i - 1] == '+' ||
                    array[i - 1] == '-' ||
                    array[i - 1] == '/' ||
                    array[i - 1] == '*' ||
                    array[i - 1] == '('

                ){
                    array.setCharAt(i, 'n')
                }
            }
            i++
        }
        return array.toString()
    }

    fun result(string: String): String {
        val stringN = replaceN(string)
        val postFix = InfixToPostFix().postFixConversion(stringN)

        if (postFix == "Invalid expression") {
            return "Invalid expression"
        }
        return try {
            val evaluation = ArithmeticEvaluation().evaluation(postFix)
            evaluation.toString()
        } catch (e: Exception) {
            return "Invalid expression"
        }
    }
}

class ArithmeticEvaluation {
    private fun notOperator(ch: Char): Boolean = when (ch) {
        '+', '-', '*', '/', '(', ')', '^' ->  false
        else -> true
    }

    fun evaluation(string: String): BigInteger {
        var str = ""
        val stack = ArrayDeque<BigInteger>()

        for (ch in string) {
            if (notOperator(ch) && ch != ' ') {
                str += ch
            } else if (ch == ' ' && str != "") {
                stack.push(str.replace('n', '-').toBigInteger())
                str = ""
            } else if (!notOperator(ch)) {
                val val1 = stack.pop()
                val val2 = stack.pop()

                when (ch) {
                    '+' -> stack.push(val2!! + val1!!)
                    '-' -> stack.push(val2!! - val1!!)
                    '*' -> stack.push(val2!! * val1!!)
                    '/' -> stack.push(val2!! / val1!!)
                    '^' -> stack.push(val2!!.pow(val1?.toInt()!!))
                }
            }
        }
        return stack.pop()!!
    }
}


fun <T> ArrayDeque<T>.push(element: T) = addLast(element)
fun <T> ArrayDeque<T>.pop() = removeLastOrNull()
fun <T> ArrayDeque<T>.peek() = lastOrNull()
