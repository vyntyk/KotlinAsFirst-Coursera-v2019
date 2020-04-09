@file:Suppress("UNUSED_PARAMETER", "ConvertCallChainIntoSequence")
package lesson7.task1
import java.io.*


/**
 * Пример
 *
 * Во входном файле с именем inputName содержится некоторый текст.
 * Вывести его в выходной файл с именем outputName, выровняв по левому краю,
 * чтобы длина каждой строки не превосходила lineLength.
 * Слова в слишком длинных строках следует переносить на следующую строку.
 * Слишком короткие строки следует дополнять словами из следующей строки.
 * Пустые строки во входном файле обозначают конец абзаца,
 * их следует сохранить и в выходном файле
 */
fun alignFile(inputName: String, lineLength: Int, outputName: String) {
    val outputStream = File(outputName).bufferedWriter()
    var currentLineLength = 0
    for (line in File(inputName).readLines()) {
        if (line.isEmpty()) {
            outputStream.newLine()
            if (currentLineLength > 0) {
                outputStream.newLine()
                currentLineLength = 0
            }
            continue
        }
        for (word in line.split(" ")) {
            if (currentLineLength > 0) {
                if (word.length + currentLineLength >= lineLength) {
                    outputStream.newLine()
                    currentLineLength = 0
                } else {
                    outputStream.write(" ")
                    currentLineLength++
                }
            }
            outputStream.write(word)
            currentLineLength += word.length
        }
    }
    outputStream.close()
}

/**
 * Средняя
 *
 * Во входном файле с именем inputName содержится некоторый текст.
 * На вход подаётся список строк substrings.
 * Вернуть ассоциативный массив с числом вхождений каждой из строк в текст.
 * Регистр букв игнорировать, то есть буквы е и Е считать одинаковыми.
 *
 */
fun countSubstrings(inputName: String, substrings: List<String>): Map<String, Int> {
    val result = mutableMapOf<String, Int>()
    val listOfSubstrings = substrings.toSet().toList()
    val text = File(inputName).readText().toLowerCase()
    for (i in listOfSubstrings.indices) {
        if (!result.contains(listOfSubstrings[i])) {
            result[listOfSubstrings[i]] = 0
        }
        for (j in text.indices) {
            if (text.startsWith(listOfSubstrings[i].toLowerCase(), j)) {
                result[listOfSubstrings[i]] = result[listOfSubstrings[i]]!! + 1
            }
        }
    }
    return result
}

/**
 * Средняя
 *
 * В русском языке, как правило, после букв Ж, Ч, Ш, Щ пишется И, А, У, а не Ы, Я, Ю.
 * Во входном файле с именем inputName содержится некоторый текст на русском языке.
 * Проверить текст во входном файле на соблюдение данного правила и вывести в выходной
 * файл outputName текст с исправленными ошибками.
 *
 * Регистр заменённых букв следует сохранять.
 *
 * Исключения (жюри, брошюра, парашют) в рамках данного задания обрабатывать не нужно
 *
 */
fun sibilants(inputName: String, outputName: String) {
    val dictOfLetters = listOf('ж', 'ч', 'ш', 'щ')
    val dictOfReplaceable = mapOf('ы' to 'и', 'я' to 'а', 'ю' to 'у')
    val outputStream = File(outputName).bufferedWriter()
    for (line in File(inputName).readLines()) {
        var l = ""
        var temp = ""
        for (i in line.indices) {
            if (temp == "") {
                if ((line[i].toLowerCase() in dictOfLetters) && (i != line.length - 1)) {
                    if (line[i + 1] in dictOfReplaceable.keys) {
                        temp = dictOfReplaceable[line[i + 1]].toString()
                    } else if (line[i + 1].toLowerCase() in dictOfReplaceable.keys) {
                        temp = dictOfReplaceable[line[i + 1].toLowerCase()].toString().toUpperCase()
                    }
                }
                l += line[i].toString() + temp
            } else {
                temp = ""
            }
        }
        outputStream.write(l)
        outputStream.newLine()
    }
    outputStream.close()
}

