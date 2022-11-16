package calculator
import java.math.BigInteger


fun main() {

    var input = readln().replace(" ", "")
    val variables = mutableMapOf<String, BigInteger>()
    while (input != "/exit") {
        if (input.isNotEmpty()) {
            when {
                input == "/help" -> {
                    println("Smart calculator. Now, also accepting variables!")
                    input = readln()
                } //HELP

                input.matches("\\/.+".toRegex()) -> { // Any command that is not /help or /exit
                    println("Unknown command")
                    input = readln()
                } // Any command that is not /help or /exit

                input.matches("\\s*[a-zA-Z]+\\s*\\=\\s*\\-?\\d+\\s*".toRegex()) -> { // Variables in the correct format
                    val variable = input.split("=").toMutableList()
                    variables[variable[0].trim()] = variable[1].trim().toBigInteger()
                    input = readln()
                } // Variables in the correct format

                input.matches(".+\\=\\s*\\-?\\d+\\s*".toRegex()) -> { // a2a = 2
                    println("Invalid identifier")
                    input = readln()
                } // a2a = 2

                input.matches("[a-zA-Z]+\\s*\\=\\s*[a-zA-Z]+\\s*".toRegex()) -> { //  a = b, b exists or not
                    val variable = input.split("=").toMutableList()
                    if (variable[1].trim() in variables) {
                        variables[variable[0].trim()] = variables.get(variable[1].trim())!!
                    } else {
                        println("Unknown variable")
                    }
                    input = readln()
                } //  a = b, b exists or not

                input.matches("[a-zA-Z]+\\s*\\=.+".toRegex()) -> { //a = a2a
                    println("Invalid assignment")
                    input = readln()
                } // invalid assignment

                input in variables -> {
                    println(variables[input])
                    input = readln()
                } //variable exists, rewrite

                input.matches("[a-zA-Z]+\\s*".toRegex()) && input !in variables -> {
                    println("Unknown variable")
                    input = readln()

                }  //unknown variable

                input.replace(" ", "") in variables -> {
                    val variable = input.replace(" ", "")
                    println(variables[variable])
                    input = readln()
                }

                input.contains(")") && !input.contains("(") -> {
                    println("Invalid expression")
                    input = readln()
                }

                else -> {
                    var replaceMultiples = input
                    while (replaceMultiples.contains("--")) replaceMultiples = replaceMultiples.replace("--", "+")
                    while (replaceMultiples.contains("++")) replaceMultiples = replaceMultiples.replace("++", "+")

                    val line = replaceMultiples.replace("(?<=\\w*)(?=[^\\w\\s])|(?<=[^\\w\\s])(?=\\w)".toRegex(), "$0 ").split(" ").toMutableList()

                    for (index in line.indices) {
                        if (line[index] in variables) {
                            line[index] =
                                variables[line[index]].toString() //replace all variables to its value before add
                        }
                    }

                    val lineString = line.joinToString(separator = "")
                    val result = Model().result(lineString)
                    println(result)
                    input = readln()
                }
            }
        } else input = readln()
    }
    println("Bye!")
}

