import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import static java.nio.file.Files.readAllLines;

public class ReadTxt {

    public static void main(String[] args) throws IOException {
        FileReader read = new FileReader("file.txt");
        BufferedReader br = new BufferedReader(read);
        List<String> lines = readAllLines(Paths.get("file.txt"));
        parseLines(lines);
    }



    public static void parseLines(List<String> lines) {
        Document document = DocumentHelper.createDocument();
        Element root = document.addElement("nd");
        Element name = root.addElement("Name");
        name.setText(lines.get(0));
        TreeNode<String> parent = new TreeNode<String>(lines.get(0));
        parent.setIndent(0);
        parent.setElement(root);
        lines.remove(0);
        addtoXML(lines,0,parent,root,name);
        XMLWriter output;
        OutputFormat format = OutputFormat.createPrettyPrint();
        try {
            output = new XMLWriter(new FileWriter("file.xml"), format);
            output.write(document);
            output.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void addtoXML(List<String> lines, Integer last_indent, TreeNode<String> last_node, Element root, Element parent_element) {
       System.out.println(lines);
        while (!lines.isEmpty()) {
            // same level, add to parent
            int indent = countblank(lines.get(0));

            if (indent == last_indent) {
                Element nd = parent_element.addElement("nd");
                Element name = nd.addElement("Name");
                name.setText(lines.get(0));
                lines.remove(0);
                addtoXML(lines,indent,last_node, root, parent_element);
            }
            // child level, add to last treeNode
            else if (indent > last_indent) {
                Element branch = root.addElement("Nodes");
                Element nd = branch.addElement("nd");
                Element name = nd.addElement("Name");
                name.setText(lines.get(0));
                TreeNode<String> child = new TreeNode<String>(lines.get(0));
                child.setIndent(indent);
                child.setElement(branch);
                last_node.addChildNode(child);
                child.setParentNode(last_node);
                lines.remove(0);
                addtoXML(lines,indent,child, nd, branch);
            }
            // not child level, find parent level
            else {
                TreeNode<String> last = getLastNode(indent,last_node);
                Element elementParent = findElementParent(indent,last_node);
                Element nd = elementParent.addElement("nd");
                Element name = nd.addElement("Name");
                name.setText(lines.get(0));
                TreeNode<String> child = new TreeNode<String>(lines.get(0));
                child.setIndent(indent);
                child.setElement(elementParent);
                last.addChildNode(child);
                child.setParentNode(last);
                lines.remove(0);
                addtoXML(lines,indent,last, nd, root);
            }
        }
    }

    private static TreeNode<String> getLastNode(Integer indent, Node<String> last_node) {
        TreeNode<String> treeNode = null;
        if (last_node.indent == indent) {
            treeNode = (TreeNode<String>) last_node;
        }
        else {
            treeNode = getLastNode(indent,last_node.parentNode);
        }
        return  treeNode;
    }

    private static Element findElementParent(int indent, Node<String> last_node) {
        Element e;
       // System.out.println(last_node.nodeEntity);
        if (last_node.indent== indent) {
            e = last_node.getElement();
        } else {
            e = findElementParent(indent,last_node.parentNode);
        }
        return e;
    }


    private static int countblank(String s) {
        int i = 0;
        while (s.charAt(i) == ' ') {
            i ++;
        }
        return i;
    }
}
