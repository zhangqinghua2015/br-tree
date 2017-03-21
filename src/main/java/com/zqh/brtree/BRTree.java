package com.zqh.brtree;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * Created by OrangeKiller on 2017/3/19.
 */
public class BRTree {

    private Node root;

    public void insert(Integer value) {
        Node node = search(value);
        // 节点已存在
        if (null != node) {
            return;
        }
        node = new Node(value);
        insertNode(node);
    }

    private void insertNode(Node node) {
        // 插入的是根节点
        if (null == root) {
            root = node;
            node.setColor(Node.BLACK);
            return;
        }
        Node m = root;
        Node parent = root;
        // 找到该插入的位置(找到node的父节点)
        while (m != null) {
            parent = m;
            if (m.getValue().compareTo(node.getValue()) > 0) {
                m = m.getLeft();
            } else if (m.getValue().compareTo(node.getValue()) < 0) {
                m = m.getRight();
            } else {
                return;
            }
        }
        node.setParent(parent);
        if (parent.getValue().compareTo(node.getValue()) > 0) {
            parent.setLeft(node);
        } else {
            parent.setRight(node);
        }
        // 父节点颜色为红色，需要进行修正
        if (parent.getColor() == Node.RED) {
            insertFixup(node);
        }
    }

    /**
     * 插入修正
     * @param node
     */
    private void insertFixup(Node node) {
        // 若当前节点是根节点(最终递归到根节点的情况)
        if (node == root) {
            node.setColor(Node.BLACK);
            return;
        }
        // 若修正节点的父节点颜色为黑色，则递归结束
        if (!node.getParent().isRed()) {
            return;
        }
        // 获取叔父节点、父节点、祖父节点
        Node uncle = node.getUncle();
        Node parent = node.getParent();
        Node grandParent = parent.getParent();
        if (null != uncle && uncle.isRed()) { // 叔父节点为红色
            uncle.setColor(Node.BLACK);
            parent.setColor(Node.BLACK);
            parent.getParent().setColor(Node.RED);
            insertFixup(grandParent);
        } else { // 叔父节点为黑色(null节点也为黑色)
            if (null != grandParent.getRight() && parent == grandParent.getRight()) {
                if (null != parent.getRight() && node == parent.getRight()) {
                    parent.setColor(Node.BLACK);
                    grandParent.setColor(Node.RED);
                    leftRotate(grandParent);
                } else {
                    rightRotate(parent);
                    insertFixup(parent);
                }
            } else {
                if (null != parent.getLeft() && node == parent.getLeft()) {
                    parent.setColor(Node.BLACK);
                    grandParent.setColor(Node.RED);
                    rightRotate(grandParent);
                } else {
                    leftRotate(parent);
                    insertFixup(parent);
                }
            }
        }
    }

    public void delete(Integer value) {
        Node node = search(value);
        // 节点不存在
        if (null == node) {
            return;
        }
        deleteNode(node);
    }

    private void deleteNode(Node node) {
        Node p = node.getParent();
        // node没有子节点
        if (null == node.getLeft() && null == node.getRight()) {
            // node为根节点
            if (node == root) {
                root = null;
                return;
            }
            boolean isRight = false;
            if (p.getRight() == node) {
                isRight = true;
                p.setRight(null);
            } else {
                p.setLeft(null);
            }
            // node 为黑色，进行修正
            if (!node.isRed()) {
                deleteFixup(node, isRight);
            }
            return;
        }

        // node有一个子节点，则node为黑色节点；将子节点颜色置为黑色，并将子节点顶替node
        if (null == node.getRight() || null == node.getLeft()) {
            Node children = node.getLeft();
            if (null == children) {
                children = node.getRight();
            }
            children.setColor(Node.BLACK);
            if (node == root) {
                root = children;
            } else {
                if (p.getRight() == node) {
                    p.setRight(children);
                } else {
                    p.setLeft(children);
                }
                children.setParent(p);
            }
            return;
        }

        // node为双子非空节点，找到顶替节点
        Node replace = node.getRight();
        while (replace.getLeft() != null) {
            replace = replace.getLeft();
        }
        // 将顶替节点的value赋值给node
        node.setValue(replace.getValue());
        // 删除顶替节点
        deleteNode(replace);
    }

    private void deleteFixup(Node node, boolean isRight) {
        Node p = node.getParent();
        Node b = isRight ?  p.getLeft() : p.getRight();
        // node节点原来为其父节点的右子节点
        if (isRight) {
            // 兄弟节点为红色
            if (b.isRed()) {
                b.setColor(Node.BLACK);
                p.setColor(Node.RED);
                rightRotate(p);
                deleteFixup(node, isRight);
                return;
            }
            Node br = b.getRight();
            // 左侄子节点为红色
            if (br != null && br.isRed()) {
                br.setColor(p.getColor());
                p.setColor(Node.BLACK);
                leftRotate(b);
                rightRotate(p);
                return;
            }
            Node bl = b.getLeft();
            // 右侄子节点为红色
            if (bl != null && bl.isRed()) {
                b.setColor(p.getColor());
                bl.setColor(Node.BLACK);
                p.setColor(Node.BLACK);
                rightRotate(p);
                return;
            }
        } else {
            Node bl = b.getLeft();
            // 兄弟节点为红色
            if (b.isRed()) {
                b.setColor(Node.BLACK);
                p.setColor(Node.RED);
                leftRotate(p);
                deleteFixup(node, isRight);
                return;
            }
            // 左侄子节点为红色
            if (bl != null && bl.isRed()) {
                bl.setColor(p.getColor());
                p.setColor(Node.BLACK);
                rightRotate(b);
                leftRotate(p);
                return;
            }
            Node br = b.getRight();
            // 右侄子节点为红色
            if (br != null && br.isRed()) {
                b.setColor(p.getColor());
                br.setColor(Node.BLACK);
                p.setColor(Node.BLACK);
                leftRotate(p);
                return;
            }
        }
        // 兄弟节点、侄子节点都不为红色节点
        // 父节点为红色
        if (p.isRed()) {
            p.setColor(Node.BLACK);
            b.setColor(Node.RED);
            return;
        }
        // 父节点为黑色
        b.setColor(Node.RED);
        // 父节点为根节点
        if (p == root) {
            return;
        }
        deleteFixup(p, p == p.getParent().getRight());
    }