/**
 * Средняя
 *
 * Во входном файле с именем inputName содержится некоторый текст (в том числе, и на русском языке).
 * Вывести его в выходной файл с именем outputName, выровняв по центру
 * относительно самой длинной строки.
 *
 * Выравнивание следует производить путём добавления пробелов в начало строки.
 *
 *
 * Следующие правила должны быть выполнены:
 * 1) Пробелы в начале и в конце всех строк не следует сохранять.
 * 2) В случае невозможности выравнивания строго по центру, строка должна быть сдвинута в ЛЕВУЮ сторону
 * 3) Пустые строки не являются особым случаем, их тоже следует выравнивать
 * 4) Число строк в выходном файле должно быть равно числу строк во входном (в т. ч. пустых)
 *
 */
fun centerFile(inputName: String, outputName: String) {
    val outputStream = File(outputName).bufferedWriter()
    val lines = File(inputName).readLines().map { it.trim() }
    var maxLength = 0
    for (line in lines) {
        if (line.length > maxLength) maxLength = line.length
    }
    for (line in lines) {
        val cent = " ".repeat((maxLength - line.length) / 2)
        outputStream.write(cent + line)
        outputStream.newLine()
    }
    outputStream.close()
}

/**
 * Сложная
 *
 * Во входном файле с именем inputName содержится некоторый текст (в том числе, и на русском языке).
 * Вывести его в выходной файл с именем outputName, выровняв по левому и правому краю относительно
 * самой длинной строки.
 * Выравнивание производить, вставляя дополнительные пробелы между словами: равномерно по всей строке
 *
 * Слова внутри строки отделяются друг от друга одним или более пробелом.
 *
 * Следующие правила должны быть выполнены:
 * 1) Каждая строка входного и выходного файла не должна начинаться или заканчиваться пробелом.
 * 2) Пустые строки или строки из пробелов трансформируются в пустые строки без пробелов.
 * 3) Строки из одного слова выводятся без пробелов.
 * 4) Число строк в выходном файле должно быть равно числу строк во входном (в т. ч. пустых).
 *
 * Равномерность определяется следующими формальными правилами:
 * 5) Число пробелов между каждыми двумя парами соседних слов не должно отличаться более, чем на 1.
 * 6) Число пробелов между более левой парой соседних слов должно быть больше или равно числу пробелов
 *    между более правой парой соседних слов.
 *
 * Следует учесть, что входной файл может содержать последовательности из нескольких пробелов  между слвоами. Такие
 * последовательности следует учитывать при выравнивании и при необходимости избавляться от лишних пробелов.
 * Из этого следуют следующие правила:
 * 7) В самой длинной строке каждая пара соседних слов должна быть отделена В ТОЧНОСТИ одним пробелом
 * 8) Если входной файл удовлетворяет требованиям 1-7, то он должен быть в точности идентичен выходному файлу
 */
fun alignFileByWidth(inputName: String, outputName: String) {
    var max = 0
    for (line in File(inputName).readLines()) {
        val l = line.replace(" +".toRegex(), " ")
        if (l.trim().length > max)
            max = l.trim().length
    }
    val outputStream = File(outputName).bufferedWriter()
    for (line in File(inputName).readLines()) {
        val l = line.replace(" +".toRegex(), " ")
        val parts = Regex("""\s""").split(l.trim()).toMutableList()
        if (parts.size == 1) {
            outputStream.write(l.trim())
            outputStream.newLine()
        } else if (!line.isBlank()) {
            var temp = max - l.trim().length
            var i = 0
            while (temp > 0) {
                parts[i] += " "
                if (i < parts.size - 2)
                    i++
                else
                    i = 0
                temp--
            }
            outputStream.write(parts.joinToString(" "))
            outputStream.newLine()
        } else
            outputStream.newLine()
    }
    outputStream.close()
}

