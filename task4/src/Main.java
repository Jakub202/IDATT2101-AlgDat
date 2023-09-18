public class Main {
    public static void main(String[] args) {

        //eksemplet fra boka hvor hver fjerde person blir drept(altså m = 3) fordi det er 3 personer mellom hver som blir drept
        System.out.println(solveJosephus(10, 3));

        ExpressionNode root = new ExpressionNode('/',
                new ExpressionNode('*', new ExpressionNode(3), new ExpressionNode('+', new ExpressionNode(2), new ExpressionNode(4))),
                new ExpressionNode('-', new ExpressionNode(7), new ExpressionNode('*', new ExpressionNode(2), new ExpressionNode(2)))
        );

        System.out.println(new ExpressionTree(root).evaluateExpression());
        System.out.println(new ExpressionTree(root));

    }

    //m er antallet af personer der skal springes over
    public static int solveJosephus(int n, int m){
        CircularList list = new CircularList();
        for(int i = 1; i <= n; i++){
            list.add(i);
        }
        CircularNode current = list.getHead();

        while(list.getSize() > 1){
            for(int i = 0; i < m; i++){
                current = current.getNext();
            }
            //System.out.println("kill " + current.getValue());
            list.remove(current);
            current = current.getNext();
        }
        return list.getHead().getValue();
    }






}