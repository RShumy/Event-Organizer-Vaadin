package com.eventorganizr.organizr.views.eventView;

import com.eventorganizr.organizr.entity.Event;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dependency.JavaScript;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.html.Div;

import com.vaadin.flow.component.html.IFrame;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.dom.Element;

@JsModule("./map-view-partial.ts")
public class MapView extends VerticalLayout {
    public Div mapView = new Div();

    MapView(){
        this.setPadding(false);
        mapView.setId("myMap");
        mapView.setSizeFull();
        if (mapView.isVisible())

        mapView.getElement().getStyle().set("border-radius","10px");
        mapView.getElement().getStyle().set("border", "5px solid white");
        add(mapView);
    };

    public void showMap(String locationQuery){
        if (!locationQuery.isEmpty()) UI.getCurrent().getPage().executeJs("ShowMap($0)", locationQuery);
    }
}
