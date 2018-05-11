package com.edwinbustamante.gruposcochalos.ImagenFull;


import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Point;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.edwinbustamante.gruposcochalos.Objetos.Constantes;

import com.edwinbustamante.gruposcochalos.R;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Hashtable;
import java.util.Map;

import uk.co.senab.photoview.PhotoViewAttacher;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.Manifest.permission_group.CAMERA;

public class FulImagen extends AppCompatActivity {


    private ProgressDialog progressDialog;
    private int CAMERA_REQUEST_CODE = 0;
    PhotoViewAttacher mAttacher;//Para hacer Zoom en el imagen
    private static String APP_DIRECTORY = "GruposCochalos/";
    private static String MEDIA_DIRECTORY = APP_DIRECTORY + "GruposCochalosImages";
    private final int MY_PERMISSIONS = 100;
    private final int PHOTO_CODE = 100;
    private final int SELECT_PICTURE = 200;
    String FileNameGrupo = "IdGrupo";
    private String UPLOAD_URL = Constantes.IP_SERVIDOR + "gruposcochalos/actualizarinformacion/actualizarfotoperfil.php?";
    private String KEY_IMAGEN = "fotoperfil";
    private String KEY_NOMBRE = "nombre";
    File fileImagen;
    Bitmap bitmap;

