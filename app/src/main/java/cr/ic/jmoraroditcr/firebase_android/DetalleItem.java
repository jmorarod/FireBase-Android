package cr.ic.jmoraroditcr.firebase_android;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class DetalleItem extends AppCompatActivity {

    private String nombre;
    private String descripcion;
    private String precio;
    private String pathFoto;
    private Bitmap bitmap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_item);
        Intent intent = getIntent();
        nombre = intent.getStringExtra("nombre");
        Log.d("Detalle Nombre",nombre);
        descripcion = intent.getStringExtra("descripcion");
        precio = intent.getStringExtra("precio");
        pathFoto = intent.getStringExtra("foto");

        TextView txtNombre = findViewById(R.id.textView1);
        TextView txtDescripcion = findViewById(R.id.textView2);
        TextView txtPrecio = findViewById(R.id.textView3);
        ImageView imageView = findViewById(R.id.imageView);
        ImageDownloadTask imageDownloadTask = new ImageDownloadTask();
        txtNombre.setText(nombre);
        txtDescripcion.setText(descripcion);
        txtPrecio.setText(precio);
        Log.d("Path Foto Detalle", pathFoto);

        try {
            bitmap = imageDownloadTask.execute(pathFoto).get();
            imageView.setImageBitmap(bitmap);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    class ImageDownloadTask extends AsyncTask<String,Void,Bitmap> {

        @Override
        protected Bitmap doInBackground(String... urls) {
            try {
                URL url = new URL(urls[0]);
                HttpURLConnection httpURLConnection =(HttpURLConnection)url.openConnection();
                Log.d("Background","Background");
                httpURLConnection.connect();
                InputStream inputStream = httpURLConnection.getInputStream();
                bitmap = BitmapFactory.decodeStream(inputStream);
                return bitmap;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
