package com.qantium.uisteps.core.utils.grid.servlets.robot;

import java.io.Serializable;
import java.util.*;
import java.util.function.UnaryOperator;

/**
 * Created Anton Solyankin
 */
public class RobotActions implements Serializable, Runnable, List<RobotAction> {

    private final List<RobotAction> actions;
    private List<Object> results;

    public RobotActions(List<RobotAction> actions) {
        this.actions = actions;
    }

    public RobotActions() {
        actions = new ArrayList();
    }

    @Override
    public void run() {
        results = new ArrayList();
        for (RobotAction action : actions) {
            action.setResults(results);
            action.run();
        }
    }

    public List<Object> getResults() {
        return results;
    }

    @Override
    public int size() {
        return actions.size();
    }

    @Override
    public boolean isEmpty() {
        return actions.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return actions.contains(o);
    }

    @Override
    public Iterator<RobotAction> iterator() {
        return actions.iterator();
    }

    @Override
    public Object[] toArray() {
        return actions.toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return actions.toArray(a);
    }

    @Override
    public boolean add(RobotAction action) {
        return actions.add(action);
    }

    @Override
    public boolean remove(Object o) {
        return actions.remove(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return actions.containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends RobotAction> c) {
        return actions.addAll(c);
    }

    @Override
    public boolean addAll(int index, Collection<? extends RobotAction> c) {
        return actions.addAll(index, c);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return actions.removeAll(c);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return actions.retainAll(c);
    }

    @Override
    public void replaceAll(UnaryOperator<RobotAction> operator) {
        actions.replaceAll(operator);
    }

    @Override
    public void sort(Comparator<? super RobotAction> c) {
        actions.sort(c);
    }

    @Override
    public void clear() {
        actions.clear();
    }

    @Override
    public boolean equals(Object o) {
        return actions.equals(o);
    }

    @Override
    public int hashCode() {
        return actions.hashCode();
    }

    @Override
    public RobotAction get(int index) {
        return actions.get(index);
    }

    @Override
    public RobotAction set(int index, RobotAction element) {
        return actions.set(index, element);
    }

    @Override
    public void add(int index, RobotAction element) {
        actions.add(index, element);
    }

    @Override
    public RobotAction remove(int index) {
        return actions.remove(index);
    }

    @Override
    public int indexOf(Object o) {
        return actions.indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o) {
        return actions.lastIndexOf(o);
    }

    @Override
    public ListIterator<RobotAction> listIterator() {
        return actions.listIterator();
    }

    @Override
    public ListIterator<RobotAction> listIterator(int index) {
        return actions.listIterator(index);
    }

    @Override
    public List<RobotAction> subList(int fromIndex, int toIndex) {
        return actions.subList(fromIndex, toIndex);
    }

    @Override
    public Spliterator<RobotAction> spliterator() {
        return actions.spliterator();
    }
}
