package com.simonag.simonag;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.daimajia.numberprogressbar.NumberProgressBar;
import com.simonag.simonag.model.Dashboard;
import com.simonag.simonag.utils.Config;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 */
public class DashboardKualitasFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        RecyclerView rv = (RecyclerView) inflater.inflate(
                R.layout.fragment_dashboard_list, container, false);
        setupRecyclerView(rv);
        return rv;
    }

    private void setupRecyclerView(RecyclerView recyclerView) {
        ArrayList<Dashboard> kualitas_sementara = ((MainActivity) getActivity()).db;
        ArrayList employees = new ArrayList();
        for (int i=0; i<kualitas_sementara.size();i++)
            employees.add(kualitas_sementara.get(i));
        Collections.sort(employees, new Comparator<Dashboard>() {
            @Override
            public int compare(Dashboard dashboard, Dashboard t1) {
                Integer id1 = (int)dashboard.getPersentase_kualitas();
                Integer id2 = (int)t1.getPersentase_kualitas();
                Log.d("databarang", id1+"|"+id2+"|"+id2.compareTo(id1));

                // ascending order
                //return id1.compareTo(id2);

                // descending order
                return id2.compareTo(id1);
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        recyclerView.setAdapter(new SimpleStringRecyclerViewAdapter(getActivity(), employees));

    }


    public static class SimpleStringRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleStringRecyclerViewAdapter.ViewHolder> {

        private final TypedValue mTypedValue = new TypedValue();
        private int mBackground;
        private ArrayList<Dashboard> mValues;
        Activity context;


        public static class ViewHolder extends RecyclerView.ViewHolder {
            public ArrayList<Dashboard> mBoundString;

            public final View mView;
            @BindView(R.id.avatar)
            ImageView avatar;
            @BindView(android.R.id.text1)
            TextView text1;
            @BindView(android.R.id.text2)
            TextView text2;
            @BindView(android.R.id.progress)
            NumberProgressBar progress;
            @BindView(R.id.progress_nilai)
            NumberProgressBar progress_nilai;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                ButterKnife.bind(this, view);
            }

            @Override
            public String toString() {
                return super.toString() + " '" + text1.getText();
            }
        }

        public SimpleStringRecyclerViewAdapter(Activity context, ArrayList<Dashboard> items) {
            context.getTheme().resolveAttribute(R.attr.selectableItemBackground, mTypedValue, true);
            mBackground = mTypedValue.resourceId;
            mValues = items;
            this.context = context;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.adapter_dashboard, parent, false);
            view.setBackgroundResource(mBackground);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder,final int position) {
            holder.text1.setText(mValues.get(position).getNama_bumn());
            holder.text2.setText(mValues.get(position).getPersentase_komersial() + " %");
            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Context context = v.getContext();
                    context.startActivity(new Intent(context, ProgramActivity.class));
                }
            });
            holder.progress.setProgressTextVisibility(NumberProgressBar.ProgressTextVisibility.Invisible);
            holder.progress.setProgress((int) mValues.get(position).getPersentase_kualitas());
            if((int) mValues.get(position).getPersentase_kualitas()<0)
                holder.progress_nilai.setProgressTextVisibility(NumberProgressBar.ProgressTextVisibility.Invisible);
            holder.progress_nilai.setProgress((int) mValues.get(position).getPersentase_kualitas());
            holder.setIsRecyclable(false);

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Context context = v.getContext();
                    Intent i = new Intent(context, ProgramActivity.class);
                    i.putExtra("KEY", "" + mValues.get(position).getId_bumn());
                    i.putExtra("NAMA_PERUSAHAAN", "" + mValues.get(position).getNama_bumn());
                    i.putExtra("GAMBAR_PERUSAHAAN", "" + mValues.get(position).getLink_gambar());
                    context.startActivity(i);
                }
            });
            String url = Config.URL_GAMBAR + mValues.get(position).getLink_gambar();

            Glide.with(holder.avatar.getContext())
                    .load(url)
                    .fitCenter()
                    .into(holder.avatar);
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }
    }

}