/**
 * Средняя
 *
 * Во входном файле с именем inputName содержится некоторый текст (в том числе, и на русском языке).
 *
 * Вернуть ассоциативный массив, содержащий 20 наиболее часто встречающихся слов с их количеством.
 * Если в тексте менее 20 различных слов, вернуть все слова.
 *
 * Словом считается непрерывная последовательность из букв (кириллических,
 * либо латинских, без знаков препинания и цифр).
 * Цифры, пробелы, знаки препинания считаются разделителями слов:
 * Привет, привет42, привет!!! -привет?!
 * ^ В этой строчке слово привет встречается 4 раза.
 *
 * Регистр букв игнорировать, то есть буквы е и Е считать одинаковыми.
 * Ключи в ассоциативном массиве должны быть в нижнем регистре.
 *
 */

fun top20Words(inputName: String): Map<String, Int> {
    var ans = mutableMapOf<String, Int>()
    for (line in File(inputName).readLines()) {
        val parts = line.split(' ', ',', '.', '!', '?', '—', '0', '1', '2', '3', '4',
            '5', '6', '7', '8', '9', '\'', '(', ')', ':', ';', '"', '-', '«', '»', '[', ']',
            '{', '}', '|', '\\', '/', '_', '=', '+', '@', '#', '&', '%', '&', '*', '^', '№')
        for (part in parts) {
            if (part != "") {
                val word = part.toLowerCase()
                var count = ans[word] ?: 0
                count++
                if (count != 1) ans[word] = count
                else ans[word] = 1
            }
        }
    }
    var list = ans.toList()
    list = list.sortedByDescending { it.second }
    if(list.size > 20) list = list.subList(0, 20)
    return list.associate { Pair(it.first, it.second) }
}

/**
 * Средняя
 *
 * Реализовать транслитерацию текста из входного файла в выходной файл посредством динамически задаваемых правил.

 * Во входном файле с именем inputName содержится некоторый текст (в том числе, и на русском языке).
 *
 * В ассоциативном массиве dictionary содержится словарь, в котором некоторым символам
 * ставится в соответствие строчка из символов, например
 * mapOf('з' to "zz", 'р' to "r", 'д' to "d", 'й' to "y", 'М' to "m", 'и' to "yy", '!' to "!!!")
 *
 * Необходимо вывести в итоговый файл с именем outputName
 * содержимое текста с заменой всех символов из словаря на соответствующие им строки.
 *
 * При этом регистр символов в словаре должен игнорироваться,
 * но при выводе символ в верхнем регистре отображается в строку, начинающуюся с символа в верхнем регистре.
 *
 * Пример.
 * Входной текст: Здравствуй, мир!
 *
 * заменяется на
 *
 * Выходной текст: Zzdrавствуy, mир!!!
 *
 * Пример 2.
 *
 * Входной текст: Здравствуй, мир!
 * Словарь: mapOf('з' to "zZ", 'р' to "r", 'д' to "d", 'й' to "y", 'М' to "m", 'и' to "YY", '!' to "!!!")
 *
 * заменяется на
 *
 * Выходной текст: Zzdrавствуy, mир!!!
 *
 * Обратите внимание: данная функция не имеет возвращаемого значения
 */

fun transliterate(inputName: String, dictionary: Map<Char, String>, outputName: String) {
    TODO()
}

/**
 * Средняя
 *
 * Во входном файле с именем inputName имеется словарь с одним словом в каждой строчке.
 * Выбрать из данного словаря наиболее длинное слово,
 * в котором все буквы разные, например: Неряшливость, Четырёхдюймовка.
 * Вывести его в выходной файл с именем outputName.
 * Если во входном файле имеется несколько слов с одинаковой длиной, в которых все буквы разные,
 * в выходной файл следует вывести их все через запятую.
 * Регистр букв игнорировать, то есть буквы е и Е считать одинаковыми.
 *
 * Пример входного файла:
 * Карминовый
 * Боязливый
 * Некрасивый
 * Остроумный
 * БелогЛазый
 * ФиолетОвый

 * Соответствующий выходной файл:
 * Карминовый, Некрасивый
 *
 * Обратите внимание: данная функция не имеет возвращаемого значения
 */
