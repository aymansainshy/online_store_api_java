package Java101.leetCode.Stack;

import java.util.*;

public class _853_CarFleet {

    public int carFleet(int target, Integer[] positions, Integer[] speeds) {
        int[][] pair = new int[positions.length][2];

        for (int i = 0; i < positions.length; i++) {
            pair[i][0] = positions[i];
            pair[i][1] = speeds[i];
        }
        // This compare by B in descending order
        Arrays.sort(pair, (a, b) -> Integer.compare(b[0], a[0]));
        System.out.println("Pairs _--_-----__-____-___-----" + Arrays.deepToString(pair));

        Stack<Double> stack = new Stack<>();

        for(int[] p: pair) {
            double arrivalTime = (double) (target - p[0]) / p[1];
            stack.push(arrivalTime);
            if(stack.size() >= 2 && stack.peek() <= stack.get(stack.size() - 2) ){
                stack.pop();
            }
        }


        return stack.size();

    }


    public Integer carFleet2(Integer target, Integer[] positions, Integer[]speeds) {
        HashMap<Integer, Integer> pairs = new HashMap<>();

        for(int i = 0; i < positions.length; i ++) {
            pairs.put(positions[i], speeds[i]);
        }

        Arrays.sort(positions,  Collections.reverseOrder());
        System.out.println("carFleet2 Pairs _--_-----__-____-___-----" + pairs);
        System.out.println("carFleet2 Positions _--_-----__-____-___-----" + Arrays.toString(positions));

        Stack<Integer> stack = new Stack<>();

        for(int p: positions){
            Integer arrivalTime = (target - p) / pairs.get(p);
            stack.push(arrivalTime);

            if(stack.size() >= 2 &&  stack.peek() <=  stack.get(stack.size() - 2)){
                stack.pop();
            }
        }

        return  stack.size();
    }

    public static void main(String[] args) {

        int target = 12;
        Integer[] positions = {10, 8, 0, 5, 3};
        Integer[] speeds = {2, 4, 1, 1, 3};

        _853_CarFleet _853_carFleet = new _853_CarFleet();
        int result = _853_carFleet.carFleet(target, positions, speeds);
        int result2 = _853_carFleet.carFleet2(target,positions,speeds);

        System.out.println(result);
        System.out.println(result2);
    }
}
