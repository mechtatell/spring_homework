package ru.mechtatell.Util.Collections;

public class Main {

    public static void main(String[] args) throws Exception {
        AdvancedList<Integer> myCollection = new MyCollection<>();
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < 30; i++) {
            myCollection.add((int) (Math.random() * 300 - 150));
            stringBuilder.append(myCollection.get(i).get()).append(" ");
        }
        myCollection = myCollection.sort(Integer::compareTo);
        myCollection.insert(0, 228);
        myCollection.insert(10, -333);
        myCollection.insert(32, 228);
        myCollection.insert(33, 228);
        myCollection.remove(0);
        myCollection.remove(0);
        myCollection.remove(30);
        myCollection.remove(30);

        stringBuilder.append("\n");
        for (int i = 0; i < myCollection.size(); i++) {
            stringBuilder.append(myCollection.get(i).get()).append(" ");
        }
        System.out.println(stringBuilder);
    }
}
