import java.io.*;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import javax.swing.tree.DefaultMutableTreeNode;

import static java.nio.file.Files.readAllLines;

public class ReadTxt {
    public static DefaultMutableTreeNode head;

    public static void main(String[] args) {
        FileReader read = null;
        try {
            read = new FileReader("file.txt");

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        BufferedReader br = new BufferedReader(read);
        List<String> lines = null;
        try {
            lines = readAllLines(Paths.get("file.txt"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        parseLines(lines);
      //  new Form_design();
    }


    public static void parseLines(List<String> lines) {
        Document document = DocumentHelper.createDocument();
        Element root = document.addElement("nd");
        Element name = root.addElement("Name");
        name.setText(lines.get(0));
        TreeNode<String> parent = new TreeNode<String>(lines.get(0));
        parent.setIndent(0);
        parent.setElement(root);
        head=new DefaultMutableTreeNode(lines.get(0));
        lines.remove(0);
        addtoXML(lines,0,parent,root,name,head);
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

    private static void addtoXML(List<String> lines, Integer last_indent, TreeNode<String> last_node, Element root, Element parent_element, DefaultMutableTreeNode previous_node) {
       //System.out.println(lines);
        while (!lines.isEmpty() && !(lines.get(0).trim().isEmpty())) {
            // same level, add to parent
            int indent = countblank(lines.get(0));

            if (indent == last_indent) {
                DefaultMutableTreeNode sub=new DefaultMutableTreeNode(lines.get(0).trim());
                previous_node.getLastLeaf().add(sub);
                Element nd = parent_element.addElement("nd");
                Element name = nd.addElement("Name");
                name.setText(lines.get(0));
                lines.remove(0);
                addtoXML(lines,indent,last_node, root, parent_element, previous_node);
            }
            // child level, add to last treeNode
            else if (indent > last_indent) {
                DefaultMutableTreeNode sub=new DefaultMutableTreeNode(lines.get(0).trim());
                previous_node.add(sub);
                Element branch = root.addElement("Nodes");
                Element nd = branch.addElement("nd");
                Element name = nd.addElement("Name");
                name.setText(lines.get(0));
                TreeNode<String> child = new TreeNode<String>(lines.get(0));
                child.setName(lines.get(0).trim());
                child.setIndent(indent);
                child.setElement(branch);
                last_node.addChildNode(child);
                child.setParentNode(last_node);
                lines.remove(0);
                addtoXML(lines,indent,child, nd, branch, sub);
            }
            // not child level, find parent level
            else {
                DefaultMutableTreeNode sub=new DefaultMutableTreeNode(lines.get(0).trim());

                TreeNode<String> last = getLastNode(indent,last_node);
                Element elementParent = findElementParent(indent,last_node);
                Element nd = elementParent.addElement("nd");
                Element name = nd.addElement("Name");
                name.setText(lines.get(0));
                TreeNode<String> child = new TreeNode<String>(lines.get(0));
                child.setIndent(indent);
                child.setElement(elementParent);
                child.setName(lines.get(0).trim());
                last.addChildNode(child);
                child.setParentNode(last);
                lines.remove(0);
                addtoXML(lines,indent,last, nd, root, previous_node);
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
