package com.simonag.simonag.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.iconics.IconicsDrawable;
import com.simonag.simonag.R;
import com.simonag.simonag.model.ProgramModel;
import com.simonag.simonag.utils.EditHapusInterface;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Ilham Aulia Majid on 07-May-17.
 */

public class DataProgramAdapter extends RecyclerView.Adapter<DataProgramAdapter.ViewHolder> {

    Context context;
    List<ProgramModel> programModels;
    EditHapusInterface listener;

    public DataProgramAdapter(Context context, List<ProgramModel> programModels, EditHapusInterface listener) {
        this.programModels = programModels;
        this.context = context;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_program, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final ProgramModel program = programModels.get(position);
        holder.tvNama.setText(program.nama);
        holder.tvNama.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent i = new Intent(context, DataAktivitasActivity.class);
//                i.putExtra("nama",program.nama);
//                i.putExtra("id",program.id);
//                context.startActivity(i);
            }
        });
        holder.btnEdit.setImageDrawable(new IconicsDrawable(context)
                .icon(FontAwesome.Icon.faw_pencil_square)
                .color(Color.WHITE));
        holder.btnHapus.setImageDrawable(new IconicsDrawable(context)
                .icon(FontAwesome.Icon.faw_trash)
                .color(Color.WHITE));
        holder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.edit(position);
            }
        });
        holder.btnHapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.hapus(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return programModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_nama)
        TextView tvNama;
        @BindView(R.id.cv_program)
        CardView cvProgram;
        @BindView(R.id.btn_edit)
        ImageView btnEdit;
        @BindView(R.id.btn_hapus)
        ImageView btnHapus;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
