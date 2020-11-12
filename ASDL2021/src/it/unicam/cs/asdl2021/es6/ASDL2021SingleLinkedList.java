package it.unicam.cs.asdl2021.es6;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;

/**
 * Lista concatenata singola che non accetta valori null, ma permette elementi
 * duplicati.
 * 
 * @author Template: Luca Tesei, Implementazione: collettiva
 *
 * @param <E>
 *                il tipo degli elementi della lista
 */
public class ASDL2021SingleLinkedList<E> implements List<E> {

    private int size;

    private Node<E> head;

    private Node<E> tail;

    private int numeroModifiche;

    /**
     * Crea una lista vuota.
     */
    public ASDL2021SingleLinkedList() {
        this.size = 0;
        this.head = null;
        this.tail = null;
        this.numeroModifiche = 0;
    }

    /*
     * Classe per i nodi della lista concatenata
     */
    private static class Node<E> {
        private E item;

        private Node<E> next;

        /*
         * Crea un nodo "singolo" equivalente a una lista con un solo elemento.
         */
        Node(E item, Node<E> next) {
            if(item==null)
                throw new NullPointerException();
            this.item = item;
            this.next = next;
        }
    }

    /*
     * Classe che realizza un iteratore per ASDL2021SingleLinkedList.
     * L'iteratore deve essere fail-safe, cioè deve lanciare una eccezione
     * IllegalStateException se a una chiamata di next() si "accorge" che la
     * lista è stata cambiata rispetto a quando l'iteratore è stato creato.
     */
    private class Itr implements Iterator<E> {

        private Node<E> lastReturned;

        private int numeroModificheAtteso;

        private Itr() {
            // All'inizio il cursore è null
            this.lastReturned = null;
            this.numeroModificheAtteso = ASDL2021SingleLinkedList.this.numeroModifiche;
        }

        @Override
        public boolean hasNext() {
            return lastReturned.next!=null;
        }

        @Override
        public E next() {
            if(hasNext())
                return lastReturned.next.item;
            else throw new NoSuchElementException();
        }

    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public boolean isEmpty() {
        return this.size == 0;
    }

    @Override
    public boolean contains(Object o) {
        if(o==null)
            throw new NullPointerException();
        boolean contains=false;//flag
        Node<E> index = this.head;//creo un nodo inserendoci il primo elemento della lista (per posizionarmi)
        while (index != null && !contains)
        {//mentre il nodo non è nullo (ha successivi) e allo stesso tempo il flag contains è false, ciclo
            contains=o.equals(index.item);//contains assume il risultato booleano comparando l'oggetto passato con l'item di index (si presuppone che l'oggetto passato sia un item comparabile)
            index=index.next;//scorro l'indice(nodo) successivo
        }
        return contains;
    }

    @Override
    public boolean add(E e) {//aggiungi infondo alla lista
        if (e==null)
            throw new NullPointerException();
        tail.next=new Node(e,null);
        size++;
        numeroModifiche++;
        return true;
    }

    @Override
    public boolean remove(Object o) {//rimuove il primo trovato senza intaccare eventuali duplicati
        if(o==null)
            throw new NullPointerException();

        if(!contains(o) || size==0)
        {
            return false;
        }

        if (head.item.equals(o))
        {
            head = head.next;//il next diventa direttamente il prossimo
            size--;
            numeroModifiche++;
            return true;
        }

        Node<E> node = this.head;//creo un nodo di comodo inserendo il primo valore della listalinked
        Node<E> nodeNext = this.head.next;//prendo il nodo dopo di head per confrontarlo

        while(nodeNext.next!=null) {//intanto che il nodo n ha un successivo, cicla
            if(nodeNext.item.equals(o))//se il nodo dopo il mio corrente, ha l'item uguale all'oggetto
            {
                node.next = nodeNext.next;//il riferimento del nodo corrente, diventerà il nodo dopo di nodeNext
                nodeNext.next=null;//rimuovo riferimento del nodo successivo, in modo che lo tolgo dalla lista
                size--;//modifico le eventuali variabili
                numeroModifiche++;
                return true;
            }
            node=node.next;
            nodeNext=nodeNext.next;
        }
        return false;
    }

    @Override
    public void clear() {
        Node<E> node = this.head;
        while(node.next!=null)
        {
            node.next=null;
            node=node.next;
            size=0;
            numeroModifiche++;
        }
    }

