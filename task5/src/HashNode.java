public class HashNode {
     private String data;
     private HashNode next;

    public HashNode(String data) {
        this.data = data;
    }

    public HashNode getNext() {
        return next;
    }

    public String getData(){
        return this.data;
    }

    public void setData(String s){
        this.data = s;
    }

    public void setNext(HashNode n){
        this.next = n;
    }


}
