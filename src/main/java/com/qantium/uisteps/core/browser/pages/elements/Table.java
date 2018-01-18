package com.qantium.uisteps.core.browser.pages.elements;

import com.qantium.uisteps.core.browser.NotInit;
import com.qantium.uisteps.core.browser.pages.UIElement;
import org.openqa.selenium.By;
import org.openqa.selenium.support.FindBy;

import java.lang.annotation.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Anton Solyankin
 */
@NotInit
@FindBy(tagName = "table")
@Group.Elements(@FindBy(tagName = "tr"))
@Table.Header(@FindBy(css = "tr th"))
public class Table<E extends Table.Row> extends Group<E> {

    private boolean headerIsFirstRow;
    private boolean headerIsNotInit = true;
    private By[] headerLocator;

    public Table(Class<E> cellType) throws IllegalArgumentException {
        super(cellType);
    }

    public boolean headerIsFirstRow() {
        return headerIsFirstRow;
    }

    public boolean headerIsNotInit() {
        return headerIsNotInit;
    }

    public Table<E> headerIsFirstRow(boolean headerIsFirstRow) {
        this.headerIsFirstRow = headerIsFirstRow;
        headerIsNotInit = false;
        return this;
    }

    public By[] getHeaderLocator() {
        return headerLocator;
    }

    public Table<E> withHeaderLocator(By[] headerLocator) {
        this.headerLocator = headerLocator;
        headerIsNotInit = false;
        return this;
    }

    public E getHeader() {
        if (headerIsFirstRow) {
            return getFirst();
        } else {
            return get(getElementType(), headerLocator);
        }
    }

    public LinkedList<UIElement> getColumn(String name) {
        if (!headerIsFirstRow) {
            throw new IllegalArgumentException("There is no header in table " + this + ". Set headerIsFirstRow = true");
        }

        int index = getIndexOf(name);
        return getColumn(index);
    }

    public LinkedList<UIElement> getColumn(int index) {

        LinkedList<UIElement> column = new LinkedList<>();
        stream().forEach(row -> {column.add(row.get(index));
        });
        if (headerIsFirstRow) {

            column.remove(0);
        }
        return column;
    }

    public int getIndexOf(String name) {
        if (!headerIsFirstRow) {
            throw new IllegalArgumentException("There is no header in table " + this + ". Set headerIsFirstRow = true");
        }

        Iterator header = getHeader().iterator();
        int index = -1;
        boolean notFound = true;
        while (header.hasNext()) {
            index++;
            if(((UIElement) header.next()).getText().equals(name)) {
                notFound = false;
                break;
            }
        }
        if(notFound) {
            throw new IllegalArgumentException("Table " + this + " doesn't contain header \"" + name + "\"");
        }
        return index;
    }


    @Group.Elements(@FindBy(tagName = "tr"))
    public static class Row<T extends UIElement> extends Group<T> {

        public Row(Class<T> elementType) throws IllegalArgumentException {
            super(elementType);
        }

        public T getByHeader(String name) {
            Table table = (Table) getContext();
            int index = table.getIndexOf(name);
            return get(index);
        }
    }

    @Override
    public <T extends UIElement> T as(Class<T> type) {
        T as = super.as(type);
        if (as instanceof Table) {
            Table table = (Table) as;
            table.headerIsFirstRow = headerIsFirstRow;
            table.headerIsNotInit = headerIsNotInit;
            table.headerLocator = headerLocator;
        }
        return as;
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.TYPE, ElementType.FIELD})
    @Inherited
    public @interface Header {
        FindBy[] value() default @FindBy();

    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.TYPE, ElementType.FIELD})
    @Inherited
    public @interface HeaderIsFirstRow {
        boolean value() default true;
    }
}

