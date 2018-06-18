package com.edwinbustamante.gruposcochalos.VisitanteArchivos;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.Image;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.edwinbustamante.gruposcochalos.ImagenFull.FulImagenVisitante;
import com.edwinbustamante.gruposcochalos.Objetos.Constantes;
import com.edwinbustamante.gruposcochalos.R;
import com.edwinbustamante.gruposcochalos.VisitarMapa.MapsActivityVisitar;
import com.edwinbustamante.gruposcochalos.domain.GrupoMusical;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

public class InformacionGrupoVisitante extends AppCompatActivity implements View.OnClickListener {
    private ImageView imageViewPortada, imageViewPerfil, movil1, movil2, movilwhatsapp, facebook, ubicacion;
    private TextView nombreGrupo, generoGrupo, informacionVisitante, movil1text, movil2text, movilwhatsapptext, linkfacebook, contactosextra, direcciontext;
    private String fotoperfil, latitudg, longitudg,fotoportada;
    private TextView numeroPublic;
    GrupoMusical grupoMusical;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informacion_grupo_visitante);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Información de Grupo Musical");
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);
        grupoMusical = getIntent().getExtras().getParcelable("grupomusical");
        imageViewPortada = (ImageView) findViewById(R.id.portada_visitante);
        imageViewPortada.setOnClickListener(this);
        imageViewPerfil = (ImageView) findViewById(R.id.perfil_visitante);
        imageViewPerfil.setOnClickListener(this);
        nombreGrupo = (TextView) findViewById(R.id.nombregrupo_visitante);
        generoGrupo = (TextView) findViewById(R.id.texgeneroMusica_visitante);
        fotoperfil = grupoMusical.getFotoperfil();
        fotoportada=grupoMusical.getFotoportada();

        Picasso.get().load(Constantes.IP_SERVIDOR + "gruposcochalos/" + fotoperfil).error(R.drawable.perfilmusic)
                .placeholder(R.drawable.progress_animation).into(imageViewPerfil);
        Picasso.get().load(Constantes.IP_SERVIDOR + "gruposcochalos/" + fotoportada).error(R.drawable.perfilmusic)
                .placeholder(R.drawable.progress_animation).into(imageViewPortada);

        nombreGrupo.setText(grupoMusical.getNombre());
        generoGrupo.setText(grupoMusical.getGenero());
        informacionVisitante = (TextView) findViewById(R.id.texViewInformacion_visitante);
        informacionVisitante.setText(grupoMusical.getInformaciondescripcion());
        movil1 = (ImageView) findViewById(R.id.imageViewMovil1_visitante);
        movil1.setOnClickListener(this);
        movil1text = (TextView) findViewById(R.id.texViewMovil1_visitante);
        movil1text.setText(grupoMusical.getNumtelefono());
        movil2 = (ImageView) findViewById(R.id.imageViewMovil2_visitante);
        movil2.setOnClickListener(this);
        movil2text = (TextView) findViewById(R.id.texViewMovil2_visitante);
        movil2text.setText(grupoMusical.getNumtelefonodos());
        movilwhatsapp = (ImageView) findViewById(R.id.imageViewMovilWhatsapp_visitante);
        movilwhatsapp.setOnClickListener(this);
        movilwhatsapptext = (TextView) findViewById(R.id.texViewMovilWhatsAp_visitante);
        movilwhatsapptext.setText(grupoMusical.getNumwhatsapp());
        facebook = (ImageView) findViewById(R.id.ImageViewFacebook_visitante);
        facebook.setOnClickListener(this);
        linkfacebook = (TextView) findViewById(R.id.texViewLinkFacebook_visitante);
        linkfacebook.setText(grupoMusical.getLinkfacebook());
        contactosextra = (TextView) findViewById(R.id.texViewContactos_visitante);
        contactosextra.setText(grupoMusical.getDescripcioncontactos());
        direcciontext = (TextView) findViewById(R.id.texViewDireccion_visitante);
        direcciontext.setText(grupoMusical.getDirecciondescripcion());
        ubicacion = (ImageView) findViewById(R.id.ubicar_visitante);
        ubicacion.setOnClickListener(this);
        numeroPublic = (TextView) findViewById(R.id.numerodepublic);
        numeroPublic.setOnClickListener(this);
        latitudg = grupoMusical.getLatitudg();
        longitudg = grupoMusical.getLongitudg();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        // getMenuInflater().inflate(R.menu.guardar_contrasenia, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        switch (item.getItemId()) {

            case android.R.id.home:
                finish();
                break;
            default:
                break;
        }

        return true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.portada_visitante:
                Intent i = new Intent(this, FulImagenVisitante.class);
                i.putExtra("nombretoolbar","Portada");
                i.putExtra("fotoperfil", fotoportada);
                startActivity(i);

                //Toast.makeText(this, fotoperfil, Toast.LENGTH_SHORT).show();
                break;
            case R.id.perfil_visitante:
                Intent in = new Intent(this, FulImagenVisitante.class);
                in.putExtra("nombretoolbar","Perfil");
                in.putExtra("fotoperfil", fotoperfil);
                startActivity(in);
                //Toast.makeText(this, "perfil", Toast.LENGTH_SHORT).show();
                break;
            case R.id.numerodepublic:
                Intent publicacionesvisitar = new Intent(InformacionGrupoVisitante.this, PublicacionesGrupoVisitante.class);
               publicacionesvisitar.putExtra("idgrupomusical",grupoMusical.getIdgrupomusical());
                startActivity(publicacionesvisitar);
                break;
            case R.id.imageViewMovil1_visitante:
                // Toast.makeText(this, "movil 1", Toast.LENGTH_SHORT).show();
                llamar(movil1text.getText().toString());
                break;
            case R.id.imageViewMovil2_visitante:
                // Toast.makeText(this, "movil 2", Toast.LENGTH_SHORT).show();
                llamar(movil2text.getText().toString());
                break;
            case R.id.imageViewMovilWhatsapp_visitante:
                // Toast.makeText(this, "whatsapp", Toast.LENGTH_SHORT).show();
                try {
                    // Intent launchIntent =getContext().getPackageManager().getLaunchIntentForPackage("com.whatsapp");
                    //startActivity(launchIntent);

                    Uri uri = Uri.parse("smsto:" + movilwhatsapptext.getText().toString());
                    Intent ir = new Intent(Intent.ACTION_SENDTO, uri);
                    if (estaInstaladaAplicacion("com.whatsapp", InformacionGrupoVisitante.this)) {
                        ir.setPackage("com.whatsapp");
                        startActivity(ir);
                    } else {
                        ir.setPackage("com.gbwhatsapp");
                        startActivity(ir);
                    }
                } catch (Exception e) {
                    Toast.makeText(this, "Por favor instale WhatsApp Oficial.." + e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.ImageViewFacebook_visitante:
                //String facebookId = "fb.//page/<Faceboook Page ID>";
                String facebookId="2207520000.1528940297";
                String urlPage = linkfacebook.getText().toString();
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(facebookId)));
                } catch (Exception e) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(urlPage)));
                }
                break;
            case R.id.ubicar_visitante:

                if (latitudg.equals("no")) {
                    Toast.makeText(InformacionGrupoVisitante.this, "El grupo " + nombreGrupo.getText().toString() + " no añadio ubicación", Toast.LENGTH_SHORT).show();
                } else {

                    Intent mapsvisitar = new Intent(InformacionGrupoVisitante.this, MapsActivityVisitar.class);
                    mapsvisitar.putExtra("latitud", latitudg);
                    mapsvisitar.putExtra("longitud", longitudg);
                    mapsvisitar.putExtra("titulomarcador", nombreGrupo.getText().toString());
                    startActivity(mapsvisitar);
                }

                break;
            default:
                break;
        }

    }
    private boolean estaInstaladaAplicacion(String nombrePaquete, Context contexto) {
        PackageManager pm = contexto.getPackageManager();
        try {
            pm.getPackageInfo(nombrePaquete, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void llamar(String numero) {
        Intent i = new Intent(Intent.ACTION_DIAL);
        if (numero.trim().isEmpty()) {
            i.setData(Uri.parse("tel:000000000"));
        } else {
            i.setData(Uri.parse("tel:" + numero));
        }
        startActivity(i);
    }
}