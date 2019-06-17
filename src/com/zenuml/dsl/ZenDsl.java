package com.zenuml.dsl;

import org.jetbrains.annotations.NotNull;
import java.util.stream.IntStream;

public class ZenDsl {
    private StringBuffer dsl = new StringBuffer();
    private int level = 0;

    String getIndent() {
        return IntStream.range(0, level).mapToObj(i -> "\t").reduce((s1, s2) -> s1 + s2).orElse("");
    }

    public void setDsl(String dsl) {
        this.dsl = new StringBuffer(dsl);
    }

    public String getDsl() {
        return dsl.toString();
    }

    void addMethodCall(String methodCall) {
        dsl.append(methodCall);
    }

    void addRemainder(String remainder) {
        level--;
        String indent = getIndent();
        level++;
        dsl.append(indent).append(remainder);
    }

    void keepHead(int pos) {
        dsl.replace(0, dsl.length(), dsl.substring(0, pos));
    }

    @NotNull
    ZenDsl append(String s) {
        dsl.append(s);
        return this;
    }

    ZenDsl appendIndent() {
        String indent = getIndent();
        return append(indent);
    }

    private void levelIncrease() {
        level++;
    }

    private void levelDecrease() {
        level--;
    }

    @NotNull
    ZenDsl closeExpressionAndNewLine() {
        dsl.append(";\n");
        return this;
    }

    void appendAssignment(String type, String name) {
        append(type)
        .whiteSpace()
        .append(name)
        .whiteSpace()
        .append("=")
        .whiteSpace();
    }

    void startBlock() {
        whiteSpace()
        .append("{")
        .changeLine();
        levelIncrease();
    }

    private ZenDsl whiteSpace() {
        return append(" ");
    }

    void closeBlock() {
        levelDecrease();
        appendIndent()
        .append("}")
        .changeLine();
    }

    @NotNull
    ZenDsl addIndent() {
        return append(getIndent());
    }

    @NotNull
    ZenDsl openParenthesis() {
        return append("(");
    }

    @NotNull
    ZenDsl closeParenthesis() {
        return append(")");
    }

    ZenDsl changeLine() {
        return append("\n");
    }

    public ZenDsl comment(String text) {
        return append("// ").append(text).changeLine();
    }
}
