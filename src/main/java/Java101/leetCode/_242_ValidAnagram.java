package Java101.leetCode;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class _242_ValidAnagram {
    public boolean isAnagram(String s, String t) {
        if (s.length() != t.length()) return false;

        Map<Character, Integer> sCount = charCount(s);
        Map<Character, Integer> tCount = charCount(t);

        for (Character key : tCount.keySet()) {
            if (!sCount.containsKey(key)) {
                return false;
            } else if (!Objects.equals(sCount.get(key), tCount.get(key))) {
                return false;
            }
        }


        return true;
    }

    public Map<Character, Integer> charCount(String s) {
        Map<Character, Integer> count = new HashMap<>();

        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);

            if (count.containsKey(c)) {
                count.put(c, count.get(c) + 1);
            } else {
                count.put(c, 1);
            }
        }

        return count;
    }

    public static void main(String[] args) {


//        Input: s = "anagram", t = "nagaram"
//        Output: true

        _242_ValidAnagram validAnagram = new _242_ValidAnagram();
        String s = "anagram";
        String t = "nagaram";

        boolean result = validAnagram.isAnagram(s, t);

        System.out.println(result);
    }
}
