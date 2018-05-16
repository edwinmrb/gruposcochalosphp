package com.edwinbustamante.gruposcochalos.CuentaUsuarioArchivos;

import android.content.Context;
import android.graphics.Point;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.edwinbustamante.gruposcochalos.Objetos.Publicacion;
import com.edwinbustamante.gruposcochalos.R;

import java.util.List;

/**
 * Created by EDWIN on 8/5/2018.
 */

public class MyAdapterPublicar extends RecyclerView.Adapter<MyAdapterPublicar.ViewHolder> {


    public List<Publicacion> publicacionLista;//lista de todos los grupos

    public MyAdapterPublicar(List<Publicacion> publicacionLista) {
        this.publicacionLista = publicacionLista;//Aqui paso la lista que stoy recibiendo
    }

    //encargado de inflar un nuevo item para la lista
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // aqui es donde se esta haciendo llamado a  la tarjeta que vamos a  cargar
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tarjeta_publicacion, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        //modificaciones del cntenido de cada item...despues de recibir del ViewHolder
        holder.fotoPerfilPublicacion.setImageResource(publicacionLista.get(position).getFotoPerfil());
        holder.nombreGrupoPublicacion.setText(publicacionLista.get(position).getNombreGrupo());
        holder.fechaPublicacion.setText(publicacionLista.get(position).getFechaPublicacion());
        holder.descripcionPublicacion.setText(publicacionLista.get(position).getDescripcionPublicacion());
        holder.fotoPublicacion.setImageResource(publicacionLista.get(position).getFotoPublicacion());

    }

    @Override
    public int getItemCount() {

        return publicacionLista.size();
    }

    /**
     * LA INER CLASE ESTA ABAJO QUE ENCARGA DE DIBUJAR
     */
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private ImageView fotoPerfilPublicacion, fotoPublicacion;
        private TextView nombreGrupoPublicacion, fechaPublicacion, descripcionPublicacion;


        public ViewHolder(View itemView) {
            super(itemView);
            fotoPerfilPublicacion = (ImageView) itemView.findViewById(R.id.fotoPerfilPublicacion);
            nombreGrupoPublicacion = (TextView) itemView.findViewById(R.id.textNombreGrupoPublicacion);
            fechaPublicacion = (TextView) itemView.findViewById(R.id.fechaPublicacion);
            descripcionPublicacion = (TextView) itemView.findViewById(R.id.textViewDescripcionPublicacion);
            fotoPublicacion = (ImageView) itemView.findViewById(R.id.imageViewPublicacion);
           /*
            Display display = getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            int width = size.x;
            int height = size.y;
            fotoPublicacion.setMaxHeight(height);
            fotoPublicacion.setMaxWidth(width);

         */
           itemView.setOnClickListener(this);// esto es a todo

        }


        @Override
        public void onClick(View view) {
            Toast.makeText(view.getContext(), "se presiono", Toast.LENGTH_SHORT).show();
        }
    }
}