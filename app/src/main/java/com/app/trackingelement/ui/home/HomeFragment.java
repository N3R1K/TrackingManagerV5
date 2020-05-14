package com.app.trackingelement.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.trackingelement.Adapter.ElementAdpater;
import com.app.trackingelement.AdapterHandler;
import com.app.trackingelement.Database.Database;
import com.app.trackingelement.MainActivity;
import com.app.trackingelement.Model.ModelElement;
import com.app.trackingelement.R;
import com.app.trackingelement.ui.Add.AddFragment;

import java.util.ArrayList;

public class HomeFragment extends Fragment {


    RecyclerView rcylerView;
    ImageView imgEmpty;
    ArrayList<ModelElement> list=new ArrayList<>();
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);

        init(root);
        return root;
    }

    private void init(View root)
    {

        rcylerView=root.findViewById(R.id.rcylerView);
        imgEmpty=root.findViewById(R.id.imgEmpty);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        //mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rcylerView.setLayoutManager(mLayoutManager);
        rcylerView.setItemAnimator(new DefaultItemAnimator());



        list=new Database(getActivity()).getAllElements();
        if(list.size()<1)
        {
            imgEmpty.setVisibility(View.VISIBLE);
            return;
        }
        else
            imgEmpty.setVisibility(View.GONE);
        ElementAdpater adpater=new ElementAdpater(getActivity(),list);
        rcylerView.setAdapter(adpater);



        adpater.adapterhandler=new AdapterHandler()
        {
            @Override
            public void editTrackingElement(ModelElement modelElement)
            {


                MainActivity activity= (MainActivity) getActivity();
                Bundle bundle=new Bundle();
                bundle.putString("ID",modelElement.getID());
                bundle.putString("Title",modelElement.getTitle());
                bundle.putString("Unit",modelElement.getUnit());
                bundle.putString("Value",modelElement.getValue());
                bundle.putString("CSV",modelElement.getCSV());
                if (activity != null) {
                    activity.AddEdit(bundle);
                }
                super.editTrackingElement(modelElement);
            }

            @Override
            public void TrackDelete(ArrayList<ModelElement> modelElement) {
                if(modelElement.size()<1)
                {
                    imgEmpty.setVisibility(View.VISIBLE);
                }
                else
                    imgEmpty.setVisibility(View.GONE);
                super.TrackDelete(modelElement);
            }
        };



    }
}
