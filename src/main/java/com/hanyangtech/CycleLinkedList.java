package com.hanyangtech;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class CycleLinkedList<E>  extends AbstractSequentialList<E> {



    public  class Node<E> {
        private E value;
        private Node<E> nextNode;
        private Node<E> previousNode;

        Node(E value, Node<E> nextNode, Node<E> previousNode) {
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
    }
    private int size=0;
    private Node<E> header = new Node(null, null, null);

    public CycleLinkedList(){
        header.nextNode = header.previousNode =header;
    }
    public Node<E> getLastNode(){
        return header.getPrevious();
    }

    public Node getNode(E e){
        Node curNode=header;
        for(int i=0;i<size;i++){
            curNode=curNode.getNext();
            if(curNode.getValue()==e){
                return curNode;
            }
        }
        throw new RuntimeException("Node Not found");
    }

    public Node<E> getNode(int index){
        if (index < 0 || index >= size)
            throw new IndexOutOfBoundsException("Index:"+index+", Size:"+size);
        Node<E> node=header;
        if (index < (size >> 1)) {//index<size/2，则说明index在前半个链表中，从前往后找
            for (int i = 0; i <= index; i++)
                node = node.nextNode;
        } else {//index>=size/2，则说明index在后半个链表中，从后往前找
            for (int i = size; i > index; i--)
                node = node.previousNode;
        }
        return node;

    }

    public Node<E> getFirstNode() {
        return header.getNext();
    }

    public void moveHeader(Node<E> node){
        Node newHeader=addbefore(null,node);
        header.getPrevious().setNextNode(header.getNext());
        header.getNext().setPreviousNode(header.getPrevious());
        header=newHeader;
    }

    public boolean add(E e) {
        addbefore(e,header);
        size++;
        return true;
    }

    public  Node addbefore(E e, Node<E> node){
        Node<E> newNode =new Node<>(e, node, node.previousNode);
        node.previousNode.nextNode = newNode;
        node.previousNode = newNode;
        return newNode;
    }

    public E get(int index) {
        return getNode(index).value;
    }

    @Override
    public String toString() {

        List<String> s=this.stream().map(Object::toString).collect(Collectors.toList());
        return s.toString();
    }

    @Override
    public ListIterator<E> listIterator(int index) {
        return new CycleLinkedListIterator(index);
    }

    private class CycleLinkedListIterator implements ListIterator<E> {

        int cursor = 0;//标记位：标记遍历到哪一个元素
        int expectedModCount = modCount;//标记位：用于判断是否在遍历的过程中，是否发生了add、remove操作


        public CycleLinkedListIterator(int index){
            cursor=index;
        }

        @Override
        public boolean hasNext() {
            return cursor != size();//如果cursor==size，说明已经遍历完了，上一次遍历的是最后一个元素
        }

        @Override
        public E next() {
            checkForComodification();//检测在遍历的过程中，是否发生了add、remove操作
            try {
                E next = get(cursor++);
                return next;
            } catch (IndexOutOfBoundsException e) {//捕获get(cursor++)方法的IndexOutOfBoundsException
                checkForComodification();
                throw new NoSuchElementException();
            }        }



        final void checkForComodification() {
            if (modCount != expectedModCount)//发生了add、remove操作,这个我们可以查看add等的源代码，发现会出现modCount++
                throw new ConcurrentModificationException();
        }
        @Override
        public boolean hasPrevious() {
            return false;
        }

        @Override
        public E previous() {
            checkForComodification();//检测在遍历的过程中，是否发生了add、remove操作
            try {
                E pre = get(cursor-2);
                return pre;
            } catch (IndexOutOfBoundsException e) {//捕获get(cursor++)方法的IndexOutOfBoundsException
                checkForComodification();
                throw new NoSuchElementException();
            }            }

        @Override
        public int nextIndex() {
            return 0;
        }

        @Override
        public int previousIndex() {
            return 0;
        }

        @Override
        public void remove() {
            Node curNode=getNode(cursor-1);
            Node nextNode=curNode.getNext();
            Node preNode=curNode.getPrevious();
            preNode.setNextNode(nextNode);
            nextNode.setPreviousNode(preNode);
            size--;
            cursor--;
        }

        @Override
        public void set(Object o) {

        }

        @Override
        public void add(Object o) {

        }
    }

    @Override
    public int size() {
        return size;
    }

    public <R> R min(Function<E,R> getValue, Comparator<R> comparator){

        R result=this.stream().map(getValue).min(comparator).get();
        return result;
        }
    public <R> R max(Function<E,R> getValue, Comparator<R> comparator){

        R result=this.stream().map(getValue).max(comparator).get();
        return result;
    }

    public void removeNode(Node node){
        Node preNode=node.getPrevious();
        Node nextNode=node.getNext();
        preNode.setNextNode(nextNode);
        nextNode.setPreviousNode(preNode);
        size--;
    }


}