fun chooseLongestChaoticWord(inputName: String, outputName: String) {
    val maxChaoticWords = StringBuilder()
    var max = 0
    for (line in File(inputName).readLines()) {
        val l = mutableSetOf<Char>()
        for (i in line) {
            l.add(i.toLowerCase())
            if (l.size == line.length && line.length > max) {
                maxChaoticWords.clear().append(line)
                max = line.length
            } else if (l.size == line.length && line.length == max)
                maxChaoticWords.append(", $line")
        }
    }
    val outputStream = File(outputName).bufferedWriter()
    outputStream.write(maxChaoticWords.toString())
    outputStream.close()
}

/**
 * Сложная
 *
 * Реализовать транслитерацию текста в заданном формате разметки в формат разметки HTML.
 *
 * Во входном файле с именем inputName содержится текст, содержащий в себе элементы текстовой разметки следующих типов:
 * - *текст в курсивном начертании* -- курсив
 * - **текст в полужирном начертании** -- полужирный
 * - ~~зачёркнутый текст~~ -- зачёркивание
 *
 * Следует вывести в выходной файл этот же текст в формате HTML:
 * - <i>текст в курсивном начертании</i>
 * - <b>текст в полужирном начертании</b>
 * - <s>зачёркнутый текст</s>
 *
 * Кроме того, все абзацы исходного текста, отделённые друг от друга пустыми строками, следует обернуть в теги <p>...</p>,
 * а весь текст целиком в теги <html><body>...</body></html>.
 *
 * Все остальные части исходного текста должны остаться неизменными с точностью до наборов пробелов и переносов строк.
 * Отдельно следует заметить, что открывающая последовательность из трёх звёздочек (***) должна трактоваться как "<b><i>"
 * и никак иначе.
 *
 * При решении этой и двух следующих задач полезно прочитать статью Википедии "Стек".
 *
 * Пример входного файла:
Lorem ipsum *dolor sit amet*, consectetur **adipiscing** elit.
Vestibulum lobortis, ~~Est vehicula rutrum *suscipit*~~, ipsum ~~lib~~ero *placerat **tortor***,

Suspendisse ~~et elit in enim tempus iaculis~~.
 *
 * Соответствующий выходной файл:
<html>
    <body>
        <p>
            Lorem ipsum <i>dolor sit amet</i>, consectetur <b>adipiscing</b> elit.
            Vestibulum lobortis. <s>Est vehicula rutrum <i>suscipit</i></s>, ipsum <s>lib</s>ero <i>placerat <b>tortor</b></i>.
        </p>
        <p>
            Suspendisse <s>et elit in enim tempus iaculis</s>.
        </p>
    </body>
</html>
 *
 * (Отступы и переносы строк в примере добавлены для наглядности, при решении задачи их реализовывать не обязательно)
 */
fun markdownToHtmlSimple(inputName: String, outputName: String) {
    val tagSign = listOf("*", "**", "~")
    val stack = mutableListOf(" ")
    val usingTags = mutableListOf(false, false, false)
    val outputStream = File(outputName).bufferedWriter()
    val emptyList = mutableListOf<Boolean>()
    outputStream.write("<html><body><p>")
    for (line in File(inputName).readLines())
        if (line.isNotEmpty())
            emptyList.add(false)
        else
            emptyList.add(true)
    emptyList.add(true)
    for ((counter, line) in File(inputName).readLines().withIndex()) {
        if (counter < emptyList.indexOf(false))
            outputStream.write("")
        else if (emptyList[counter] && !emptyList[counter + 1])
            outputStream.write("</p><p>")
        else if (emptyList[counter] && emptyList[counter + 1])
            outputStream.write("")
        else {
            var i = 0
            while (i < line.length) {
                if (i < line.length - 1 && line[i] == line[i + 1] && line[i].toString() == tagSign[0]) {
                    outputStream.write("<")
                    if (stack.last() == tagSign[1]) {
                        outputStream.write("/")
                        stack.remove(stack.last())
                        usingTags[1] = false
                    } else {
                        stack.add(tagSign[1])
                        usingTags[1] = true
                    }
                    outputStream.write("b>")
                    i++
                } else if (line[i].toString() == tagSign[0]) {
                    outputStream.write("<")
                    if (stack.last() == tagSign[0]) {
                        outputStream.write("/")
                        stack.remove(stack.last())
                        usingTags[0] = false
                    } else {
                        stack.add(tagSign[0])
                        usingTags[0] = true
                    }
                    outputStream.write("i>")
                } else if (i < line.length - 1 && line[i] == line[i + 1] && line[i].toString() == tagSign[2]) {
                    outputStream.write("<")
                    if (!usingTags[2])
                        usingTags[2] = true
                    else {
                        outputStream.write("/")
                        usingTags[2] = false
                    }
                    outputStream.write("s>")
                    i++
                } else
                    outputStream.write(line[i].toString())
                i++
            }
        }
    }
    outputStream.write("</p></body></html>")
    outputStream.close()
}

