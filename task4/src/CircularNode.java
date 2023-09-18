public class CircularNode {
    private int value;
    private CircularNode next;


    public CircularNode(int value, CircularNode next) {
        this.value = value;
        this.next = next;
    }

    public int getValue() {
        return value;
    }

    public CircularNode getNext() {
        return next;
    }

    public void setNext(CircularNode next) {
        this.next = next;
    }

}
