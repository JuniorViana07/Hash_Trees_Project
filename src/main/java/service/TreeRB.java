package service;

import util.Estatisticas;

import java.util.List;

public class TreeRB<T extends Comparable<T>> implements BalancedTree<T> {

    private NodeRB<T> root;
    private Estatisticas estat;

    public TreeRB(Estatisticas estat) {
        this.estat = estat;
    }

    public static class NodeRB<AnyType> {
        AnyType element;
        NodeRB<AnyType> parent, left, right;
        Color color;

        enum Color {
            RED, BLACK
        }

        NodeRB(AnyType e) {
            this(e, null, null, null, false);
        }

        NodeRB(AnyType e, NodeRB<AnyType> l, NodeRB<AnyType> r, NodeRB<AnyType> p, boolean isBlack) {
            element = e;
            left = l;
            right = r;
            parent = p;
            color = isBlack ? Color.BLACK : Color.RED;
        }
    }

    @Override
    public void insert(T value) {
        root = insert(root, value);
        root.color = NodeRB.Color.BLACK;
        estat.atribuicoes++;
    }

    private NodeRB<T> insert(NodeRB<T> h, T value) {
        if (h == null) {
            estat.atribuicoes++;
            return new NodeRB<>(value);
        }

        int cmp = value.compareTo(h.element);
        estat.atribuicoes++;
        estat.comparacoes++;

        if (cmp < 0) {
            h.left = insert(h.left, value);
            estat.atribuicoes++;
            h.left.parent = h;
            estat.atribuicoes++;
        } else if (cmp > 0) {
            estat.comparacoes++;
            h.right = insert(h.right, value);
            estat.atribuicoes++;
            h.right.parent = h;
            estat.atribuicoes++;
        } else {
            estat.comparacoes++;
            return h;
        }

        if (isRed(h.right) && !isRed(h.left)) {
            estat.comparacoes += 2;
            h = rotateLeft(h);
            estat.atribuicoes++;
        }
        if (isRed(h.left) && isRed(h.left.left)) {
            estat.comparacoes += 2;
            h = rotateRight(h);
            estat.atribuicoes++;
        }
        if (isRed(h.left) && isRed(h.right)) {
            estat.comparacoes += 2;
            flipColors(h);
            estat.atribuicoes++;
        }

        return h;
    }

    private boolean isRed(NodeRB<T> node) {
        if (node == null) return false;
        return node.color == NodeRB.Color.RED;
    }

    private NodeRB<T> rotateLeft(NodeRB<T> h) {
        NodeRB<T> x = h.right;
        h.right = x.left;
        estat.atribuicoes++;
        if (x.left != null) {
            x.left.parent = h;
            estat.atribuicoes++;
        }
        x.left = h;
        estat.atribuicoes++;
        x.color = h.color;
        h.color = NodeRB.Color.RED;
        estat.atribuicoes += 2;
        return x;
    }

    private NodeRB<T> rotateRight(NodeRB<T> h) {
        NodeRB<T> x = h.left;
        h.left = x.right;
        estat.atribuicoes++;
        if (x.right != null) {
            x.right.parent = h;
            estat.atribuicoes++;
        }
        x.right = h;
        estat.atribuicoes++;
        x.color = h.color;
        h.color = NodeRB.Color.RED;
        estat.atribuicoes += 2;
        return x;
    }

    private void flipColors(NodeRB<T> h) {
        h.color = NodeRB.Color.RED;
        if (h.left != null) h.left.color = NodeRB.Color.BLACK;
        if (h.right != null) h.right.color = NodeRB.Color.BLACK;
        estat.atribuicoes += (1 + (h.left != null ? 1 : 0) + (h.right != null ? 1 : 0));
    }

    @Override
    public boolean remove(T value) {
        // fazer remocao depois, pois não precisa no trabalho
        return false;
    }

    @Override
    public boolean find(T value) {
        return findRecursive(root, value);
    }

    private boolean findRecursive(NodeRB<T> node, T value) {
        if (node == null) return false;
        int cmp = value.compareTo(node.element);
        if (cmp < 0) return findRecursive(node.left, value);
        if (cmp > 0) return findRecursive(node.right, value);
        return true;
    }

    @Override
    public int getHeight() {
        return getHeightRecursive(root);
    }

    private int getHeightRecursive(NodeRB<T> node) {
        if (node == null) return -1;
        return 1 + Math.max(getHeightRecursive(node.left), getHeightRecursive(node.right));
    }

    @Override
    public void printInOrder() {
        printInOrderRecursive(root);
        System.out.println();
    }

    private void printInOrderRecursive(NodeRB<T> node) {
        if (node != null) {
            printInOrderRecursive(node.left);
            System.out.print(node.element + " ");
            printInOrderRecursive(node.right);
        }
    }

    public void collectInOrder(java.util.List<T> lista) {
        collectInOrderRecursive(root, lista);
    }

    private void collectInOrderRecursive(NodeRB<T> node, java.util.List<T> lista) {
        if (node != null) {
            collectInOrderRecursive(node.left, lista);
            lista.add(node.element);
            collectInOrderRecursive(node.right, lista);
        }
    }

    public NodeRB<T> getRoot() {
        return this.root;
    }

}
