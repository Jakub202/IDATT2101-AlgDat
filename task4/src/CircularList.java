public class CircularList {
    private int size;

    private CircularNode head;

    public CircularList() {
        size = 0;
    }

    public void add(int value) {
        if(size == 0){
            head = new CircularNode(value, null);
            head.setNext(head);
            size++;
        }else{
            CircularNode current = head;
            while(current.getNext() != head){
                current = current.getNext();
            }
            current.setNext(new CircularNode(value, head));

            size++;
        }
    }

    public void remove(CircularNode node){
        if(size == 1){
            head = null;
            size--;
        } else{
            CircularNode current = head;
            while(current.getNext() != node){
                current = current.getNext();
            }
            if(node.getValue() == head.getValue()){
                head = current.getNext().getNext();
            }
            current.setNext(node.getNext());
            size--;
        }
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public CircularNode getHead() {
        return head;
    }

}
