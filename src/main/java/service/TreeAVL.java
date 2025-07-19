package service;

import util.Estatisticas;

import java.util.List;

public class TreeAVL<T extends Comparable<T>> implements BalancedTree<T> {

    public static class NodeAVL<T> {
        T element;
        NodeAVL<T> left;
        NodeAVL<T> right;
        int height;

        public NodeAVL(T e) {
            this.element = e;
            this.left = null;
            this.right = null;
            this.height = 0;
        }
    }

    private NodeAVL<T> root;
    private Estatisticas estat;

    public TreeAVL(Estatisticas estat) {
        this.estat = estat;
    }

    @Override
    public void insert(T value) {
        root = insert(root, value);
    }

    private NodeAVL<T> insert(NodeAVL<T> node, T value) {
        if (node == null) {
            estat.atribuicoes++;
            return new NodeAVL<>(value);
        }

        estat.comparacoes++;
        int cmp = value.compareTo(node.element);
        estat.atribuicoes++;

        if (cmp < 0) {
            node.left = insert(node.left, value);
            estat.atribuicoes++;
        } else if (cmp > 0) {
            estat.comparacoes++;
            node.right = insert(node.right, value);
            estat.atribuicoes++;
        } else {
            estat.comparacoes++;
            return node;
        }

        updateHeight(node);
        return balance(node);
    }

    private void updateHeight(NodeAVL<T> node) {
        node.height = 1 + Math.max(height(node.left), height(node.right));
        estat.atribuicoes++;
    }

    private int height(NodeAVL<T> node) {
        return (node == null) ? -1 : node.height;
    }

    private int getBalance(NodeAVL<T> node) {
        return (node == null) ? 0 : height(node.left) - height(node.right);
    }

    private NodeAVL<T> balance(NodeAVL<T> node) {
        int balance = getBalance(node);
        estat.atribuicoes++;

        if (balance > 1) {
            estat.comparacoes++;
            if (getBalance(node.left) < 0) {
                estat.comparacoes++;
                node.left = rotateLeft(node.left);
                estat.atribuicoes++;
            }
            node = rotateRight(node);
            estat.atribuicoes++;
        } else if (balance < -1) {
            estat.comparacoes++;
            if (getBalance(node.right) > 0) {
                estat.comparacoes++;
                node.right = rotateRight(node.right);
                estat.atribuicoes++;
            }
            node = rotateLeft(node);
            estat.atribuicoes++;
        }

        return node;
    }

    private NodeAVL<T> rotateRight(NodeAVL<T> y) {
        NodeAVL<T> x = y.left;
        NodeAVL<T> T2 = x.right;
        estat.atribuicoes += 2;

        x.right = y;
        y.left = T2;
        estat.atribuicoes += 2;

        updateHeight(y);
        updateHeight(x);

        return x;
    }

    private NodeAVL<T> rotateLeft(NodeAVL<T> y) {
        NodeAVL<T> x = y.right;
        NodeAVL<T> T2 = x.left;
        estat.atribuicoes += 2;

        x.left = y;
        y.right = T2;
        estat.atribuicoes += 2;

        updateHeight(y);
        updateHeight(x);

        return x;
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

    private boolean findRecursive(NodeAVL<T> node, T value) {
        if (node == null) return false;
        int cmp = value.compareTo(node.element);
        if (cmp < 0) return findRecursive(node.left, value);
        if (cmp > 0) return findRecursive(node.right, value);
        return true;
    }

    @Override
    public int getHeight() {
        return height(root);
    }

    @Override
    public void printInOrder() {
        printInOrderRecursive(root);
        System.out.println();
    }

    private void printInOrderRecursive(NodeAVL<T> node) {
        if (node != null) {
            printInOrderRecursive(node.left);
            System.out.print(node.element + " ");
            printInOrderRecursive(node.right);
        }
    }

    public void collectInOrder(java.util.List<T> lista) {
        collectInOrderRecursive(root, lista);
    }

    private void collectInOrderRecursive(NodeAVL<T> node, java.util.List<T> lista) {
        if (node != null) {
            collectInOrderRecursive(node.left, lista);
            lista.add(node.element);
            collectInOrderRecursive(node.right, lista);
        }
    }

    public NodeAVL<T> getRoot() {
        return this.root;
    }
}
