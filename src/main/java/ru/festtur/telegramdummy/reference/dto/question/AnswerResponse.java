package ru.festtur.telegramdummy.reference.dto.question;

public record AnswerResponse(String text) {

    public static AnswerResponse error(String err) {
        return new AnswerResponse(err);
    }

    public static AnswerResponse completed() {
        return new AnswerResponse(":)");
    }

    public static AnswerResponse nextQuestion(String text) {
        return new AnswerResponse(text);
    }

}
