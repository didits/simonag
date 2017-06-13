package com.simonag.simonag;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.simonag.simonag.Model.Dashboard;
import com.simonag.simonag.Model.Program;

import java.util.ArrayList;

/**
 * Created by diditsepiyanto on 6/13/17.
 */

public class ProgramActivity extends AppCompatActivity {
    public static final String EXTRA_NAME = "id_bumn";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.program);
        RecyclerView rv = (RecyclerView) findViewById(R.id.list_program);
        setupRecyclerView(rv);
    }

    private void setupRecyclerView(RecyclerView recyclerView) {
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        recyclerView.setAdapter(new SimpleStringRecyclerViewAdapter(this,
                getRandomSublist()));
    }

    private ArrayList<Program> getRandomSublist() {
        ArrayList<Program> list = new ArrayList<>();
        list.add(new Program(0, "Pertamina"));
        list.add(new Program(1, "Pertamina"));
        list.add(new Program(2, "Pertamina"));
        list.add(new Program(3, "Pertamina"));
        return list;
    }

    public static class SimpleStringRecyclerViewAdapter
            extends RecyclerView.Adapter<DashboardFragment.SimpleStringRecyclerViewAdapter.ViewHolder> {

        private final TypedValue mTypedValue = new TypedValue();
        private int mBackground;
        private ArrayList<Program> mValues;

        public static class ViewHolder extends RecyclerView.ViewHolder {
            public ArrayList<Dashboard>  mBoundString;

            public final View mView;
            public final ImageView mImageView;
            public final TextView mTextView;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                mImageView = (ImageView) view.findViewById(R.id.avatar);
                mTextView = (TextView) view.findViewById(android.R.id.text1);
            }

            @Override
            public String toString() {
                return super.toString() + " '" + mTextView.getText();
            }
        }

        public SimpleStringRecyclerViewAdapter(Context context, ArrayList<Program> items) {
            context.getTheme().resolveAttribute(R.attr.selectableItemBackground, mTypedValue, true);
            mBackground = mTypedValue.resourceId;
            mValues = items;
        }

        @Override
        public DashboardFragment.SimpleStringRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.adapter_nama_program, parent, false);
            view.setBackgroundResource(mBackground);
            return new DashboardFragment.SimpleStringRecyclerViewAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final DashboardFragment.SimpleStringRecyclerViewAdapter.ViewHolder holder, int position) {
            //holder.mBoundString = mValues.get(position);
            //holder.mTextView.setText(mValues.get(position).getNama_bumn());

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Context context = v.getContext();
                    Intent intent = new Intent(context, ProgramActivity.class);
                    intent.putExtra(ProgramActivity.EXTRA_NAME, holder.mBoundString);

                    context.startActivity(intent);
                }
            });
            /*
            Glide.with(holder.mImageView.getContext())
                    .load(Cheeses.getRandomCheeseDrawable())
                    .fitCenter()
                    .into(holder.mImageView);*/
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }
    }
}