/**
 * Сложная
 *
 * Реализовать транслитерацию текста в заданном формате разметки в формат разметки HTML.
 *
 * Во входном файле с именем inputName содержится текст, содержащий в себе набор вложенных друг в друга списков.
 * Списки бывают двух типов: нумерованные и ненумерованные.
 *
 * Каждый элемент ненумерованного списка начинается с новой строки и символа '*', каждый элемент нумерованного списка --
 * с новой строки, числа и точки. Каждый элемент вложенного списка начинается с отступа из пробелов, на 4 пробела большего,
 * чем список-родитель. Максимально глубина вложенности списков может достигать 6. "Верхние" списки файла начинются
 * прямо с начала строки.
 *
 * Следует вывести этот же текст в выходной файл в формате HTML:
 * Нумерованный список:
 * <ol>
 *     <li>Раз</li>
 *     <li>Два</li>
 *     <li>Три</li>
 * </ol>
 *
 * Ненумерованный список:
 * <ul>
 *     <li>Раз</li>
 *     <li>Два</li>
 *     <li>Три</li>
 * </ul>
 *
 * Кроме того, весь текст целиком следует обернуть в теги <html><body>...</body></html>
 *
 * Все остальные части исходного текста должны остаться неизменными с точностью до наборов пробелов и переносов строк.
 *
 * Пример входного файла:
///////////////////////////////начало файла/////////////////////////////////////////////////////////////////////////////
* Утка по-пекински
    * Утка
    * Соус
* Салат Оливье
    1. Мясо
        * Или колбаса
    2. Майонез
    3. Картофель
    4. Что-то там ещё
* Помидоры
* Фрукты
    1. Бананы
    23. Яблоки
        1. Красные
        2. Зелёные
///////////////////////////////конец файла//////////////////////////////////////////////////////////////////////////////
 *
 *
 * Соответствующий выходной файл:
///////////////////////////////начало файла/////////////////////////////////////////////////////////////////////////////
<html>
  <body>
    <ul>
      <li>
        Утка по-пекински
        <ul>
          <li>Утка</li>
          <li>Соус</li>
        </ul>
      </li>
      <li>
        Салат Оливье
        <ol>
          <li>Мясо
            <ul>
              <li>
                  Или колбаса
              </li>
            </ul>
          </li>
          <li>Майонез</li>
          <li>Картофель</li>
          <li>Что-то там ещё</li>
        </ol>
      </li>
      <li>Помидоры</li>
      <li>
        Фрукты
        <ol>
          <li>Бананы</li>
          <li>
            Яблоки
            <ol>
              <li>Красные</li>
              <li>Зелёные</li>
            </ol>
          </li>
        </ol>
      </li>
    </ul>
  </body>
</html>
///////////////////////////////конец файла//////////////////////////////////////////////////////////////////////////////
 * (Отступы и переносы строк в примере добавлены для наглядности, при решении задачи их реализовывать не обязательно)
 */
fun markdownToHtmlLists(inputName: String, outputName: String) {
    TODO()
}

/**
 * Очень сложная
 *
 * Реализовать преобразования из двух предыдущих задач одновременно над одним и тем же файлом.
 * Следует помнить, что:
 * - Списки, отделённые друг от друга пустой строкой, являются разными и должны оказаться в разных параграфах выходного файла.
 *
 */
