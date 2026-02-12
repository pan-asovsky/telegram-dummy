package ru.festtur.telegramdummy.reference.dto;

public record AnswerResult(String v) {

    public static AnswerResult error(String err) {
        return new AnswerResult(err);
    }

    public static AnswerResult completed() {
        return new AnswerResult(":)");
    }

    public static AnswerResult nextQuestion(String text) {
        return new AnswerResult(text);
    }

}
