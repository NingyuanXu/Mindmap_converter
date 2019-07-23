
import java.io.Serializable;
import java.util.List;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;


@SuppressWarnings("serial")
public class Node<T> implements Serializable {
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    protected Node<T> parentNode;
    protected T nodeEntity;
    protected List<Node<T>> childNodes;
    protected int indent;
    protected Element element;
    protected String name;

    public Element getElement() {
        return element;
    }

    public void setElement(Element element) {
        this.element = element;
    }


    public void setIndent(int indent) {
        this.indent = indent;
    }

    public void setParentNode(Node<T> parentNode) {
        this.parentNode = parentNode;
    }


}