fun markdownToHtmlSimpleConstructor(lines: List<String>):String {
    val keys = listOf(
        Triple("**", "<b>", "</b>"),
        Triple("*", "<i>", "</i>"),
        Triple("~~", "<s>", "</s>")
    )
    var text = lines.joinToString(separator = "\n").split("\n\n").joinToString(separator = "</p><p>")
    for (key in keys) {
        val temp = text.split(key.first).toMutableList()
        if (temp.size == 1) continue
        if (temp.size % 2 == 0) {
            temp[temp.size - 2] += key.first + temp[temp.size - 1]
            temp.removeAt(temp.size - 1)
        }
        val sb = StringBuilder()
        var k = true
        for (i in 0..temp.size - 2) {
            if (k) {
                sb.append((listOf(temp[i], temp[i + 1])).joinToString(separator = key.second))
                k = false
            } else {
                sb.append(key.third)
                k = true
            }
        }
        sb.append(temp[temp.size - 1])
        text = sb.toString()
    }
    return text
}
fun markdownToHtmlListsConstructor (lines: List<String>, index: Int): String{
    val sb = StringBuilder()
    if (lines[0][index] == '*') sb.append("<ul>")
    if (lines[0][index] in '1'..'9') sb.append("<ol>")
    var label = true
    for (i in 0..lines.size - 1){
        val temp = lines[i].filter {it !in "123456890. "}
        if (lines[i][index] != ' '){
            label = true
            sb.append("<li>$temp")
            if (i == lines.size - 1 || lines[i + 1][index] != ' ') sb.append("</li>")
        }
        if (lines[i][index] == ' ' && label) {
            val list = mutableListOf(lines[i])
            var k = i + 1
            while (k <= lines.size - 1 && lines[k][index] == ' ') {
                list.add(lines[k])
                k++
            }
            label = false
            sb.append(markdownToHtmlListsConstructor(list, index + 4))
            sb.append("</li>")
        }
    }
    if (lines[0][index] == '*') sb.append("</ul>")
    if (lines[0][index] in '1'..'9') sb.append("</ol>")
    return sb.toString().split("<li>*").joinToString(separator = "<li>")
}

fun markdownToHtml(inputName: String, outputName: String) {
    val lines = File(inputName).readLines()
    val outputStream = File(outputName).bufferedWriter()
    outputStream.write("<html><body>")
    var labelLists = true
    var labelSimple = true
    for (i in 0..lines.size - 1){
        when{
            lines[i].isEmpty() && i != lines.size - 1-> {
                outputStream.newLine()
                labelLists = true
                labelSimple = true
            }
            lines[i].trim()[0] != '*' && lines[i].trim()[0] !in '1'..'9' && labelSimple-> {
                labelSimple = false
                labelLists = true
                val list = mutableListOf(lines[i])
                var k = i + 1
                while (k <= lines.size - 1 && lines[k].isNotEmpty() &&
                    (lines[k][0] != '*' && lines[k][0] !in '1'..'9')) {
                    list.add(lines[k])
                    k++
                }
                outputStream.write("<p>")
                outputStream.write(markdownToHtmlSimpleConstructor(list))
                outputStream.write("</p>")
            }
            (lines[i][0] == '*' || lines[i][0] in '1'..'9') && labelLists -> {
                labelLists = false
                labelSimple = true
                val list = mutableListOf(lines[i])
                var k = i + 1
                while (k <= lines.size - 1 && lines[k].isNotEmpty() && (lines[k][0] in "* " || lines[k][0] in '0'..'9')) {
                    list.add(lines[k])
                    k++
                }
                outputStream.write(markdownToHtmlListsConstructor(list, 0))
                if (k != lines.size - 1) outputStream.newLine()
            }
        }
    }
    outputStream.write("</body></html>")
    outputStream.close()
}

/**
 * Средняя
 *
 * Вывести в выходной файл процесс умножения столбиком числа lhv (> 0) на число rhv (> 0).
 *
 * Пример (для lhv == 19935, rhv == 111):
   19935
*    111
--------
   19935
+ 19935
+19935
--------
 2212785
 * Используемые пробелы, отступы и дефисы должны в точности соответствовать примеру.
 * Нули в множителе обрабатывать так же, как и остальные цифры:
  235
*  10
-----
    0
+235
-----
 2350
 *
 */
