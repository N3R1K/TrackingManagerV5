package com.app.trackingelement;

import android.graphics.Color;
import android.widget.GridView;
import android.widget.ListView;

import com.app.trackingelement.Model.ModelElement;

import java.util.ArrayList;

public abstract class AdapterHandler
{
    public void editTrackingElement(ModelElement modelElement) {}
    public void TrackDelete(ArrayList<ModelElement> modelElement) {}
}
