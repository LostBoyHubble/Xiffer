package org.lostboyhubble.xiffer.parsers;

import org.lostboyhubble.xiffer.ParserResult;
import org.lostboyhubble.xiffer.troubleshooting.InvalidXifferStatementException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class XiffParser
{
    public XiffParser() {}

    private ParserResult y = new ParserResult();

    public ParserResult parse(File x)
    {
        try
        {
            ParserResult y = new ParserResult();
            processParse(x);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return y;
    }

    private void processParse(File x) throws Exception
    {
        if (!x.getPath().endsWith(".xff")) System.err.println("[DECLARATION CONCERN] The file parsed was not declared as .xff file.\n[DECLARATION CONCERN] Trying to parse...");

        FileReader fileReader = new FileReader(x);
        BufferedReader reader = new BufferedReader(fileReader);

        String current = "";

        while ((current = reader.readLine()) != null)
        {
            if (current.startsWith("#")) this.declareComment(current);
            else if (current.startsWith("*")) this.declareElementStart(current);
            else this.declareContentStatement(current);
        }

        reader.close();
        fileReader.close();
    }

    private String currentElement = "";
    private int currentStage = 0;

    private final String locator = "    ";

    private void declareElementStart(String s) {
        currentElement = "";
        currentStage = 0;

        if (this.isSingleLineStatement(s)) {
            String raw = s.replaceAll(" ", "");
            String[] split = raw.split("=");

            y.content.put(this.removeCastDeclaration(split[0].replaceAll("\\*", "")), this.cast(split[0], split[1]));
        } else {
            String raw = s.replaceAll(" ", "");
            String[] split = raw.split(":");

            currentElement += split[0].replaceAll("\\*", "");
            currentStage++;
        }
    }

    private void declareComment(String s)
    {
        y.comments.add(s.replaceFirst("# ", ""));
    }

    private void declareContentStatement(String s)
    {
        if (s.isEmpty()) return;

        String raw = s.replaceAll(locator, "!");

        int indentationStage = 0;
        char current;

        while ((current = raw.toCharArray()[indentationStage]) == '!')
        {
            indentationStage ++;
        }

        if (indentationStage > this.currentStage) throw new IllegalStateException();
        if (indentationStage < this.currentStage) this.declareEndOfContentObject(s);

        raw = raw.replaceAll("!", "");

        if (this.isSingleLineStatement(raw))
        {
            String[] split = raw.split("=");
            String raw2 = split[0].replaceAll(" ", "");

            y.content.put(this.currentElement + "/" + this.removeCastDeclaration(split[0]), this.cast(split[0], split[1]));
        }
        else
        {
            String raw2 = raw.replaceAll(" ", "");
            String[] split = raw2.split(":");

            currentElement += "/" + split[0];
            currentStage ++;
        }
    }

    private void declareEndOfContentObject(String s)
    {
        String[] x = this.currentElement.split("/");
        this.currentElement = this.currentElement.replace("/" + this.currentElement.split("/")[x.length - 1], "");
        this.currentStage --;
    }

    private Boolean isSingleLineStatement(String s)
    {
        char[] chars = s.toCharArray();

        String first = "";

        for (char aChar : chars)
        {
            if (aChar == ':') return false;
            if (aChar == '=') return true;
        }

        throw new InvalidXifferStatementException();
    }

    private Object cast(String dec, String con)
    {
        if (!dec.contains("\\$")) return con;
        String[] split = dec.split("\\$");

        switch (split[0].toUpperCase())
        {
            case "INT" -> { con = con.replaceAll(" ", ""); return Integer.parseInt(con); }
            case "LONG" -> { con = con.replaceAll(" ", ""); return Long.parseLong(con); }
            case "SHORT" -> { con = con.replaceAll(" ", ""); return Short.parseShort(con); }
            case "FLOAT" -> { con = con.replaceAll(" ", ""); return Float.parseFloat(con); }
            case "BOOLEAN" -> { con = con.replaceAll(" ", ""); return Boolean.parseBoolean(con); }
            case "DOUBLE" -> { con = con.replaceAll(" ", ""); return Double.parseDouble(con); }
            case "" -> { return con; }
        }

        return null;
    }

    private String removeCastDeclaration(String s)
    {
        String[] split = s.split("\\$");
        if (split.length == 1) return s;
        return split[1];
    }
}
