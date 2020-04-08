@file:Suppress("UNUSED_PARAMETER", "ConvertCallChainIntoSequence")
package lesson6.task1
import lesson2.task2.daysInMonth
/**
 * Пример
 *
 * Время представлено строкой вида "11:34:45", содержащей часы, минуты и секунды, разделённые двоеточием.
 * Разобрать эту строку и рассчитать количество секунд, прошедшее с начала дня.
 */
fun timeStrToSeconds(str: String): Int {
    val parts = str.split(":")
    var result = 0
    for (part in parts) {
        val number = part.toInt()
        result = result * 60 + number
    }
    return result
}
/**
 * Пример
 *
 * Дано число n от 0 до 99.
 * Вернуть его же в виде двухсимвольной строки, от "00" до "99"
 */
fun twoDigitStr(n: Int) = if (n in 0..9) "0$n" else "$n"

/**
 * Пример
 *
 * Дано seconds -- время в секундах, прошедшее с начала дня.
 * Вернуть текущее время в виде строки в формате "ЧЧ:ММ:СС".
 */
fun timeSecondsToStr(seconds: Int): String {
    val hour = seconds / 3600
    val minute = (seconds % 3600) / 60
    val second = seconds % 60
    return String.format("%02d:%02d:%02d", hour, minute, second)
}
/**
 * Пример: консольный ввод
 */
fun main() {
    println("Введите время в формате ЧЧ:ММ:СС")
    val line = readLine()
    if (line != null) {
        val seconds = timeStrToSeconds(line)
        if (seconds == -1) {
            println("Введённая строка $line не соответствует формату ЧЧ:ММ:СС")
        } else {
            println("Прошло секунд с начала суток: $seconds")
        }
    } else {
        println("Достигнут <конец файла> в процессе чтения строки. Программа прервана")
    }
}
/**
 * Средняя
 *
 * Дата представлена строкой вида "15 июля 2016".
 * Перевести её в цифровой формат "15.07.2016".
 * День и месяц всегда представлять двумя цифрами, например: 03.04.2011.
 * При неверном формате входной строки вернуть пустую строку.
 *
 * Обратите внимание: некорректная с точки зрения календаря дата (например, 30.02.2009) считается неверными
 * входными данными.
 */
fun universalDateFunction(date: String, trend: Boolean): MutableList<String>? {
    val dict = listOf(
        "января", "февраля", "марта", "апреля", "мая",
        "июня", "июля", "августа", "сентября", "октября", "ноября", "декабря"
    )
    val dateOnList = mutableListOf<String>()
    val parts = if (trend) {
        date.split(" ")
    } else date.split(".")
    for (part in parts)
        dateOnList.add(part)
    if (dateOnList.size != 3 || parts[0].toInt() < 1 || (!trend && (parts[1].toInt() < 1 || parts[1].toInt() > 12)))
        return null
    else if (trend && !dict.contains(dateOnList[1]))
        return null
    else {
        if (trend && dict.contains(dateOnList[1]))
            dateOnList[1] = (dict.indexOf(dateOnList[1]) + 1).toString()
        else if (!trend && dateOnList[1].toInt() in 1..12)
            dateOnList[1] = dict[dateOnList[1].toInt() - 1]
        if (trend && daysInMonth(
                dateOnList[1].toInt(),
                dateOnList[2].toInt()
            ) < dateOnList[0].toInt()
        )
            return null
        if (!trend && daysInMonth(parts[1].toInt(), dateOnList[2].toInt()) < dateOnList[0].toInt())
            return null
    }
    return dateOnList
}

fun dateStrToDigit(str: String): String {
    if (str.matches(Regex("""\d* [а-я]* \d*"""))) {
        val list = universalDateFunction(str, true)
        if (!list.isNullOrEmpty())
            return String.format("%02d.%02d.%01d", list[0].toInt(), list[1].toInt(), list[2].toInt())
    }
    return ""
}