    @Override
    public E get(int index) {
        if(index<0 || index>=size)
            throw new IndexOutOfBoundsException();
        Node<E> node = this.head;
        for(int i=0;i!=index;i++)
        {
            node=node.next;
        }
        return node.item;
    }

    @Override
    public E set(int index, E element) {
        if(index<0 || index>=size)
            throw new IndexOutOfBoundsException();
        if(element==null)
            throw new NullPointerException();
        Node<E> node = this.head;
        for(int i=0;i!=index;i++)
        {
            node=node.next;
        }
        numeroModifiche++;
        return node.item=element;
    }

    @Override
    public void add(int index, E element) {
        if(index<0 || index>=size)
            throw new IndexOutOfBoundsException();
        if(element==null)
            throw new NullPointerException();
        Node<E> node = this.head;//creo un nodo di comodo inserendo il primo valore della listalinked
        Node<E> nodeNext = this.head.next;//prendo il nodo dopo di head per confrontarlo
        int i=0;

        while(index==i)
        {//ciclo finchè non arrivo all'indice passato, scorrendo tutti i riferimenti dei nodi che mi servono
            i++;
            node.next = nodeNext.next;//il riferimento del nodo corrente, diventerà il nodo dopo di nodeNext etc
            nodeNext.next = nodeNext.next.next;
        }

        Node<E> toAdd = new Node<>(element,nodeNext);
        node.next=toAdd;
        size++;
        numeroModifiche++;

    }


    @Override
    public E remove(int index) {
        if(index<0 || index>=size)
            throw new IndexOutOfBoundsException();

        Node<E> node = this.head;//creo un nodo di comodo inserendo il primo valore della listalinked
        Node<E> nodeNext = this.head.next;//prendo il nodo dopo di head per confrontarlo
        int i=0;

        while(index==i)
        {//ciclo finchè non arrivo all'indice passato, scorrendo tutti i riferimenti dei nodi che mi servono
            i++;
            node.next = nodeNext.next;//il riferimento del nodo corrente, diventerà il nodo dopo di nodeNext etc
            nodeNext.next = nodeNext.next.next;
        }
        node.next=nodeNext.next;
        nodeNext.next=null;
        size--;
        numeroModifiche++;
        return nodeNext.item;
    }

    @Override
    public int indexOf(Object o) {
        if(o==null)
            throw new NullPointerException();
        int index=-1;
        Node<E> node = this.head;
        //if (node.item.equals(o)) return 0;//oppure index
        while(!node.item.equals(o))//finche l'item del nodo non è uguale all'oggetto passato, incrementa index a ogni passo e prendi il prossimo nodo
        {
            index++;
            node=node.next;
        }
        return index;
    }

    @Override
    public int lastIndexOf(Object o) {
        if(o==null)
            throw new NullPointerException();
        int index=-1;
        int indexReq=0;
        Node<E> node = this.head;
        //if (node.item.equals(o)) return 0;//oppure index
        while(node.next!=null)//finche l'item del nodo non è uguale all'oggetto passato, incrementa index a ogni passo e prendi il prossimo nodo
        {
            if(node.item.equals(o))
            {
                indexReq=index;
            }
            index++;
            node=node.next;
        }
        return indexReq;
    }
    
    @Override
    public Object[] toArray() {
        Object[] array=new Object[size+1];
        Node<E> node = this.head;
        int i=0;

        while(node.next!=null)
        {
            array[i]=node;
            i++;
        }
        return array;
    }
    
    @Override
    public Iterator<E> iterator() {
        return new Itr();
    }

    @Override
    public ListIterator<E> listIterator() {
        throw new UnsupportedOperationException("Operazione non supportata.");
    }

    @Override
    public ListIterator<E> listIterator(int index) {
        throw new UnsupportedOperationException("Operazione non supportata.");
    }

    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        throw new UnsupportedOperationException("Operazione non supportata.");
    }

    @Override
    public <T> T[] toArray(T[] a) {
        throw new UnsupportedOperationException("Operazione non supportata.");
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        throw new UnsupportedOperationException("Operazione non supportata.");
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        throw new UnsupportedOperationException("Operazione non supportata.");
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        throw new UnsupportedOperationException("Operazione non supportata.");
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        throw new UnsupportedOperationException("Operazione non supportata.");
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException("Operazione non supportata.");
    }
}
