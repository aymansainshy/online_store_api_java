package Java101.collections;


import java.util.*;


/**
 ** @Collection_Api **

  Map      -> HashMap()
           ->

  Iterable -> Collection -> List   -> ArrayList()
                                   -> LinkedList()

                         -> Set    -> HashSet()
                                   -> LinkedHashSet()

                         -> Queue  -> Dequeue()
 */

public class CollectionApi {
    public static void main(String[] args) {
        Comparator<Integer> comparator = new Comparator<Integer>() {
            @Override
            public int compare(Integer i, Integer j) {
                if (i % 10 > j % 10) {
                    return 1;
                } else {
                    return -1;
                }
            }
        };

        ArrayList<Integer> collection = new ArrayList<>();
        collection.add(11);
        collection.add(23);
        collection.add(35);
        collection.add(43);
        collection.add(41);

        Collections.sort(collection);

        System.out.println(collection);
        collection.forEach(System.out::println);


        System.out.println("\n___--_-----_--__-____--__-----__--__--LinkedList___--__--____-___---__--___-_--_---_-----\n");

        LinkedList<Integer> linkedList = new LinkedList<>();

        linkedList.add(2);
        linkedList.add(22);
        linkedList.add(55);
        linkedList.add(99);

        System.out.println(linkedList);
        int result = linkedList.getLast();
        System.out.println(result);


        System.out.println("\n___--_-----_--__-____--__-----__--SET--__--____-___---__--__--__----_-_-_--_---_-----\n");

        Set<Integer> set = new TreeSet<>();
        set.add(1);
        set.add(2);
        set.add(3);
        set.add(4);
        set.add(4);

        System.out.println(set);
    }
}