/**
 * Средняя
 *
 * Дата представлена строкой вида "15.07.2016".
 * Перевести её в строковый формат вида "15 июля 2016".
 * При неверном формате входной строки вернуть пустую строку
 *
 * Обратите внимание: некорректная с точки зрения календаря дата (например, 30 февраля 2009) считается неверными
 * входными данными.
 */
fun dateDigitToStr(digital: String): String {
    if (digital.matches(Regex("""\d+\.\d+\.\d+"""))) {
        val list = universalDateFunction(digital, false)
        if (!list.isNullOrEmpty())
            return String.format("%d %s %d", list[0].toInt(), list[1], list[2].toInt())
    }
    return ""
}

/**
 * Средняя
 *
 * Номер телефона задан строкой вида "+7 (921) 123-45-67".
 * Префикс (+7) может отсутствовать, код города (в скобках) также может отсутствовать.
 * Может присутствовать неограниченное количество пробелов и чёрточек,
 * например, номер 12 --  34- 5 -- 67 -89 тоже следует считать легальным.
 * Перевести номер в формат без скобок, пробелов и чёрточек (но с +), например,
 * "+79211234567" или "123456789" для приведённых примеров.
 * Все символы в номере, кроме цифр, пробелов и +-(), считать недопустимыми.
 * При неверном формате вернуть пустую строку.
 *
 * PS: Дополнительные примеры работы функции можно посмотреть в соответствующих тестах.
 */
fun flattenPhoneNumber(phone: String) =
    if (!phone.contains(Regex("""\( *\)""")) &&
        !phone.matches(Regex("""\+ ?\d""")) &&
        phone.matches(Regex("""(\+? *\d[- \d]*(\([-\d ]+\)[-\d ]+)?)""")))
        phone.filter { it !in " " && it !in "(" && it !in ")" && it !in "-" }
    else ""

/**
 * Средняя
 *
 * Результаты спортсмена на соревнованиях в прыжках в длину представлены строкой вида
 * "706 - % 717 % 703".
 * В строке могут присутствовать числа, черточки - и знаки процента %, разделённые пробелами;
 * число соответствует удачному прыжку, - пропущенной попытке, % заступу.
 * Прочитать строку и вернуть максимальное присутствующее в ней число (717 в примере).
 * При нарушении формата входной строки или при отсутствии в ней чисел, вернуть -1.
 */
fun bestLongJump(jumps: String): Int {
    val jList = jumps.split(Regex(" +"))
    val checkR = Regex("""^\d+|%|-$""")
    var max = -1
    for (el in jList) {
        if (!el.matches(checkR))
            return -1

        if (el.matches(Regex("""^\d+$"""))) {
            val jump = el.toInt()
            if (jump > max)
                max = jump
        }
    }
    return max
}

/**
 * Сложная
 *
 * Результаты спортсмена на соревнованиях в прыжках в высоту представлены строкой вида
 * "220 + 224 %+ 228 %- 230 + 232 %%- 234 %".
 * Здесь + соответствует удачной попытке, % неудачной, - пропущенной.
 * Высота и соответствующие ей попытки разделяются пробелом.
 * Прочитать строку и вернуть максимальную взятую высоту (230 в примере).
 * При нарушении формата входной строки, а также в случае отсутствия удачных попыток,
 * вернуть -1.
 */
fun bestHighJump(jumps: String): Int {
    val jList = jumps.split(Regex(" +"))
    val checkR = Regex("^\\d+[+\\-%]+$")
    var max = -1
    for (i in 0 until jList.lastIndex step 2) {
        val attempts = Pair(jList[i], jList[i + 1])
        if (!(attempts.first + attempts.second).matches(checkR))
            return -1
        if (attempts.second.indexOf('+') != -1)
            max = Math.max(max, attempts.first.toInt())
    }
    return max
}

/**
 * Сложная
 *
 * В строке представлено выражение вида "2 + 31 - 40 + 13",
 * использующее целые положительные числа, плюсы и минусы, разделённые пробелами.
 * Наличие двух знаков подряд "13 + + 10" или двух чисел подряд "1 2" не допускается.
 * Вернуть значение выражения (6 для примера).
 * Про нарушении формата входной строки бросить исключение IllegalArgumentException
 */
