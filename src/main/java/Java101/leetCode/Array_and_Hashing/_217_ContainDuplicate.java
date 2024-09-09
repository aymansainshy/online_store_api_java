package Java101.leetCode.Array_and_Hashing;

import java.util.HashSet;
import java.util.Set;

public class _217_ContainDuplicate {

    public boolean containsDuplicate(int[] nums) {
        Set<Integer> numsSet = new HashSet<>();

        for (int num : nums) {

            if (numsSet.contains(num)) {
                return true;
            } else {
                numsSet.add(num);
            }
        }

        return false;
    }


    public static void main(String[] args) {
        _217_ContainDuplicate containDuplicate = new _217_ContainDuplicate();

        var myNums = new int[]{1, 1, 1, 3, 3, 4, 3, 2, 4, 2};

        boolean result = containDuplicate.containsDuplicate(myNums);

        System.out.println(result);

    }
}

