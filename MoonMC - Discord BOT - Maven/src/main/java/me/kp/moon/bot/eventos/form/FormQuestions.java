package me.kp.moon.bot.eventos.form;

public enum FormQuestions {
    _1(0, "Qual é o seu nick in game?"),
    _2(1, "Qual é o seu nome?"),
    _3(2, "Quantos anos você tem?"),
    _4(3, "Você consegue gravar sua tela com boa qualidade?"),
    _5(4, "Você já faz parte de alguma equipe?"),
    _6(5, "Defina responsabilidade e maturidade."),
    _7(6, "Defina ética."),
    _8(7, "Cite qualidades e defeitos seus."),
    _9(8, "Por que deveríamos te escolher?"),
    _10(9, "Qual é a função do cargo TRIAL?"),
    _11(10, "O que você faria caso presenciasse algum membro da equipe abusando?"),
    _12(11, "O que você faria caso algum player ameaçasse o servidor?"),
    _13(12, "Qual é a sua meta no MoonMC?"),
    _14(13, "Quanto tempo em média você tem para moderar o servidor?"),
    _15(14, "Cite 5 clients de hacks mais utilizados."),
    _16(15, "Qual a diferença entre Kill-Aura e ForceField?"),
    _17(16, "Qual a função do hack AimAssist?"),
    _18(17, "Qual a função do hack Reach?"),
    _19(18, "Qual a função do hack BunnyHop?"),
    _20(19, "Qual a função do hack AutoSoup?"),
    _21(20, "Qual a função do hack Phase?"),
    _22(21, "Qual a função do hack FreeCam?"),
    _23(22, "Qual a função do hack Velocity?"),
    _24(23, "Qual a função do hack Flight?"),
    _25(24, "Qual a função do hack AutoClicker?"),
    ;

    private final int number;
    private final String question;
    FormQuestions(int number, String question) {
        this.number = number;
        this.question = question;
    }

    public int getNumber() {
        return number;
    }

    public String getQuestion() {
        return question;
    }

    public static String getQuestionText(int number) {
        for (FormQuestions question : FormQuestions.values()) {
            if (question.getNumber() == number)
                return question.getQuestion();
        }
        return "?";
    }

}
