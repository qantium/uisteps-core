package com.qantium.uisteps.core.browser.pages.elements;

import com.qantium.uisteps.core.browser.Browser;

/**
 * Created by Solan on 25.10.2016.
 */
public class MyTable extends Table<MyTable.MyRow> {


    public MyTable() {
        super(MyTable.MyRow.class);
    }

    public static class MyRow extends Table.Row<MyCell> {

        public MyRow() {
            super(MyCell.class);
        }

        public String getRowName() {
            return "Solyankin";
        }
    }

    public static class MyCell extends TextField {

        public String getCellName() {
            return "Anton";
        }
    }


    public static void main(String[] args) {
        new Browser().get(MyTable.class).getRows().get(0).getRowName();
    }

}
