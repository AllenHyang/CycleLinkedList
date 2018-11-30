package com.hanyangtech;

public  class Node<E> implements Cloneable{
    public E value;
    public Node<E> nextNode;
    public Node<E> previousNode;

    public Node(E value, Node<E> nextNode, Node<E> previousNode) {
        this.value = value;
        this.nextNode = nextNode;
        this.previousNode = previousNode;
    }
    public E getValue() {
        return value;
    }
    public Node<E> getNext(){return nextNode;}
    public Node<E> getPrevious(){return previousNode;}
    public void setNextNode(Node<E> nextNode) {
        this.nextNode = nextNode;
    }
    public void setPreviousNode(Node<E> previousNode) {
        this.previousNode = previousNode;
    }

    @Override
    public Node<E> clone()  {
        Node<E> node=null;
        try {
            node= (Node<E>) super.clone();

        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return node;
    }
}