fun printMultiplicationProcess(lhv: Int, rhv: Int, outputName: String) {
    val output = File(outputName).bufferedWriter()
    val list = mutableListOf<Int>()
    val space=(lhv * rhv).toString().length+1
    var rhv1 = rhv

    while (rhv1 > 0) {
        list.add(rhv1 % 10)
        rhv1 /= 10
    }
    for (i in 0 until space - lhv.toString().length) {
        output.write(" ")
    }
    output.write(lhv.toString())
    output.newLine()
    output.write("*")
    for (i in 0 until space - rhv.toString().length - 1) {
        output.write(" ")
    }
    output.write(rhv.toString())
    output.newLine()

    for (i in 0 until space) {
        output.write("-")
    }
    output.newLine()
    var k=1      //для добавления пробела когда первое число под черточкой выводится без '+'
    for (j in 0 until rhv.toString().length) {
        val number = list[j] * lhv
        if (j != 0) {
            output.write("+")
        }
        for (i in 0 until space - number.toString().length - 1 - j + k) {
            output.write(" ")
        }
        output.write(number.toString())
        output.newLine()
        k = 0
    }
    for (i in 0 until space) {
        output.write("-")
    }
    output.newLine()
    val numb = (lhv * rhv).toString()
    for (i in 0 until space - numb.length) {
        output.write(" ")
    }
    output.write(numb)
    output.close()
}

/**
 * Сложная
 *
 * Вывести в выходной файл процесс деления столбиком числа lhv (> 0) на число rhv (> 0).
 *
 * Пример (для lhv == 19935, rhv == 22):
  19935 | 22
 -198     906
 ----
    13
    -0
    --
    135
   -132
   ----
      3

 * Используемые пробелы, отступы и дефисы должны в точности соответствовать примеру.
 *
 */
fun printDivisionProcess(lhv: Int, rhv: Int, outputName: String) {
    if (rhv > lhv) {
        divisionIfRhvMoreLhv(lhv, rhv, outputName)
        return
    }
    val (answer, reminder) = Pair(lhv / rhv, lhv % rhv)
    val listOfDigitsLhv = listOfDigits(lhv)
    val listOfMultiple = listOfDigitsAnswer(rhv, answer)
    if (listOfMultiple.size == 1) {
        ifSizeOne(lhv, rhv, outputName, listOfMultiple.first(), answer, reminder)
        return
    }
    val pairOfIntermediate = listOfIntermediate(listOfDigitsLhv, listOfMultiple)
    val listOfIntermediate = pairOfIntermediate.first
    val listOfIntermediateRes = pairOfIntermediate.second
    val first = " $lhv | $rhv"
    var second = "-" + listOfMultiple.first().toString()
    for (i in 1..first.length - second.length - "$rhv".length) {
        second += ' '
    }
    second += answer
    val outputStream = File(outputName).bufferedWriter()
    outputStream.write(first)
    outputStream.newLine()
    outputStream.write(second)
    outputStream.newLine()
    for (i in 0..listOfMultiple.first().toString().length) {
        outputStream.write("-")
    }
    outputStream.newLine()
    var spacesLen = listOfMultiple.first().toString().length - listOfIntermediateRes[0].toString().length + 1
    var dashLen: Int
    var indexOfMul = 0
    //0 - длина результата меньше промежуточного вычитания
    //1 - длина результата больше промежуточного вычитания
    //2 - длина равна
    var flagOfDash: Int
    for ((ind, el) in listOfIntermediate.withIndex()) {
        for (i in 0 until spacesLen) outputStream.write(" ")
        outputStream.write(el)
        outputStream.newLine()
        when {
            el.length < listOfMultiple[ind + 1].toString().length + 1 -> {
                for (i in 0 until spacesLen - 1) outputStream.write(" ")
                flagOfDash = 0
                dashLen = listOfMultiple[ind + 1].toString().length + 1
            }
            el.length > listOfMultiple[ind + 1].toString().length + 1 -> {
                val res = el.length - (listOfMultiple[ind + 1].toString().length + 1)
                for (i in 0 until spacesLen + res) outputStream.write(" ")
                flagOfDash = 1
                dashLen = el.length
            }
            else -> {
                for (i in 0 until spacesLen) outputStream.write(" ")
                dashLen = el.length
                flagOfDash = 2
            }
        }
        outputStream.write("-${listOfMultiple[ind + 1]}")
        outputStream.newLine()
        if (flagOfDash == 0) {
            for (i in 0 until spacesLen - 1) outputStream.write(" ")
            for (i in 0 until dashLen) outputStream.write("-")
            outputStream.newLine()
        } else if (flagOfDash == 0) {
            for (i in 0 until spacesLen - 1) outputStream.write(" ")
            for (i in 0 until dashLen) outputStream.write("-")
            outputStream.newLine()
        } else {
            for (i in 0 until spacesLen) outputStream.write(" ")
            for (i in 0 until dashLen) outputStream.write("-")
            outputStream.newLine()
        }
        if (ind < listOfIntermediateRes.lastIndex) {
            if (el.length > listOfIntermediateRes[ind + 1].toString().length)
                spacesLen += el.length - listOfIntermediateRes[ind + 1].toString().length
        }
        indexOfMul++
    }
    for (i in 0..lhv.toString().length - reminder.toString().length) {
        outputStream.write(" ")
    }
    outputStream.write("$reminder")
    outputStream.close()
}