fun plusMinus(expression: String): Int {
    if (!expression.matches(Regex("""^\d+( +[+\-] +(\d+))*$""")))
        throw IllegalArgumentException("Wrong format")
    val plus = Regex("""(^\d+)|(\+ +\d+)""").findAll(expression)
    val minus = Regex("""- +\d+""").findAll(expression)
    var result = 0

    for (el in plus) {
        val tmp = Regex("""\d+""").find(el.value)
        if (tmp != null)
            result += tmp.value.toInt()
        else throw IllegalArgumentException("")
    }
    for (el in minus) {
        val tmp = Regex("""\d+""").find(el.value)
        if (tmp != null)
            result -= tmp.value.toInt()
        else throw IllegalArgumentException("")
    }
    return result
}

/**
 * Сложная
 *
 * Строка состоит из набора слов, отделённых друг от друга одним пробелом.
 * Определить, имеются ли в строке повторяющиеся слова, идущие друг за другом.
 * Слова, отличающиеся только регистром, считать совпадающими.
 * Вернуть индекс начала первого повторяющегося слова, или -1, если повторов нет.
 * Пример: "Он пошёл в в школу" => результат 9 (индекс первого 'в')
 */
fun firstDuplicateIndex(str: String): Int {
    val list = str.toLowerCase().split(' ')
    var index = 0
    for (i in 0 until list.count() - 1) {
        if (list[i] == list[i + 1]) {
            return index
        } else index += list[i].count() + 1
    }
    return -1
}

/**
 * Сложная
 *
 * Строка содержит названия товаров и цены на них в формате вида
 * "Хлеб 39.9; Молоко 62; Курица 184.0; Конфеты 89.9".
 * То есть, название товара отделено от цены пробелом,
 * а цена отделена от названия следующего товара точкой с запятой и пробелом.
 * Вернуть название самого дорогого товара в списке (в примере это Курица),
 * или пустую строку при нарушении формата строки.
 * Все цены должны быть больше либо равны нуля.
 */
fun mostExpensive(description: String): String {
    if (description.isEmpty()) return ""
    val divider = description.split(";")
    var max = ""
    var maxcost = 0.0
    for (i in divider) {
        val product = i.trim().split(" ")
        try {
            if (maxcost <= product[1].toDouble()) {
                maxcost = product[1].toDouble()
                max = product[0]
            }
        } catch (e: NumberFormatException) {
            return ""
        }
    }
    return max
}

/**
 * Сложная
 *
 * Перевести число roman, заданное в римской системе счисления,
 * в десятичную систему и вернуть как результат.
 * Римские цифры: 1 = I, 4 = IV, 5 = V, 9 = IX, 10 = X, 40 = XL, 50 = L,
 * 90 = XC, 100 = C, 400 = CD, 500 = D, 900 = CM, 1000 = M.
 * Например: XXIII = 23, XLIV = 44, C = 100
 *
 * Вернуть -1, если roman не является корректным римским числом
 */
fun fromRoman(roman: String): Int {
    val list = mutableListOf<Int>()
    var x = -1
    for (part in roman) {
        when (part) {
            'I' -> list.add(1)
            'V' -> list.add(5)
            'X' -> list.add(10)
            'L' -> list.add(50)
            'C' -> list.add(100)
            'D' -> list.add(500)
            'M' -> list.add(1000)
            else -> return -1
        }
    }
    for (i in 0 until list.size) {
        if (i == 0) x = list[0]
        else {
            if (list[i] <= list[i - 1]) x += list[i]
            else x += list[i] - 2 * list[i - 1]
        }
    }
    return x
}

