package com.gmail.alexejkrawez.site_scrapper;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.Hyperlink;


public class TableRow {
    private final SimpleBooleanProperty checkbox = new SimpleBooleanProperty();
    private final SimpleStringProperty name = new SimpleStringProperty();
    private final ObjectProperty<Hyperlink> url = new SimpleObjectProperty<>();


    public TableRow(boolean checkbox, String name, Hyperlink url) {
        this.checkbox.set(checkbox);
        this.name.set(name);
        this.url.set(url);
    }

    public final StringProperty nameProperty() {
        return this.name;
    }
    public final BooleanProperty checkboxProperty() {
        return this.checkbox;
    }
    public ObjectProperty<Hyperlink> urlProperty() {
        return this.url;
    }


    public final String getName() {
        return this.nameProperty().get();
    }

    public final void setName(final String name) {
        this.nameProperty().set(name);
    }


    public boolean isCheckbox() {
        return checkbox.get();
    }

    public void setCheckbox(boolean checkbox) {
        this.checkbox.set(checkbox);
    }


    public Hyperlink getUrl() {
        return url.get();
    }

    public void setUrl(Hyperlink url) {
        this.url.set(url);
    }

    @Override
    public String toString() {
        return getName();
    }

}