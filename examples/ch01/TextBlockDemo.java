public class TextBlockDemo {
    public static void main(String[] args) {
        var text = """
            "Java" \
            21\s
            """;

        System.out.println("Uzunluq: " + text.length()); // 11
        System.out.print(text);
    }
}
