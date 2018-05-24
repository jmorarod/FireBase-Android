package cr.ic.jmoraroditcr.firebase_android;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


public class AgregarProducto extends AppCompatActivity {
    private Bitmap bitmap;
    private Uri path;
    private Uri downloadPath;
    private ImageView image;
    private StorageReference mStorageRef;
    private TextView txtNombre;
    private TextView txtDescripcion;
    private TextView txtPrecio;
    DatabaseReference Database;

    public void agregar_producto(View view){
        String nombre = txtNombre.getText().toString();
        String description = txtDescripcion.getText().toString();
        String precio = txtPrecio.getText().toString();
        String ID_Usuario = FirebaseAuth.getInstance().getCurrentUser().getUid().toString();
        Item item = new Item(description, downloadPath.toString(), ID_Usuario, nombre, precio);
        String id = Database.push().getKey();
        Database.child(id).setValue(item);
        startActivity(new Intent(AgregarProducto.this, MainActivity.class));
    }

    public void cambiar_imagen(View view){
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/");
        startActivityForResult(intent.createChooser(intent,"Seleccione la Aplicación"),10);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_producto);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        image = findViewById(R.id.image);
        mStorageRef = FirebaseStorage.getInstance().getReference();
        txtNombre = findViewById(R.id.textView1);
        txtDescripcion = findViewById(R.id.textView2);
        txtPrecio = findViewById(R.id.textView3);
        Database = FirebaseDatabase.getInstance().getReference("Productos");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

            if(resultCode==RESULT_OK)
            {
                path = data.getData();

                image.setImageURI(path);
                StorageReference imagesRef;
                try {
                    imagesRef = mStorageRef.child("imagenes_tarea/" + txtNombre.getText().toString());
                }catch (Exception ex){
                    imagesRef = mStorageRef.child("imagenes_tarea/"+ UUID.randomUUID().toString());
                }
                imagesRef.putFile(path)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                // Get a URL to the uploaded content
                                downloadPath = taskSnapshot.getDownloadUrl();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                // Handle unsuccessful uploads
                                // ...
                            }
                        });

            }
        }
class ImageUploadTask extends AsyncTask<Uri,Void,Bitmap> {
    File file;
    FileInputStream fileInputStream;
    @Override
        protected Bitmap doInBackground(Uri... urls) {

            return null;
        }
    }
}
