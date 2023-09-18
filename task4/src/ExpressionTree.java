public class ExpressionTree {
    private ExpressionNode root;

    public ExpressionTree(ExpressionNode root) {
        this.root = root;
    }

    public double evaluateExpression() {
        return evaluate(root);
    }

    public double evaluate(ExpressionNode node) {
        if (node.isOperator()) {
            switch (node.getOperator()) {
                case '+':
                    return evaluate(node.getLeft()) + evaluate(node.getRight());
                case '-':
                    return evaluate(node.getLeft()) - evaluate(node.getRight());
                case '*':
                    return evaluate(node.getLeft()) * evaluate(node.getRight());
                case '/':
                    return evaluate(node.getLeft()) / evaluate(node.getRight());
                default:
                    return 0;
            }
        } else {
            return node.getValue();
        }
    }


    public ExpressionNode getRoot() {
        return root;
    }

    public void setRoot(ExpressionNode root) {
        this.root = root;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        return toString(root);
    }

    public String toString(ExpressionNode node) {
        StringBuilder builder = new StringBuilder();
        if (node.isOperator()) {
            if (node.getOperator() == '+' || node.getOperator() == '-') {
                builder.append("(");
                builder.append(toString(node.getLeft()));
                builder.append(node.getOperator());
                builder.append(toString(node.getRight()));
                builder.append(")");
            }else{
                builder.append(toString(node.getLeft()));
                builder.append(node.getOperator());
                builder.append(toString(node.getRight()));
            }
        }else {
            builder.append(node.getValue());
        }
        return builder.toString();
    }
}