    /**
     * 通过value查找节点
     * @param value
     * @return
     */
    public Node search(Integer value) {
        if (null == root) {
            return null;
        }
        Node node = root;
        while (null != node) {
            if (node.getValue().compareTo(value) == 0) {
                return node;
            } else if (node.getValue().compareTo(value) < 0) {
                node = node.getRight();
            } else if (node.getValue().compareTo(value) > 0) {
                node = node.getLeft();
            }
        }
        return null;
    }

    /**
     * 左旋
     * @param node
     */
    public void leftRotate(Node node) {
        if (null == node || null == node.getRight()) {
            return;
        }
        Node p = node.getParent();
        Node r = node.getRight();
        Node rl = r.getLeft();
        // 重新设置node的右子节点
        node.setRight(rl);
        // 重新设置node的父节点
        node.setParent(r);
        // 重新设置node的右子节点的(左子节点的父节点)
        if (null != rl) {
            rl.setParent(node);
        }
        // 重新设置node的右子节点的左子节点
        r.setLeft(node);
        // 重新设置node节点的右子节点的父节点
        r.setParent(p);

        // node原来的父节点为空
        if (null == p) {
            this.root = r;
        } else {
            // 将node原来的右子节点顶替node的位置
            if (node == p.getLeft()) {
                p.setLeft(r);
            } else {
                p.setRight(r);
            }
        }
    }

    /**
     * 右旋
     * @param node
     */
    public void rightRotate(Node node) {
        if (null == node || null == node.getLeft()) {
            return;
        }
        Node p = node.getParent();
        Node l = node.getLeft();
        Node lr = l.getRight();
        node.setLeft(lr);
        node.setParent(l);
        if (null != lr) {
            lr.setParent(node);
        }
        l.setRight(node);
        l.setParent(p);
        if (null == p) {
            this.root = l;
        } else {
            if (node == p.getLeft()) {
                p.setLeft(l);
            } else {
                p.setRight(l);
            }
        }
    }

    /**
     * 获取以给定节点为根节点的树的深度
     * @param node
     * @return
     */
    public static int getDepth(Node node) {
        if (null == node) {
            return 0;
        }
        int rightDepth = 1 + getDepth(node.getRight());
        int leftDepth = 1 + getDepth(node.getLeft());
        return rightDepth > leftDepth ? rightDepth : leftDepth;
    }

    /**
     *
     * @param isRoot 当前节点是否为根节点
     * @param totalDepth 二叉树层数
     * @param parentDepthNum 父节点所在层数(根节点为第1层)
     * @param parentX 父节点水平下标
     * @param node 当前节点
     * @param depthNodesArray 存放二叉树节点的二维数组
     */
    public static void printTree(boolean isRoot, int totalDepth, int parentDepthNum, int parentX, Node node, Node[][] depthNodesArray) {
        if (null == node) {
            return;
        }
        int x; // 水平下标
        int y; // 垂直下标
        if (isRoot) {
            parentDepthNum = 0;
            depthNodesArray = new Node[totalDepth][(1<<totalDepth) - 1];
            x = (1 << (totalDepth - 1)) - 1;
        } else {
            // 知道第i层某个节点在水平一维数组中的下标x后，
            // 我们可知其左子节点的下标为x-2^(n-i-1)，
            // 右子节点的下标为x+2^(n-i-1)
            if (node == node.getParent().getLeft()) {
                x = parentX - (1<<(totalDepth - parentDepthNum - 1));
            } else {
                x = parentX + (1<<(totalDepth - parentDepthNum - 1));
            }
        }
        y = parentDepthNum; // 当前节点所在层数减去一
        depthNodesArray[y][x] = node;
        // 将左右子节点放入数组
        printTree(false, totalDepth, parentDepthNum + 1, x, node.getLeft(), depthNodesArray);
        printTree(false, totalDepth, parentDepthNum + 1, x, node.getRight(), depthNodesArray);

        if (isRoot) {
            for (Node[] depthNodes : depthNodesArray) {
                for (Node depthNode : depthNodes) {
                    if (null == depthNode) {
                        System.out.print("      ");
                    } else {
                        System.out.print(depthNode.getValue() + ":" + (depthNode.getColor() ? "red  " : "black"));
                    }
                }
                System.out.println();
            }
        }
    }

//    /**
//     * 获取前驱节点
//     * @param node
//     * @return
//     */
//    public Node getPrecursor(Node node) {
//        if (null == node || null == node.getLeft()) {
//            return null;
//        }
//        Node precursor = node.getLeft();
//        while (null != precursor.getRight()) {
//            precursor = precursor.getRight();
//        }
//        return precursor;
//    }
//
//    /**
//     * 获取后继节点
//     * @param node
//     * @return
//     */
//    public Node getSucceed(Node node) {
//        if (null == node || null == node.getRight()) {
//            return null;
//        }
//        Node succeed = node.getRight();
//        while (null != succeed.getLeft()) {
//            succeed = succeed.getLeft();
//        }
//        return succeed;
//    }

    public Node getRoot() {
        return root;
    }

    public void setRoot(Node root) {
        this.root = root;
    }


    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