fun listOfDigits(lhv: Int): List<Int> {
    val listOfDecimals = mutableListOf<Int>()
    var number = lhv
    while (number != 0) {
        listOfDecimals.add(number % 10)
        number /= 10
    }
    return listOfDecimals.reversed()
}

fun listOfDigitsAnswer(rhv: Int, answer: Int): List<Int> {
    val listOfDigitsAnswer = listOfDigits(answer)
    val listOfMultiple = mutableListOf<Int>()
    for (element in listOfDigitsAnswer) {
        listOfMultiple.add(element * rhv)
    }
    return listOfMultiple
}

fun listOfIntermediate(list: List<Int>, listOfMultiple: List<Int>): Pair<List<String>, List<Int>> {
    val k = mutableListOf<String>()
    val listOfIntermediateRes = mutableListOf<Int>()
    var index = 0
    var result = list[0]
    for (el in listOfMultiple) {
        var number = result
        while (number < el) {
            index++
            number = number * 10 + list[index]
        }
        if (index + 1 > list.lastIndex) break
        listOfIntermediateRes.add(number - el)
        result = (number - el) * 10 + list[++index]
        if (listOfIntermediateRes.last() == 0)
            k.add("0$result")
        else k.add("$result")
    }
    return Pair(k, listOfIntermediateRes)
}

fun divisionIfRhvMoreLhv(lhv: Int, rhv: Int, outputName: String) {
    val outputStream = File(outputName).bufferedWriter()
    var second = ""
    for (i in 0 until "$lhv".length - 1) {
        second += ' '
    }
    val first = " $lhv | $rhv"
    second += "-0"
    for (i in 0 until 3) {
        second += ' '
    }
    second += "0"
    outputStream.write(first)
    outputStream.newLine()
    outputStream.write(second)
    outputStream.newLine()
    for (i in 0 until lhv.toString().length + 1)
        outputStream.write("-")
    outputStream.newLine()
    outputStream.write(" ")
    outputStream.write(lhv.toString())
    outputStream.close()
}

fun ifSizeOne(lhv: Int, rhv: Int, outputName: String, mul: Int, answer: Int, remainder: Int) {
    val outputStream = File(outputName).bufferedWriter()
    val first = " $lhv | $rhv"
    var second = "-$mul"
    for (i in 1..first.length - second.length - "$rhv".length) {
        second += ' '
    }
    second += "$answer"
    outputStream.write(first)
    outputStream.newLine()
    outputStream.write(second)
    outputStream.newLine()
    for (i in 0 until lhv.toString().length + 1)
        outputStream.write("-")
    outputStream.newLine()
    for (i in 0 until lhv.toString().length - "$remainder".length + 1)
        outputStream.write(" ")
    outputStream.write(remainder.toString())
    outputStream.close()
}

