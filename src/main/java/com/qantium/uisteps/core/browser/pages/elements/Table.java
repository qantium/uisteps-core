package com.qantium.uisteps.core.browser.pages.elements;

import com.qantium.uisteps.core.browser.pages.UIElement;
import org.openqa.selenium.By;

/**
 * Created by Anton Solyankin
 */
public class Table<R extends Table.Row> extends UIElement {

    private final Class<R> row;
    private By cellLocator;
    private By rowLocator;


    public Table(Class<R> row) {
        this.row = row;
    }

    public Table<R> setCellLocator(By cellLocator) {
        this.cellLocator = cellLocator;
        return this;
    }

    public Table<R> setRowLocator(By rowLocator) {
        this.rowLocator = rowLocator;
        return this;
    }

    @Override
    public String getValue() {
        return getRows().getText();
    }

    public UIElements<R> getRows() {
        UIElements<R> rows;

        if(rowLocator != null) {
            rows = getAll(row, rowLocator);
        } else {
            rows = getAll(row);
        }

        if(cellLocator != null) {
            for(R row: rows) {
                row.setCellLocator(cellLocator);
            }
        }

        return rows;
    }

    public static class Row<C extends UIElement> extends UIElement {

        private final Class<C> cell;
        private By cellLocator;

        public Row(Class<C> cell) {
            this.cell = cell;
        }

        public Row<C> setCellLocator(By cellLocator) {
            this.cellLocator = cellLocator;
            return this;
        }

        public UIElements<C> getCells() {
            if(cellLocator != null) {
                return getAll(cell, cellLocator);
            } else {
                return getAll(cell);
            }

        }
    }
}
