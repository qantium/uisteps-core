package com.qantium.uisteps.core.browser.pages.elements;

import com.qantium.uisteps.core.browser.NotInit;
import com.qantium.uisteps.core.browser.pages.UIElement;
import org.openqa.selenium.By;
import org.openqa.selenium.support.FindBy;

import java.lang.annotation.*;

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
            return get(0);
        } else {
            return get(getElementType(), headerLocator);
        }
    }

    @Group.Elements(@FindBy(tagName = "td"))
    public static class Row<T extends UIElement> extends Group<T> {

        public Row(Class<T> elementType) throws IllegalArgumentException {
            super(elementType);
        }

        public T getByName(String name) {
            Table table = (Table) getContext();
            Row header = table.getHeader();
            int index = header.getIndexOf(name);
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