    private ImageView iconoPerfil;
    private Button mOptionButton;
    private RelativeLayout mRlView;
    private String mPath;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_ful_imagen);

        Bundle textoDireccion = this.getIntent().getExtras();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Foto de Perfil");
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(android.R.drawable.ic_menu_close_clear_cancel);

        iconoPerfil = (ImageView) findViewById(R.id.imagenfull);
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;
        iconoPerfil.setMaxHeight(height);
        iconoPerfil.setMaxWidth(width);


        //hace que la imagen sea expansible
        mAttacher = new PhotoViewAttacher(iconoPerfil);

        // mOptionButton = (Button) findViewById(R.id.show_options_button);
        //mRlView = (RelativeLayout) findViewById(R.id.rl_view);


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_full_image, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.edit_perfil:
                if (mayRequestStoragePermission()) {
                    showOptions();
                }

                break;
            case R.id.guardarperfil:
                uploadImage();
            case android.R.id.home:
                finish();
                break;
            default:
                break;

        }
        return true;
    }

    public String getStringImagen(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = android.util.Base64.encodeToString(imageBytes, android.util.Base64.DEFAULT);
        //Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    private void uploadImage() {
        //Mostrar el diálogo de progreso
        final ProgressDialog loading = ProgressDialog.show(this, "Subiendo...", "Espere por favor...", false, false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UPLOAD_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        //Descartar el diálogo de progreso
                        loading.dismiss();
                        //Mostrando el mensaje de la respuesta
                        Toast.makeText(FulImagen.this, s, Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        //Descartar el diálogo de progreso
                        loading.dismiss();

                        //Showing toast
                        Toast.makeText(FulImagen.this, volleyError.getMessage().toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //Convertir bits a cadena
                String imagen = getStringImagen(bitmap);


                String defaultValue = "DefaultName";
                SharedPreferences sharedPreferences = getSharedPreferences(FileNameGrupo, Context.MODE_PRIVATE);
                String idGrupoMusical = sharedPreferences.getString("idgrupomusical", defaultValue);
                //Obtener el nombre de la imagen
                String nombre = idGrupoMusical;

                //Creación de parámetros
                Map<String, String> params = new Hashtable<String, String>();

                //Agregando de parámetros
                params.put(KEY_IMAGEN, imagen);
                params.put(KEY_NOMBRE, nombre);

                //Parámetros de retorno
                return params;
            }
        };

        //Creación de una cola de solicitudes
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        //Agregar solicitud a la cola
        requestQueue.add(stringRequest);
    }


    private boolean mayRequestStoragePermission() {

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
            return true;

        if ((checkSelfPermission(WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) &&
                (checkSelfPermission(CAMERA) == PackageManager.PERMISSION_GRANTED))
            return true;

        if ((shouldShowRequestPermissionRationale(WRITE_EXTERNAL_STORAGE)) || (shouldShowRequestPermissionRationale(CAMERA))) {
            Snackbar.make(mRlView, "Los permisos son necesarios para poder usar la aplicación",
                    Snackbar.LENGTH_INDEFINITE).setAction(android.R.string.ok, new View.OnClickListener() {
                @TargetApi(Build.VERSION_CODES.M)
                @Override
                public void onClick(View v) {
                    requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE, CAMERA}, MY_PERMISSIONS);
                }
            });
        } else {
            requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE, CAMERA}, MY_PERMISSIONS);
        }

        return false;
    }

    private void showOptions() {
        final CharSequence[] option = {"Tomar foto", "Elegir de galeria", "Cancelar"};
        final AlertDialog.Builder builder = new AlertDialog.Builder(FulImagen.this);
        builder.setTitle("Elige una opción");
        builder.setItems(option, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (option[which] == "Tomar foto") {
                    openCamera();
                } else if (option[which] == "Elegir de galeria") {

                    Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(intent.createChooser(intent, "Selecciona app de imagen"), SELECT_PICTURE);
                } else {
                    dialog.dismiss();
                }
            }
        });

        builder.show();
    }

    private void openCamera() {
        File file = new File(Environment.getExternalStorageDirectory(), MEDIA_DIRECTORY);
        boolean isDirectoryCreated = file.exists();

        if (!isDirectoryCreated)
            isDirectoryCreated = file.mkdirs();

        if (isDirectoryCreated) {
            Long timestamp = System.currentTimeMillis() / 1000;
            String imageName = timestamp.toString() + ".jpg";

            mPath = Environment.getExternalStorageDirectory() + File.separator + MEDIA_DIRECTORY
                    + File.separator + imageName;

            File newFile = new File(mPath);

            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(newFile));
            startActivityForResult(intent, PHOTO_CODE);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("file_path", mPath);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PHOTO_CODE:
                    MediaScannerConnection.scanFile(this, new String[]{mPath}, null,
                            new MediaScannerConnection.OnScanCompletedListener() {
                                @Override
                                public void onScanCompleted(String path, Uri uri) {
                                    Log.i("ExternalStorage", "Scanned " + path + ":");
                                    Log.i("ExternalStorage", "-> Uri = " + uri);
                                }
                            });


                    bitmap = BitmapFactory.decodeFile(mPath);
                    iconoPerfil.setImageBitmap(rotateImage(bitmap, 90));
                    break;
                case SELECT_PICTURE:

                    // final ProgressDialog subiendoProgres = new ProgressDialog(FulImagen.this);
                    // subiendoProgres.setMessage("Subiendo foto....");
                    //subiendoProgres.show();
                    Uri filePath = data.getData();
                    try {
                        //Cómo obtener el mapa de bits de la Galería
                        bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                        //Configuración del mapa de bits en ImageView
                        iconoPerfil.setImageBitmap(bitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    //subiendoProgres.dismiss();
                    break;

            }
        }
    }

    private String getRandomString() {
        SecureRandom random = new SecureRandom();
        return new BigInteger(130, random).toString(32);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == MY_PERMISSIONS) {
            if (grantResults.length == 2 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(FulImagen.this, "Permisos aceptados", Toast.LENGTH_SHORT).show();
                mOptionButton.setEnabled(true);
            }
        } else {
            showExplanation();
        }
    }

    private void showExplanation() {
        AlertDialog.Builder builder = new AlertDialog.Builder(FulImagen.this);
        builder.setTitle("Permisos denegados");
        builder.setMessage("Para usar las funciones de la app necesitas aceptar los permisos");
        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getPackageName(), null);
                intent.setData(uri);
                startActivity(intent);
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                finish();
            }
        });

        builder.show();
    }


    public static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }


}



