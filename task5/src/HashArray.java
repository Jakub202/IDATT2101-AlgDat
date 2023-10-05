import javax.print.DocFlavor;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class HashArray {

    private HashNode[] array;
    private int collisions;

    public HashArray(int length) {
        this.array = new HashNode[length];
        this.collisions = 0;
    }

    public int nextPrime(int input){
        int counter;
        input++;
        while(true){
            int l = (int) Math.sqrt(input);
            counter = 0;
            for(int i = 2; i <= l; i ++){
                if(input % i == 0)  counter++;
            }
            if(counter == 0)
                return input;
            else{
                input++;
            }
        }
    }


    public int modHash(String s){
        int sum = 0;
        int weight = 0;
        for(int i = 0; i < s.length(); i++){
            sum += s.charAt(i) * weight;
            weight++;
        }
        return sum % array.length;
    }

    public int multiHash(String s){
        int sum = 0;
        int weight = 0;
        double A = (Math.sqrt(5)-1)/2;
        for(int i = 0; i < s.length(); i++){
            sum += s.charAt(i) * weight;
            weight++;
        }

        return (int)Math.floor(array.length*(A*sum % 1));
    }


    public void push(String s) {
        int hash = multiHash(s);
        if (array[hash] == null) {
            array[hash] = new HashNode(s);
        } else {
            System.out.println("Collision at index: [" + hash + "] between " + s + " and " + array[hash].getData());
            collisions++;

            // Create a new node and set its next as the current head.
            HashNode newNode = new HashNode(s);
            newNode.setNext(array[hash]);

            // Set new node as the head.
            array[hash] = newNode;
        }
    }


    public HashNode getNode(String s){
        int hash = multiHash(s);
        HashNode current = array[hash];
        while(!current.getData().equals(s)){
            if(current.getNext() == null){
                return null;
            }
            current = current.getNext();
        }
        return current;
    }


    public int getNrOfElements(){
        int count = 0;
        for (HashNode n: array) {
            if(n != null){
                count++;
                while(n.getNext() != null){
                    n = n.getNext();
                    count ++;
                }
            }
        }
        return count;
    }

    public int getCollisions(){
        return collisions;
    }

    public int getArraySize(){
        return array.length;
    }

    public double getLoadFactor(){
        return (double)getNrOfElements()/array.length;
    }

    public double getCollisionsPerPerson(){
        return (double) collisions/getNrOfElements();
    }


    public static void main(String[] args){
        File file = new File("task5/src/navn.txt");
        Scanner scanner;
        try {
            scanner = new Scanner(file);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }


        HashArray hashArray = new HashArray(149);
        while(scanner.hasNext()){
            hashArray.push(scanner.nextLine());
        }


        System.out.println("Number of collisions: " + hashArray.getCollisions());
        System.out.println("Loadfactor: " + hashArray.getLoadFactor());
        System.out.println(hashArray.getNode("Jakub Maksymilian Rysiak").getData());
        System.out.println("Collisions per person: " + (double) hashArray.getCollisions() / hashArray.getNrOfElements());
        System.out.println("Number of nodes: " + hashArray.getNrOfElements());


    }
}
