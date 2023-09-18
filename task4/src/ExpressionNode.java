public class ExpressionNode {
    private boolean isOperator;
    private char operator;
    private int value;
    private ExpressionNode left;
    private ExpressionNode right;

    public ExpressionNode(int value) {
        this.isOperator = false;
        this.value = value;
        this.left = null;
        this.right = null;
    }

    public ExpressionNode(int value, ExpressionNode left, ExpressionNode right) {
        this.isOperator = false;
        this.value = value;
        this.left = left;
        this.right = right;
    }

    public ExpressionNode(char operator, ExpressionNode left, ExpressionNode right) {
        this.isOperator = true;
        this.operator = operator;
        this.left = left;
        this.right = right;
    }

    public ExpressionNode getLeft() {
        return left;
    }

    public ExpressionNode getRight() {
        return right;
    }

    public void setLeft(ExpressionNode left) {
        this.left = left;
    }

    public void setRight(ExpressionNode right) {
        this.right = right;
    }

    public boolean isOperator() {
        return isOperator;
    }

    public char getOperator() {
        return operator;
    }

    public int getValue() {
        return value;
    }


}
