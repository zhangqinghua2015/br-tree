package com.zqh.brtree;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * Created by OrangeKiller on 2017/3/19.
 */
public class Node {

    public static boolean RED = true;
    public static boolean BLACK = false;

    private Integer value;
    private Node parent;
    private Node left;
    private Node right;
    private boolean color;

    public Node() {
        this(0);
    }

    public Node(Integer value) {
        this(value, Node.RED);
    }

    public Node(Integer value, boolean color) {
        this.value = value;
        this.color = color;
    }

    public Node getUncle() {
        Node parent = this.getParent();
        if (null == parent) {
            return null;
        }
        Node grandParent = parent.getParent();
        if (null == grandParent) {
            return null;
        }
        if (parent == grandParent.getLeft()) {
            return grandParent.getRight();
        } else {
            return grandParent.getLeft();
        }
    }

    public boolean isRed() {
        return this.color;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public Node getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public Node getLeft() {
        return left;
    }

    public void setLeft(Node left) {
        this.left = left;
    }

    public Node getRight() {
        return right;
    }

    public void setRight(Node right) {
        this.right = right;
    }

    public boolean getColor() {
        return color;
    }

    public void setColor(boolean color) {
        this.color = color;
    }

    @Override
    public boolean equals(Object obj) {
        if (null == obj) {
            return false;
        }
        if (obj instanceof Node) {
            return this.value.equals(((Node) obj).getValue()) && this.color == ((Node) obj).getColor();
        }
        return false;
    }


    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
