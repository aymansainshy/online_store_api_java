package Java101.leetCode.Sliding_window;

import java.util.HashSet;
import java.util.Set;

public class _3_LongestSubstringWithoutRepeatingCharacters {

    public int lengthOfLongestSubstring(String s) {
        Set<Character> visitedChars = new HashSet<>();

        int result = 0;
        int left = 0;

        for (int i = 0; i < s.length(); i++) {

            char c = s.charAt(i);

            while (visitedChars.contains(c)) {
                visitedChars.remove(s.charAt(left));
                left += 1;
            }

            visitedChars.add(c);
            result = Math.max(result, visitedChars.size());
        }

        return result;
    }


    public static void main(String[] args) {

        String s ="bbbbb";

        _3_LongestSubstringWithoutRepeatingCharacters longest = new _3_LongestSubstringWithoutRepeatingCharacters();
        int result = longest.lengthOfLongestSubstring(s);
        System.out.println(result);

    }
}
