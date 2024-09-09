package Java101.leetCode.Stack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.Stack;

public class _20_Valid_Parentheses {

    public boolean isValidParentheses(String s) {
        if (s.length() % 2 != 0) {
            return false;
        }

        HashMap<Character, Character> closeToOpen = new HashMap<>() {{
            put(')', '(');
            put(']', '[');
            put('}', '{');
        }};

        Stack<Character> stack = new Stack<>();

        for (int i = 0; i < s.length(); i++) {
            char key = s.charAt(i);

            if (closeToOpen.containsKey(key)) {
                if (Objects.equals(closeToOpen.get(key), stack.peek())) {
                    stack.pop();
                } else {
                    return false;
                }
            } else {
                stack.push(key);
            }
        }

        return stack.isEmpty();
    }


    public static void main(String[] args) {

        _20_Valid_Parentheses validParenthesesClass = new _20_Valid_Parentheses();
        String ss = "({)";

        boolean result = validParenthesesClass.isValidParentheses(ss);
        System.out.println(result);

    }
}
