import java.util.ArrayList;


@SuppressWarnings("serial")
public class TreeNode<T> extends Node<T>{


    public TreeNode(T nodeEntity) {
        super();
        super.nodeEntity = nodeEntity;
    }

    protected void addChildNode(Node<T> childNode) {
        childNode.setParentNode(this);
        if(childNodes == null) {
            childNodes = new ArrayList<Node<T>>();
        }
        this.childNodes.add(childNode);
    }



}
