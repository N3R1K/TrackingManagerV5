package com.app.trackingelement.Adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.app.trackingelement.AdapterHandler;
import com.app.trackingelement.Database.Database;
import com.app.trackingelement.Model.ModelElement;
import com.app.trackingelement.R;


import java.util.ArrayList;

public class ElementAdpater extends RecyclerView.Adapter<ElementAdpater.ViewHolder> {

    public ArrayList<ModelElement> mData;
    public AdapterHandler adapterhandler;
    private LayoutInflater mInflater;
    private  Context mContext;
    Database DBObj;

    // data is passed into the constructor
    public ElementAdpater(Context context, ArrayList<ModelElement> data) {
        this.mInflater = LayoutInflater.from(context);
        mContext=context;
        this.mData = data;
        DBObj=new Database(mContext);
    }

    // inflates the cell layout from xml when needed
    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.lyt_element, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each cell
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position)
    {

        final ModelElement modelElement = mData.get(position);


        holder.txtTitle.setText(modelElement.getTitle());
        holder.txtValue.setText(modelElement.getValue()+ " "+modelElement.getUnit());
        holder.txtCSV.setText(modelElement.getCSV()+ " "+modelElement.getUnit());

        View.OnClickListener listener=new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                double v1 = Double.parseDouble(modelElement.getCSV());
                double v2 = Double.parseDouble(modelElement.getValue());



                if(v.getId()==R.id.btnPlus)
                {
                    double v3 = v2 + v1;
                    modelElement.setValue(String.valueOf(v3));
                    holder.txtValue.setText(modelElement.getValue()+ " "+modelElement.getUnit());

                }
                else
                {
                    double v3 = v2 - v1;
                    modelElement.setValue(String.valueOf(v3));
                    holder.txtValue.setText(modelElement.getValue()+ " "+modelElement.getUnit());
                }
                DBObj.editTrackingElement(modelElement,modelElement.getID());
            }
        };





        holder.btnPlus.setOnClickListener(listener);
        holder.btnMinus.setOnClickListener(listener);


        holder.crdParent.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v)
            {


                showDialog(modelElement,position);
                return false;
            }
        });
    }


    /*

        Prompt A Dialog To Ask If User Wants To Delete
     */
    public  void showDialog(final ModelElement element, final int Position)
    {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(mContext);
        View child = getActivity().getLayoutInflater().inflate(R.layout.lyt_dialog_deleteedit, null);

        //

                Button btnEdit=child.findViewById(R.id.btnEdit);
                Button btnDelete=child.findViewById(R.id.btnDelete);


        ///


        builder1.setView(child);
        builder1.setCancelable(true);
        final AlertDialog dialog = builder1.create();
        dialog.show();




        View.OnClickListener listener=new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                dialog.dismiss();
                if(v.getId()==R.id.btnEdit)
                {
                    adapterhandler.editTrackingElement(element);
                }
                else
                if (v.getId()==R.id.btnDelete)
                {

                    boolean Result = new Database(mContext).deleteTrackingElement(element.getID());

                    if(Result)
                    {

                        Toast.makeText(getActivity(), R.string.successdeleteitem,Toast.LENGTH_SHORT).show();
                        mData.remove(Position);
                        notifyDataSetChanged();
                        adapterhandler.TrackDelete(mData);

                    }
                    else
                    {
                        Toast.makeText(getActivity(), R.string.failedtodeleteitem,Toast.LENGTH_SHORT).show();
                    }

                }

            }
        };

        btnEdit.setOnClickListener(listener);
        btnDelete.setOnClickListener(listener);



    }
    // total number of cells
    @Override
    public int getItemCount() {
        return mData.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder  {


        Button btnPlus;
        Button btnMinus;
        TextView txtTitle;
        TextView txtValue;
        TextView txtCSV;

        CardView crdParent;

        ViewHolder(View itemView) {
            super(itemView);
            btnPlus=itemView.findViewById(R.id.btnPlus);
            btnMinus = itemView.findViewById(R.id.btnMinus);
            txtTitle = itemView.findViewById(R.id.txtTitle);
            txtValue = itemView.findViewById(R.id.txtValue);
            txtCSV=itemView.findViewById(R.id.txtSCV);
            crdParent=itemView.findViewById(R.id.crdParent);

        }

    }



    public  Activity getActivity()
    {
        return (Activity) mContext;
    }

}