/**
 * Очень сложная
 *
 * Имеется специальное устройство, представляющее собой
 * конвейер из cells ячеек (нумеруются от 0 до cells - 1 слева направо) и датчик, двигающийся над этим конвейером.
 * Строка commands содержит последовательность команд, выполняемых данным устройством, например +>+>+>+>+
 * Каждая команда кодируется одним специальным символом:
 *	> - сдвиг датчика вправо на 1 ячейку;
 *  < - сдвиг датчика влево на 1 ячейку;
 *	+ - увеличение значения в ячейке под датчиком на 1 ед.;
 *	- - уменьшение значения в ячейке под датчиком на 1 ед.;
 *	[ - если значение под датчиком равно 0, в качестве следующей команды следует воспринимать
 *  	не следующую по порядку, а идущую за соответствующей следующей командой ']' (с учётом вложенности);
 *	] - если значение под датчиком не равно 0, в качестве следующей команды следует воспринимать
 *  	не следующую по порядку, а идущую за соответствующей предыдущей командой '[' (с учётом вложенности);
 *      (комбинация [] имитирует цикл)
 *  пробел - пустая команда
 *
 * Изначально все ячейки заполнены значением 0 и датчик стоит на ячейке с номером N/2 (округлять вниз)
 *
 * После выполнения limit команд или всех команд из commands следует прекратить выполнение последовательности команд.
 * Учитываются все команды, в том числе несостоявшиеся переходы ("[" при значении под датчиком не равном 0 и "]" при
 * значении под датчиком равном 0) и пробелы.
 *
 * Вернуть список размера cells, содержащий элементы ячеек устройства после завершения выполнения последовательности.
 * Например, для 10 ячеек и командной строки +>+>+>+>+ результат должен быть 0,0,0,0,0,1,1,1,1,1
 *
 * Все прочие символы следует считать ошибочными и формировать исключение IllegalArgumentException.
 * То же исключение формируется, если у символов [ ] не оказывается пары.
 * Выход за границу конвейера также следует считать ошибкой и формировать исключение IllegalStateException.
 * Считать, что ошибочные символы и непарные скобки являются более приоритетной ошибкой чем выход за границу ленты,
 * то есть если в программе присутствует некорректный символ или непарная скобка, то должно быть выброшено
 * IllegalArgumentException.
 * IllegalArgumentException должен бросаться даже если ошибочная команда не была достигнута в ходе выполнения.
 *
 */
fun computeDeviceCells(cells: Int, commands: String, limit: Int): List<Int> {
    val list = mutableListOf<Int>()
    var listFirst = listOf<Int>()
    val listSecond = mutableListOf<Int>()
    for (i in 0 until cells) {
        list.add(0)
    }
    var k = 0
    var j: Int
    var c1 = 0
    var c2 = 0
    for (i in 0 until commands.length) {
        if (commands[i] == '[') {
            k++
            listFirst += i
            j = i + 1
            c1++
            while (k != 0) {
                if (j == commands.length) throw IllegalArgumentException()
                if (commands[j] == ']') {
                    k--
                }
                if (commands[j] == '[') {
                    k++
                }
                j++
            }
            listSecond += j - 1
        }
        if (commands[i] == ']') c2++
        if ((commands[i] != '<') && (commands[i] != '>') &&
            (commands[i] != '+') && (commands[i] != '-') &&
            (commands[i] != '[') && (commands[i] != ']') &&
            (commands[i] != ' ')
        ) {
            throw IllegalArgumentException()
        }
    }
    if ((listFirst.isEmpty() && "]" in commands) || c1 != c2) throw IllegalArgumentException()
    var sensor = cells / 2
    var count = 0
    var i = 0
    var command: Char
    while ((count < limit) && (i < commands.length)) {
        command = commands[i]
        if (command == ' ') {
            i++
        }
        if (command == '>') {
            sensor++
            i++
        }
        if (command == '<') {
            sensor--
            i++
        }
        if (command == '+') {
            list[sensor]++
            i++
        }
        if (command == '-') {
            list[sensor]--
            i++
        }
        if (command == '[') {
            if (list[sensor] == 0) {
                i = listSecond[listFirst.indexOf(i)] + 1
            } else {
                i++
            }
        }
        if (command == ']') {
            if (list[sensor] != 0) {
                i = listFirst[listSecond.indexOf(i)] + 1
            } else {
                i++
            }
        }
        count++
        if (sensor !in 0 until cells) {
            throw IllegalStateException()
        }
    }
    return list
}