package lab_4;

import java.util.ArrayList;

/**
 * Парсер csv файлов.
 *
 * первая строка - заголовок;
 *
 * разделители полей — запятые,
 * в конце строки разделителя нет;
 *
 * пробелы, окружающие запятую-разделитель, игнорируются;
 *
 * если значение содержит в себе
 * двойную кавычку или запятую, то заключение
 * значения в кавычки обязательно. В противном случае — допустимо;
 *
 * если внутри закавыченной части встречаются двойные кавычки, то используется
 * специфический квотинг кавычек в CSV — их дублирование.
 */


public class LameCSVParser {

    public static ArrayList<String> parseItPlease(String line){

        /*
        * The header:
        * MODEL, YEAR, EFFICIENCY, SPEED, MAXWEIGHT, FANS
        * */

        ArrayList<String> parsed = new ArrayList<>();

        StringBuffer curVal = new StringBuffer();
        boolean inQuotes = false;
        boolean simple = false;
        boolean CollectingChars = false;
        boolean doubleQuotes = false;
        boolean firstDoubleQuote = false;

        char[] c = line.toCharArray();

        for (int i = 0; i < c.length; i++){
            if (inQuotes){
                if (c[i] == '"'){
                    if (doubleQuotes){
                        if(firstDoubleQuote){
                            curVal.append(c[i]);
                            firstDoubleQuote = false;
                            i++;
                        }
                        else firstDoubleQuote = true;
                    }
                    else{
                        if (i < c.length - 1  && c[i + 1] == ',' || i == c.length - 1){
                            inQuotes = false;
                            parsed.add(curVal.toString());
                            curVal = new StringBuffer();
                        }
                        else{
                            doubleQuotes = true;
                        }
                    }
                }
                else curVal.append(c[i]);
            }
            else if (simple){
                if (i == c.length - 1){curVal.append(c[i]); parsed.add(curVal.toString());}
                if (c[i] == ',' || c[i] == '\n'){
                    simple = false;
                    parsed.add(curVal.toString());
                    curVal = new StringBuffer();
                }
                else curVal.append(c[i]);
            }
            else{
                if (c[i] == ' ' || c[i] == ',') continue;
                if (c[i] == '"') {inQuotes = true; CollectingChars = true; }
                else { simple = true; CollectingChars = true; curVal.append(c[i]);}
            }
        }
        return parsed;
    }
}
